package es.studium.urbanmiau.ui.tratamientos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.studium.urbanmiau.network.ApiConexion;

public class TratamientosViewModel extends ViewModel {

    private final MutableLiveData<List<Tratamiento>> mListaTratamientos =
            new MutableLiveData<>();

    private final MutableLiveData<String> mError =
            new MutableLiveData<>();

    public LiveData<List<Tratamiento>> getListaTratamientos() {
        return mListaTratamientos;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void cargarTratamientos() {

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            try {

                String respuestaJson =
                        ApiConexion.hacerPeticion(
                                "GET",
                                "http://10.0.2.2/urbanmiau/tratamientos.php",
                                ""
                        );

                JSONArray array =
                        new JSONArray(respuestaJson);

                List<Tratamiento> listaAux =
                        new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {

                    JSONObject t =
                            array.getJSONObject(i);

                    listaAux.add(
                            new Tratamiento(
                                    t.getInt("idTratamiento"),
                                    t.getString("descripcion")
                            )
                    );
                }

                mListaTratamientos.postValue(
                        listaAux
                );

            } catch (Exception e) {

                e.printStackTrace();

                mError.postValue(
                        e.getMessage()
                );
            }
        });
    }

    public void insertarTratamiento(
            String descripcion
    ) {

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            try {

                String parametros =
                        "descripcion="
                                + URLEncoder.encode(
                                descripcion,
                                "UTF-8"
                        );

                ApiConexion.hacerPeticion(
                        "POST",
                        "http://10.0.2.2/urbanmiau/tratamientos.php",
                        parametros
                );

                cargarTratamientos();

            } catch (Exception e) {

                e.printStackTrace();

                mError.postValue(
                        e.getMessage()
                );
            }
        });
    }

    public void actualizarTratamiento(
            int idTratamiento,
            String descripcion
    ) {

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            try {

                String parametros =
                        "idTratamiento="
                                + idTratamiento
                                + "&descripcion="
                                + URLEncoder.encode(
                                descripcion,
                                "UTF-8"
                        );

                ApiConexion.hacerPeticion(
                        "PUT",
                        "http://10.0.2.2/urbanmiau/tratamientos.php",
                        parametros
                );

                cargarTratamientos();

            } catch (Exception e) {

                e.printStackTrace();

                mError.postValue(
                        e.getMessage()
                );
            }
        });
    }

    public void eliminarTratamiento(
            int idTratamiento
    ) {

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            try {

                ApiConexion.hacerPeticion(
                        "DELETE",
                        "http://10.0.2.2/urbanmiau/tratamientos.php?idTratamiento="
                                + idTratamiento,
                        ""
                );

                cargarTratamientos();

            } catch (IOException e) {

                e.printStackTrace();

                mError.postValue(
                        e.getMessage()
                );
            }
        });
    }
}