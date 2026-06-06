package es.studium.urbanmiau.crudgatos;

import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BajaGatos {

    private final OkHttpClient client = new OkHttpClient();

    // Definimos una interfaz para devolver la respuesta al Fragment
    public interface CallbackBaja {
        void onSuccess();
        void onError(String mensajeError);
    }

    public void darBaja(long idGato, CallbackBaja callback) {
        Request request = new Request.Builder()
                .url("http://10.0.2.2/urbanmiau/gatos.php?idGato=" + idGato)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BajaGato", "Error de red: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respuesta = response.body() != null ? response.body().string() : "";
                Log.i("BajaGato", "ID enviado: " + idGato);
                Log.i("BajaGato", "Código HTTP: " + response.code());
                Log.i("BajaGato", "Respuesta PHP: " + respuesta);

                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Código de servidor: " + response.code());
                }
            }
        });
    }
}