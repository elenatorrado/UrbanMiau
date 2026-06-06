package es.studium.urbanmiau.ui.tratamientos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class TratamientosFragment extends Fragment {

    private RecyclerView rvTratamientos;
    private TratamientoAdapter adapter;
    private List<Tratamiento> listaTratamientos;
    private TratamientosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View vista = inflater.inflate(
                R.layout.fragment_tratamientos,
                container,
                false
        );

        rvTratamientos =
                vista.findViewById(
                        R.id.rvTratamientos
                );

        rvTratamientos.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        listaTratamientos = new ArrayList<>();

        adapter = new TratamientoAdapter(
                listaTratamientos,
                tratamiento -> abrirDetalleTratamiento(tratamiento),
                tratamiento -> mostrarDialogoConfirmacionBorrar(tratamiento)
        );

        rvTratamientos.setAdapter(adapter);

        return vista;
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(view, savedInstanceState);

        viewModel =
                new ViewModelProvider(this)
                        .get(TratamientosViewModel.class);

        viewModel.getListaTratamientos()
                .observe(getViewLifecycleOwner(), tratamientos -> {

                    listaTratamientos.clear();
                    listaTratamientos.addAll(tratamientos);

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

        viewModel.cargarTratamientos();

        FloatingActionButton fab =
                view.findViewById(
                        R.id.floatingActionButtonTratamiento
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
                        R.layout.dialog_alta_tratamiento,
                        null
                );

        dialog.setContentView(vistaDialogo);

        EditText etDescripcion =
                vistaDialogo.findViewById(
                        R.id.etDescripcionTratamiento
                );

        Button btnCancelar =
                vistaDialogo.findViewById(
                        R.id.btnCancelarTratamiento
                );

        Button btnGuardar =
                vistaDialogo.findViewById(
                        R.id.btnGuardarTratamiento
                );

        btnCancelar.setOnClickListener(
                v -> dialog.dismiss()
        );

        btnGuardar.setOnClickListener(v -> {

            String descripcion =
                    etDescripcion.getText()
                            .toString()
                            .trim();

            if (descripcion.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Introduce una descripción",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            viewModel.insertarTratamiento(
                    descripcion
            );

            Toast.makeText(
                    requireContext(),
                    "Tratamiento creado",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void abrirDetalleTratamiento(
            Tratamiento tratamiento
    ) {

        Bundle datos = new Bundle();

        datos.putInt(
                "idTratamiento",
                tratamiento.getIdTratamiento()
        );

        datos.putString(
                "descripcionTratamiento",
                tratamiento.getDescripcion()
        );

        Navigation.findNavController(requireView())
                .navigate(
                        R.id.action_nav_tratamientos_to_tratamientoInfoFragment,
                        datos
                );
    }

    private void mostrarDialogoConfirmacionBorrar(
            Tratamiento tratamiento
    ) {

        new AlertDialog.Builder(requireContext())
                .setTitle("¿Eliminar tratamiento?")
                .setMessage(
                        tratamiento.getDescripcion()
                )
                .setPositiveButton(
                        "Sí",
                        (dialog, which) -> {

                            viewModel.eliminarTratamiento(
                                    tratamiento.getIdTratamiento()
                            );

                            Toast.makeText(
                                    getContext(),
                                    "Tratamiento eliminado",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .setNegativeButton(
                        "Cancelar",
                        null
                )
                .show();
    }
}