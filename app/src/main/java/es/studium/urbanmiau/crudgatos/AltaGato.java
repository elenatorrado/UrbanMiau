package es.studium.urbanmiau.crudgatos;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.studium.urbanmiau.pojo.ItemGato;

public class AltaGato {

    private final RequestQueue queue;
    private final Context context; // 🔥 Guardamos el contexto de la UI activa

    public interface CallbackLista {
        void onSuccess(List<ItemGato> lista);
        void onError(Exception e);
    }

    // 🔥 CORREGIDO: Usamos el contexto directo de la actividad/vista, NO el ApplicationContext
    public AltaGato(Context ctx) {
        this.context = ctx;
        this.queue = Volley.newRequestQueue(ctx);
    }

    // =========================
    // SPINNERS API
    // =========================

    public void getColonias(CallbackLista c) {
        getLista("http://10.0.2.2/urbanmiau/gatos.php?tipo=COLONIAS", c);
    }

    public void getCuidadoras(CallbackLista c) {
        getLista("http://10.0.2.2/urbanmiau/gatos.php?tipo=CUIDADORAS", c);
    }

    public void getClinicas(CallbackLista c) {
        getLista("http://10.0.2.2/urbanmiau/gatos.php?tipo=CLINICAS", c);
    }

    // =========================
    // GET LISTA (CORREGIDO Y CAZADOR DE ERRORES)
    // =========================

    private void getLista(String url, CallbackLista callback) {

        Log.d("SPINNER_URL", "Solicitando a URL: " + url);

        com.android.volley.toolbox.StringRequest req =
                new com.android.volley.toolbox.StringRequest(
                        Request.Method.GET,
                        url,
                        response -> {
                            try {
                                Log.d("SPINNER_RAW", "Respuesta en crudo: " + response);

                                if (response == null || response.trim().isEmpty()) {
                                    throw new Exception("El servidor devolvió un texto vacío.");
                                }

                                response = response.trim();

                                // 🔥 Evitar basura o warnings de PHP fuera de los corchetes
                                int start = response.indexOf("[");
                                int end = response.lastIndexOf("]");

                                if (start == -1 || end == -1) {
                                    throw new Exception("La respuesta del servidor no contiene un JSON Array válido. Respuesta: " + response);
                                }

                                String clean = response.substring(start, end + 1);
                                JSONArray arr = new JSONArray(clean);
                                List<ItemGato> lista = new ArrayList<>();

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject o = arr.getJSONObject(i);
                                    lista.add(new ItemGato(
                                            o.optInt("id"),
                                            o.optString("nombre")
                                    ));
                                }

                                Log.d("SPINNER_SUCCESS", "Mapeados correctamente " + lista.size() + " elementos.");
                                callback.onSuccess(lista);

                            } catch (Exception e) {
                                Log.e("SPINNER_PARSE", "Error al procesar el JSON de la respuesta", e);

                                // Alerta visual en pantalla por si el PHP se rompe por dentro
                                new AlertDialog.Builder(context)
                                        .setTitle("⚠️ Error de Formato PHP")
                                        .setMessage("El PHP respondió, pero no devolvió un JSON correcto.\n\nDetalle: " + e.getMessage())
                                        .setPositiveButton("Revisar", null)
                                        .show();

                                callback.onError(e);
                            }
                        },
                        error -> {
                            Log.e("SPINNER_NETWORK", "Error de red en Volley", error);

                            // 🔥 CAZADOR DE ERRORES DEFINITIVO EN PANTALLA
                            String msg = "No se pudo conectar con el servidor PHP.\n\n";
                            if (error.networkResponse != null) {
                                msg += "Código de estado HTTP: " + error.networkResponse.statusCode;
                            } else {
                                msg += "Detalle: " + error.toString() + "\n\nRevisa que XAMPP esté encendido y que uses la IP correcta (10.0.2.2 para emulador).";
                            }

                            new AlertDialog.Builder(context)
                                    .setTitle("❌ Error de Conexión de Red")
                                    .setMessage(msg)
                                    .setPositiveButton("Entendido", null)
                                    .show();

                            callback.onError(new Exception(error));
                        }
                );

        req.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                1,
                1.0f
        ));

        queue.add(req);
    }

    // =========================
    // CREAR GATO (POST + IMAGEN)
    // =========================

    public void crear(
            String nombre,
            int edad,
            String sexo,
            int idColonia,
            int idCuidadora,
            int idClinica,
            Uri imageUri,
            Context context,
            com.android.volley.Response.Listener<String> success,
            com.android.volley.Response.ErrorListener error
    ) {
        VolleyMultipartRequest req = new VolleyMultipartRequest(
                Request.Method.POST,
                "http://10.0.2.2/urbanmiau/gatos.php",
                success,
                error
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operacion", "insert"); // 🔥 ¡ESTO FALTABA!
                params.put("nombre", nombre);
                params.put("edad", String.valueOf(edad));
                params.put("sexo", sexo);
                params.put("idColonia", String.valueOf(idColonia));
                params.put("idCuidadora", String.valueOf(idCuidadora));
                params.put("idClinica", String.valueOf(idClinica));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> data = new HashMap<>();
                try {
                    if (imageUri != null) {
                        InputStream is = context.getContentResolver().openInputStream(imageUri);
                        if (is != null) {
                            byte[] bytes = readBytes(is);
                            data.put("imagen", new DataPart("gato.jpg", bytes, "image/jpeg"));
                        }
                    }
                } catch (Exception e) {
                    Log.e("UPLOAD_IMAGE", "Error leyendo bytes", e);
                }
                return data;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
        queue.add(req);
    }

    private byte[] readBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }
}