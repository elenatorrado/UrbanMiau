package es.studium.urbanmiau.ui.colonias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.studium.urbanmiau.R;

public class ColoniasFragment extends Fragment {

    private RecyclerView rvColonias;
    private ColoniaAdapter adapter;
    private List<Colonia> listaColonias;
    private ColoniasViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(
                R.layout.fragment_colonias,
                container,
                false
        );

        rvColonias = vista.findViewById(R.id.rvColonias);
        rvColonias.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        listaColonias = new ArrayList<>();

        adapter = new ColoniaAdapter(
                listaColonias,
                colonia -> abrirDetalleColonia(colonia),
                colonia -> mostrarDialogoConfirmacionBorrar(colonia)
        );

        rvColonias.setAdapter(adapter);

        return vista;
    }

    private void abrirDetalleColonia(Colonia colonia) {

        Bundle datos = new Bundle();

        datos.putInt(
                "idColonia",
                colonia.getIdColonia()
        );

        datos.putString(
                "nombreColonia",
                colonia.getNombre()
        );

        datos.putString(
                "cuidadorColonia",
                colonia.getCuidador()
        );

        datos.putString(
                "ciudadColonia",
                colonia.getCiudad()
        );

        datos.putString(
                "clinicaColonia",
                colonia.getClinica()
        );

        Navigation.findNavController(requireView())
                .navigate(
                        R.id.action_nav_colonias_to_coloniasInfoFragment,
                        datos
                );
    }

    private void mostrarDialogoConfirmacionBorrar(
            Colonia colonia
    ) {

        new AlertDialog.Builder(requireContext())
                .setTitle("¿Eliminar colonia?")
                .setMessage(
                        "¿Seguro que deseas eliminar la colonia \""
                                + colonia.getNombre()
                                + "\"?"
                )
                .setPositiveButton(
                        "Sí",
                        (dialog, which) -> {

                            viewModel.eliminarColonia(
                                    colonia.getIdColonia()
                            );

                            Toast.makeText(
                                    getContext(),
                                    "Eliminando colonia...",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .setNegativeButton(
                        "Cancelar",
                        (dialog, which) -> dialog.dismiss()
                )
                .show();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(ColoniasViewModel.class);

        viewModel.getListaColonias()
                .observe(getViewLifecycleOwner(), colonias -> {

                    listaColonias.clear();
                    listaColonias.addAll(colonias);

                    adapter.notifyDataSetChanged();
                });

        viewModel.getError()
                .observe(getViewLifecycleOwner(), error -> {

                    if (error != null) {

                        Toast.makeText(
                                getContext(),
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

        viewModel.cargarColonias();

        FloatingActionButton fab =
                view.findViewById(
                        R.id.floatingActionButton3
                );

        fab.setOnClickListener(
                v -> mostrarDialogoAlta()
        );
    }

    private void mostrarDialogoAlta() {

        BottomSheetDialog dialog =
                new BottomSheetDialog(requireContext());

        View vistaDialogo =
                getLayoutInflater().inflate(
                        R.layout.dialog_alta_colonia,
                        null
                );

        dialog.setContentView(vistaDialogo);

        EditText etNombre =
                vistaDialogo.findViewById(
                        R.id.etAltaNombreColonia
                );

        Spinner spCiudad =
                vistaDialogo.findViewById(
                        R.id.spAltaCiudadColonia
                );

        Button btnCancelar =
                vistaDialogo.findViewById(
                        R.id.btnCancelarAltaColonia
                );

        Button btnGuardar =
                vistaDialogo.findViewById(
                        R.id.btnGuardarAltaColonia
                );

        String[] ciudades = {
                "Madrid",
                "Toledo",
                "Sevilla",
                "Barcelona"
        };

        ArrayAdapter<String> adapterSpinner =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        ciudades
                );

        adapterSpinner.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCiudad.setAdapter(adapterSpinner);

        btnCancelar.setOnClickListener(
                v -> dialog.dismiss()
        );

        btnGuardar.setOnClickListener(v -> {

            String nombre =
                    etNombre.getText()
                            .toString()
                            .trim();

            String ciudad =
                    spCiudad.getSelectedItem()
                            .toString();

            if (nombre.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Introduce un nombre",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            viewModel.insertarColonia(
                    nombre,
                    ciudad
            );

            Toast.makeText(
                    requireContext(),
                    "Colonia creada",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        dialog.show();
    }
}