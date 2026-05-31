package es.studium.urbanmiau.ui.clinicas;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.studium.urbanmiau.network.ApiConexion;

public class ClinicasViewModel extends ViewModel {

    private static final String TAG = "URBANMIAU_VIEWMODEL";
    private static final String TAG2 = "PARAMETROS";

    // Lista observable por el Fragment
    private final MutableLiveData<List<Clinica>> mListaClinicas = new MutableLiveData<>();
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LiveData<List<Clinica>> getListaClinicas() {
        return mListaClinicas;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void cargarClinicas() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                String respuestaJson = ApiConexion.hacerPeticion("GET", "http://10.0.2.2/urbanmiau/clinicas.php", "");

                Log.d(TAG, "Respuesta del servidor: " + respuestaJson);

                List<Clinica> listaAux = new ArrayList<>();
                JSONArray array = new JSONArray(respuestaJson);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject c = array.getJSONObject(i);
                    listaAux.add(new Clinica(
                            c.getInt("idClinica"),
                            c.getString("nombre"),
                            c.getString("direccion"),
                            c.getString("telefono"),
                            c.getString("ciudad")
                    ));
                }

                mListaClinicas.postValue(listaAux);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                mError.postValue("Error al conectar con el servidor local: " + e.getMessage());
                Log.e(TAG, "Excepción capturada: " + e.getMessage());
            }
        });
    }

    public void insertarClinica(String nombre, String direccion, String telefono, String nombreCiudad) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String parametros = "nombre_clinica=" + java.net.URLEncoder.encode(nombre, "UTF-8") +
                        "&direccion_clinica=" + java.net.URLEncoder.encode(direccion, "UTF-8") +
                        "&telefono_clinica=" + java.net.URLEncoder.encode(telefono, "UTF-8") +
                        "&ciudad=" + java.net.URLEncoder.encode(nombreCiudad, "UTF-8");
                Log.d(TAG2, "Parámetros a enviar: " + parametros);

                //  URL cambiada a clinica.php (singular)
                String respuesta = ApiConexion.hacerPeticion("POST", "http://10.0.2.2/urbanmiau/clinicas.php", parametros);

                cargarClinicas();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Error al registrar la clínica: " + e.getMessage());
                mError.postValue("Error al registrar la clínica: " + e.getMessage());
            }
        });
    }

    public void eliminarClinica(int idClinica) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                android.util.Log.d("URBANMIAU_DEBUG", "Solicitando borrar clínica con ID: " + idClinica);


                String urlConParametro = "http://10.0.2.2/urbanmiau/clinicas.php?idClinica=" + idClinica;

                String respuesta = es.studium.urbanmiau.network.ApiConexion.hacerPeticion("DELETE", urlConParametro, "");

                android.util.Log.d("URBANMIAU_DEBUG", "Respuesta DELETE de XAMPP: " + respuesta);

                cargarClinicas();

            } catch (java.io.IOException e) {
                e.printStackTrace();
                android.util.Log.e("URBANMIAU_DEBUG", "Error al ejecutar el DELETE: " + e.getMessage());
                mError.postValue("No se pudo eliminar la clínica: " + e.getMessage());
            }
        });
    }

    // Se añade 'String nombreCiudad' como parámetro para solucionar el error de compilación
    public void actualizarClinica(int idClinica, String nombre, String direccion, String telefono, String nombreCiudad) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                android.util.Log.d("URBANMIAU_DEBUG", "Intentando actualizar clínica ID: " + idClinica);
                //

                // 🟢 TRUCO INFALIBLE: Construimos los parámetros como formulario de texto, no como JSON complejo
                String parametros = "idClinica=" + idClinica +
                        "&nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8") +
                        "&direccion=" + java.net.URLEncoder.encode(direccion, "UTF-8") +
                        "&telefono=" + java.net.URLEncoder.encode(telefono, "UTF-8") +
                        "&ciudad=" + java.net.URLEncoder.encode(nombreCiudad, "UTF-8");

                android.util.Log.d("URBANMIAU_DEBUG", "Parámetros PUT a enviar: " + parametros);

                String respuesta = es.studium.urbanmiau.network.ApiConexion.hacerPeticion("PUT", "http://10.0.2.2/urbanmiau/clinicas.php", parametros);

                android.util.Log.d("URBANMIAU_DEBUG", "Respuesta PUT de XAMPP: " + respuesta);

                cargarClinicas();

            } catch (Exception e) {
                e.printStackTrace();
                android.util.Log.e("URBANMIAU_DEBUG", "Error al actualizar: " + e.getMessage());
                mError.postValue("Error al actualizar la clínica: " + e.getMessage());
            }
        });
    }
}