package es.studium.urbanmiau.crudgatos;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<String> {

    private final Response.Listener<String> listener;
    private final Map<String, String> params;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.params = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }

    protected Map<String, DataPart> getByteData() {
        return null;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String res = new String(response.data);
        return Response.success(res, null);
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

    public static class DataPart {
        public String fileName;
        public byte[] content;

        public DataPart(String fileName, byte[] content, String contentType) {
            this.fileName = fileName;
            this.content = content;
        }
    }
}