package es.studium.urbanmiau.crudgatos;

import android.util.Log;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.*;

public class ConsultaGato {

    OkHttpClient client = new OkHttpClient();

    public interface CallbackGatos {
        void onSuccess(JSONArray data);
        void onError(Exception e);
    }

    public void obtenerGatos(CallbackGatos callback) {

        Request request = new Request.Builder()
                .url("http://10.0.2.2/urbanmiau/gatos.php")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String json = response.body().string();
                    JSONArray arr = new JSONArray(json);
                    callback.onSuccess(arr);

                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        });
    }
}