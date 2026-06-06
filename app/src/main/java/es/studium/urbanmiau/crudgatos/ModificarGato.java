package es.studium.urbanmiau.crudgatos;

import android.content.Context;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModificarGato
{
    private final Context context;
    private final OkHttpClient client;

    public ModificarGato(Context context)
    {
        this.context = context;
        this.client = new OkHttpClient();
    }

    public void ejecutar(
            String operacion,
            long idGato,
            String nombre,
            int edad,
            String sexo,
            int idColonia,
            int idCuidadora,
            int idClinica,
            String fotoActual,
            Uri nuevaFoto,
            final CallbackResultado callback)
    {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("operacion", operacion)
                .addFormDataPart("idGato", String.valueOf(idGato))
                .addFormDataPart("nombre", nombre)
                .addFormDataPart("edad", String.valueOf(edad))
                .addFormDataPart("sexo", sexo)
                .addFormDataPart("idColonia", String.valueOf(idColonia))
                .addFormDataPart("idCuidadora", String.valueOf(idCuidadora))
                .addFormDataPart("idClinica", String.valueOf(idClinica));

        if (fotoActual != null)
        {
            builder.addFormDataPart("foto_actual", fotoActual);
        }

        if (nuevaFoto != null)
        {
            try
            {
                InputStream inputStream =
                        context.getContentResolver().openInputStream(nuevaFoto);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    bos.write(buffer, 0, bytesRead);
                }

                byte[] imagenBytes = bos.toByteArray();

                builder.addFormDataPart(
                        "imagen",
                        "gato.jpg",
                        RequestBody.create(
                                imagenBytes,
                                MediaType.parse("image/jpeg")
                        )
                );

                inputStream.close();
                bos.close();
            }
            catch (Exception e)
            {
                callback.onError(e.getMessage());
                return;
            }
        }

        Request request = new Request.Builder()
                .url("http://10.0.2.2/urbanmiau/gatos.php")
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    callback.onSuccess();
                }
                else
                {
                    callback.onError(
                            "Error del servidor: " + response.code()
                    );
                }

                response.close();
            }
        });
    }

    public interface CallbackResultado
    {
        void onSuccess();
        void onError(String error);
    }
}