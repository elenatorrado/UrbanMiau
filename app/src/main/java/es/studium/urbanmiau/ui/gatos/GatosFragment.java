package es.studium.urbanmiau.ui.gatos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.studium.urbanmiau.adapters.GatoAdapter;
import es.studium.urbanmiau.databinding.FragmentGatosBinding;

import es.studium.urbanmiau.crudgatos.ConsultaGato;
import es.studium.urbanmiau.pojo.Gato;

public class GatosFragment extends Fragment {

    private FragmentGatosBinding binding;

    private List<Gato> lista = new ArrayList<>();
    private GatoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGatosBinding.inflate(inflater, container, false);

        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder().permitAll().build()
        );

        binding.rvGatos.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        cargarDatos();

        return binding.getRoot();
    }

    private void cargarDatos() {

        ConsultaGato consulta = new ConsultaGato();
        JSONArray datos = consulta.obtenerGatos();

        lista.clear();

        try {
            for (int i = 0; i < datos.length(); i++) {

                JSONObject obj = datos.getJSONObject(i);

                lista.add(new Gato(
                        obj.getString("nombre"),
                        obj.getString("edad"),
                        obj.optString("nombreColonia"),
                        obj.optString("nombreCuidadora"),
                        obj.optString("foto")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new GatoAdapter(lista, new GatoAdapter.OnGatoClick() {

            @Override
            public void onClick(Gato gato) {
                abrirModificar(gato);
            }

            @Override
            public void onLongClick(Gato gato) {
                confirmarBorrado(gato);
            }
        });

        binding.rvGatos.setAdapter(adapter);
    }

    private void confirmarBorrado(Gato gato) {

        new AlertDialog.Builder(getContext())
                .setTitle("Borrar gato")
                .setMessage("¿Seguro que quieres borrar a " + gato.getNombre() + "?")
                .setPositiveButton("Sí", (d, w) -> {

                   es.studium.urbanmiau.baja.BajaGatos baja = new es.studium.urbanmiau.baja.BajaGatos();
                    baja.darBaja(gato.getIdGato());

                    cargarDatos();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void abrirModificar(Gato gato) {
        // aquí abrirías tu Activity o Dialog
        // Intent intent = new Intent(getContext(), ModificarGatoActivity.class);
        // intent.putExtra("id", gato.getIdGato());
        // startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}