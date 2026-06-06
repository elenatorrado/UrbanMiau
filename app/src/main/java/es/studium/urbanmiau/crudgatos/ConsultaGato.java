package es.studium.urbanmiau.crudgatos;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConsultaGato { OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
        .url("http://10.0.2.2/urbanmiau/gatos.php")
        .build();

public ConsultaGato() {}


public JSONArray obtenerGatos() {
    JSONArray resultado = new JSONArray();
    try {
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {

            resultado = new JSONArray(response.body().string());
        } else {
            Log.e("ConsultaGato", "Error: " + response.message());
        }
    } catch (IOException e) {
        Log.e("ConsultaGato", "Error IO: " + e.getMessage());
    } catch (JSONException e) {
        Log.e("ConsultaGato", "Error JSON: " + e.getMessage());
    }
    return resultado;
}
}
