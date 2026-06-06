package es.studium.urbanmiau.ui.colonias;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.studium.urbanmiau.network.ApiConexion;

public class ColoniasViewModel extends ViewModel {

    private static final String TAG = "URBANMIAU_COLONIAS";

    private final MutableLiveData<List<Colonia>> mListaColonias = new MutableLiveData<>();
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LiveData<List<Colonia>> getListaColonias() {
        return mListaColonias;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void cargarColonias() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {

                String respuestaJson = ApiConexion.hacerPeticion(
                        "GET",
                        "http://10.0.2.2/urbanmiau/colonias.php",
                        ""
                );

                Log.d(TAG, respuestaJson);

                List<Colonia> listaAux = new ArrayList<>();

                JSONArray array = new JSONArray(respuestaJson);

                for (int i = 0; i < array.length(); i++) {

                    JSONObject c = array.getJSONObject(i);

                    listaAux.add(
                            new Colonia(
                                    c.getInt("idColonia"),
                                    c.getString("nombre"),
                                    c.getString("cuidador"),
                                    c.getString("ciudad"),
                                    c.getString("clinica")
                            )
                    );
                }

                mListaColonias.postValue(listaAux);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                mError.postValue(e.getMessage());
            }
        });
    }

    public void insertarColonia(String nombre, String ciudad) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {

                String parametros =
                        "nombre=" + URLEncoder.encode(nombre, "UTF-8")
                                + "&ciudad=" + URLEncoder.encode(ciudad, "UTF-8");

                ApiConexion.hacerPeticion(
                        "POST",
                        "http://10.0.2.2/urbanmiau/colonias.php",
                        parametros
                );

                cargarColonias();

            } catch (Exception e) {
                e.printStackTrace();
                mError.postValue(e.getMessage());
            }
        });
    }

    public void eliminarColonia(int idColonia) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {

                String url =
                        "http://10.0.2.2/urbanmiau/colonias.php?idColonia="
                                + idColonia;

                ApiConexion.hacerPeticion(
                        "DELETE",
                        url,
                        ""
                );

                cargarColonias();

            } catch (Exception e) {
                e.printStackTrace();
                mError.postValue(e.getMessage());
            }
        });
    }

    public void actualizarColonia(
            int idColonia,
            String nombre,
            String ciudad
    ) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {

                String parametros =
                        "idColonia=" + idColonia
                                + "&nombre=" + URLEncoder.encode(nombre, "UTF-8")
                                + "&ciudad=" + URLEncoder.encode(ciudad, "UTF-8");

                ApiConexion.hacerPeticion(
                        "PUT",
                        "http://10.0.2.2/urbanmiau/colonias.php",
                        parametros
                );

                cargarColonias();

            } catch (Exception e) {
                e.printStackTrace();
                mError.postValue(e.getMessage());
            }
        });
    }
}