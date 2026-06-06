package es.studium.urbanmiau.ui.gatos;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import es.studium.urbanmiau.R;
import es.studium.urbanmiau.adapters.GatoAdapter;
import es.studium.urbanmiau.crudgatos.AltaGato;
import es.studium.urbanmiau.crudgatos.BajaGatos;
import es.studium.urbanmiau.crudgatos.ConsultaGato;
import es.studium.urbanmiau.crudgatos.ModificarGato;
import es.studium.urbanmiau.pojo.Gato;
import es.studium.urbanmiau.pojo.ItemGato;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GatosFragment extends Fragment {

    private RecyclerView recyclerView;
    private GatoAdapter gatoAdapter;
    private List<Gato> listaGatos;
    private List<Gato> listaGatosFiltrada;
    private FloatingActionButton fabAddGato;
    private EditText etBuscador;

    private List<ItemGato> listaColonias = new ArrayList<>();
    private List<ItemGato> listaCuidadoras = new ArrayList<>();
    private List<ItemGato> listaClinicas = new ArrayList<>();

    private AltaGato altaGatoService;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri uriFotoSeleccionada = null;
    private ImageView imgPreviewGlobal;
    private ModificarGato modificarGatoService;

    public GatosFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                uriFotoSeleccionada = uri;
                if (imgPreviewGlobal != null) {
                    imgPreviewGlobal.setImageURI(uri);
                }
                Log.d("DEBUG_URI", "URI capturada: " + uri.toString());
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_gatos, container, false);

        fabAddGato = vista.findViewById(R.id.fabAddGato);
        if (fabAddGato != null) {
            fabAddGato.setOnClickListener(v -> abrirDialogoFormulario(null));
        }

        etBuscador = vista.findViewById(R.id.etBuscarGatos);
        if (etBuscador != null) {
            etBuscador.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filtrarBuscador(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvGatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaGatos = new ArrayList<>();
        listaGatosFiltrada = new ArrayList<>();

        gatoAdapter = new GatoAdapter(listaGatosFiltrada, new GatoAdapter.OnGatoClick() {
            @Override
            public void onClick(Gato g) {
                abrirDialogoFormulario(g);
            }

            @Override
            public void onLongClick(Gato g) {
                abrirDialogoBajaGato(g);
            }
        });

        recyclerView.setAdapter(gatoAdapter);

        altaGatoService = new AltaGato(requireContext());

        cargarSpinnersDesdeBD();
        cargarGatosDesdeServidor();
    }

    private void filtrarBuscador(String texto) {
        listaGatosFiltrada.clear();
        if (texto.isEmpty()) {
            listaGatosFiltrada.addAll(listaGatos);
        } else {
            for (Gato g : listaGatos) {
                if (g.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                        g.getNombreColonia().toLowerCase().contains(texto.toLowerCase())) {
                    listaGatosFiltrada.add(g);
                }
            }
        }
        if (gatoAdapter != null) {
            gatoAdapter.notifyDataSetChanged();
        }
    }

    private void abrirDialogoFormulario(@Nullable Gato gatoAEditar) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_crear_gato, null);

        ImageView imgPreview = dialogView.findViewById(R.id.imgPreview);
        Button btnFoto = dialogView.findViewById(R.id.btnfoto);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etEdad = dialogView.findViewById(R.id.etEdad);
        Spinner spColonia = dialogView.findViewById(R.id.spColonia);
        Spinner spCuidadora = dialogView.findViewById(R.id.spCuidadora);
        Spinner spClinica = dialogView.findViewById(R.id.spClinica);

        imgPreviewGlobal = imgPreview;
        uriFotoSeleccionada = null;

        ArrayAdapter<ItemGato> adapterColonia = new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_spinner_item, listaColonias);
        adapterColonia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColonia.setAdapter(adapterColonia);

        ArrayAdapter<ItemGato> adapterCuidadora = new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_spinner_item, listaCuidadoras);
        adapterCuidadora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCuidadora.setAdapter(adapterCuidadora);

        ArrayAdapter<ItemGato> adapterClinica = new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_spinner_item, listaClinicas);
        adapterClinica.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClinica.setAdapter(adapterClinica);

        boolean esEdicion = (gatoAEditar != null);

        if (esEdicion) {
            etNombre.setText(gatoAEditar.getNombre());
            etEdad.setText(gatoAEditar.getEdad());

            if (gatoAEditar.getFoto() != null && !gatoAEditar.getFoto().equalsIgnoreCase("null")) {
                Glide.with(dialogView.getContext()).load(gatoAEditar.getFoto()).placeholder(R.drawable.gatoimg).into(imgPreview);
            }

            for (int i = 0; i < listaColonias.size(); i++) {
                if (listaColonias.get(i).getNombre().equals(gatoAEditar.getNombreColonia())) {
                    spColonia.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < listaCuidadoras.size(); i++) {
                if (listaCuidadoras.get(i).getNombre().equals(gatoAEditar.getNombreCuidadora())) {
                    spCuidadora.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < listaClinicas.size(); i++) {
                if (listaClinicas.get(i).getNombre().equals(gatoAEditar.getNombreClinica())) {
                    spClinica.setSelection(i);
                    break;
                }
            }
        }

        if (btnFoto != null) {
            btnFoto.setOnClickListener(v -> pickMedia.launch(
                    new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build()
            ));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        builder.setPositiveButton(esEdicion ? "Actualizar" : "Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String edadStr = etEdad.getText().toString().trim();

            ItemGato coloniaSeleccionada = (ItemGato) spColonia.getSelectedItem();
            ItemGato cuidadoraSeleccionada = (ItemGato) spCuidadora.getSelectedItem();
            ItemGato clinicaSeleccionada = (ItemGato) spClinica.getSelectedItem();

            if (nombre.isEmpty() || edadStr.isEmpty() || coloniaSeleccionada == null || cuidadoraSeleccionada == null || clinicaSeleccionada == null) {
                Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int edad = Integer.parseInt(edadStr);
            String sexo = gatoAEditar != null ? gatoAEditar.getSexo() : "Macho";

            if (esEdicion) {
                actualizarGatoOkHttp(gatoAEditar.getIdGato(), nombre, edad, sexo, coloniaSeleccionada.getId(), cuidadoraSeleccionada.getId(), clinicaSeleccionada.getId(), gatoAEditar.getFoto());
            } else {
                altaGatoService.crear(nombre, edad, sexo, coloniaSeleccionada.getId(), cuidadoraSeleccionada.getId(), clinicaSeleccionada.getId(), uriFotoSeleccionada, getContext(),
                        response -> {
                            Toast.makeText(getContext(), "Gato registrado con éxito", Toast.LENGTH_SHORT).show();
                            cargarGatosDesdeServidor();
                        },
                        error -> Toast.makeText(getContext(), "Error al guardar el gato", Toast.LENGTH_SHORT).show()
                );
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void abrirDialogoBajaGato(Gato gato) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Eliminar registro");
        builder.setMessage("¿Estás segura de que deseas dar de baja a " + gato.getNombre() + "?");

        builder.setPositiveButton("Confirmar Baja", (dialog, which) -> {
            new BajaGatos().darBaja(gato.getIdGato(), new BajaGatos.CallbackBaja() {
                @Override
                public void onSuccess() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), gato.getNombre() + " dado de baja", Toast.LENGTH_SHORT).show();
                            cargarGatosDesdeServidor();
                        });
                    }
                }

                @Override
                public void onError(String mensajeError) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Error al dar de baja: " + mensajeError, Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void actualizarGatoOkHttp(long idGato, String nombre, int edad, String sexo, int idColonia, int idCuidadora, int idClinica, String fotoUrlOpcional) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("operacion", "update")
                .addFormDataPart("idGato", String.valueOf(idGato))

                .addFormDataPart("nombre", nombre)
                .addFormDataPart("edad", String.valueOf(edad))
                .addFormDataPart("sexo", sexo)
                .addFormDataPart("idColonia", String.valueOf(idColonia))
                .addFormDataPart("idCuidadora", String.valueOf(idCuidadora))
                .addFormDataPart("idClinica", String.valueOf(idClinica));

        if (uriFotoSeleccionada != null) {
            try {
                InputStream is = getContext().getContentResolver().openInputStream(uriFotoSeleccionada);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                byte[] imagenBytes = bos.toByteArray();

                builder.addFormDataPart("imagen", "gato_editado.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), imagenBytes));

            } catch (Exception e) {
                Log.e("URBANMIAU_UPDATE", "Error al procesar los bytes de la nueva imagen", e);
            }
        } else {
            builder.addFormDataPart("foto", fotoUrlOpcional != null ? fotoUrlOpcional : "null");
        }

        Request request = new Request.Builder()
                .url("http://10.0.2.2/urbanmiau/gatos.php")
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error de red al actualizar", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Gato actualizado correctamente", Toast.LENGTH_SHORT).show();
                            uriFotoSeleccionada = null;
                            cargarGatosDesdeServidor();
                        } else {
                            Toast.makeText(getContext(), "Error en el servidor al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void cargarGatosDesdeServidor() {
        new ConsultaGato().obtenerGatos(new ConsultaGato.CallbackGatos() {
            @Override
            public void onSuccess(JSONArray data) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            listaGatos.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
                                Gato gato = new Gato();
                                gato.setIdGato(jsonObject.getInt("idGato"));
                                gato.setNombre(jsonObject.optString("nombre", "Sin nombre"));
                                gato.setEdad(jsonObject.optString("edad", "Desconocida"));
                                gato.setNombreColonia(jsonObject.optString("nombreColonia", "Sin colonia"));
                                gato.setNombreCuidadora(jsonObject.optString("nombreCuidadora", "Sin cuidador/a"));
                                gato.setNombreClinica(jsonObject.optString("nombreClinica", "Sin clínica"));
                                gato.setFoto(jsonObject.optString("foto", "null"));
                                if (jsonObject.has("sexo")) {
                                    gato.setSexo(jsonObject.getString("sexo"));
                                }
                                listaGatos.add(gato);
                            }
                            filtrarBuscador(etBuscador != null ? etBuscador.getText().toString() : "");
                        } catch (Exception e) {
                            Log.e("URBANMIAU_ERROR", "Error JSON: " + e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("URBANMIAU_ERROR", "Error OkHttp: " + e.getMessage());
            }
        });
    }

    private void cargarSpinnersDesdeBD() {
        altaGatoService.getColonias(new AltaGato.CallbackLista() {
            @Override
            public void onSuccess(List<ItemGato> lista) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        listaColonias.clear();
                        listaColonias.addAll(lista);
                    });
                }
            }
            @Override
            public void onError(Exception e) {}
        });

        altaGatoService.getCuidadoras(new AltaGato.CallbackLista() {
            @Override
            public void onSuccess(List<ItemGato> lista) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        listaCuidadoras.clear();
                        listaCuidadoras.addAll(lista);
                    });
                }
            }
            @Override
            public void onError(Exception e) {}
        });

        altaGatoService.getClinicas(new AltaGato.CallbackLista() {
            @Override
            public void onSuccess(List<ItemGato> lista) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        listaClinicas.clear();
                        listaClinicas.addAll(lista);
                    });
                }
            }
            @Override
            public void onError(Exception e) {}
        });
    }
}