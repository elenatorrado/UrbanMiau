package es.studium.urbanmiau.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class ApiConexion {


    public static String hacerPeticion(String metodo, String urlString, String parametros) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod(metodo);
        conexion.setConnectTimeout(10000);
        conexion.setReadTimeout(10000);

        if (metodo.equals("POST") || metodo.equals("PUT")) {
            conexion.setDoOutput(true);
            conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream salida = conexion.getOutputStream();
            salida.write(parametros.getBytes("UTF-8"));
            salida.flush();
            salida.close();
        }

        BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        StringBuilder respuesta = new StringBuilder();
        String linea;

        while ((linea = lector.readLine()) != null) {
            respuesta.append(linea);
        }

        lector.close();
        conexion.disconnect();
        return respuesta.toString();
    }
}