package es.studium.urbanmiau.baja;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BajaGatos {

    OkHttpClient client = new OkHttpClient();

    public BajaGatos() {}

    public boolean darBaja(long idGato) {

        boolean correcta = true;

        Request request = new Request.Builder()
                .url("http://10.0.2.2/urbanmiau/gatos.php?idGato=" + idGato)
                .delete()
                .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            Log.i("BajaGato", String.valueOf(response));
            Log.i("BajaGato", "Enviando petición de baja para ID: " + idGato);
            correcta = true;

        } catch (IOException e) {
            Log.e("BajaGato", e.getMessage());
            correcta = false;
        }

        return correcta;
    }
}