package es.studium.urbanmiau.ui.tratamientos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.studium.urbanmiau.R;

public class TratamientoInfoFragment extends Fragment {

    private int idTratamiento;
    private String descripcion;

    private TextView tvDescripcion;

    private FloatingActionButton btnEditar;

    private TratamientosViewModel viewModel;

    public TratamientoInfoFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.activity_tratamiento_info,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        tvDescripcion =
                view.findViewById(
                        R.id.tvInfoDescripcionTratamiento
                );

        btnEditar =
                view.findViewById(
                        R.id.btnEditarTratamiento
                );

        viewModel =
                new ViewModelProvider(requireActivity())
                        .get(TratamientosViewModel.class);

        if (getArguments() != null) {

            idTratamiento =
                    getArguments().getInt(
                            "idTratamiento"
                    );

            descripcion =
                    getArguments().getString(
                            "descripcionTratamiento"
                    );

            tvDescripcion.setText(
                    descripcion
            );
        }

        btnEditar.setOnClickListener(v ->
                mostrarDialogoEdicion(view)
        );
    }

    private void mostrarDialogoEdicion(
            View vistaFragmento
    ) {

        BottomSheetDialog dialog =
                new BottomSheetDialog(
                        requireContext()
                );

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

        etDescripcion.setText(
                descripcion
        );

        btnCancelar.setOnClickListener(
                v -> dialog.dismiss()
        );

        btnGuardar.setOnClickListener(v -> {

            String nuevaDescripcion =
                    etDescripcion.getText()
                            .toString()
                            .trim();

            if (nuevaDescripcion.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Introduce una descripción",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            viewModel.actualizarTratamiento(
                    idTratamiento,
                    nuevaDescripcion
            );

            Toast.makeText(
                    requireContext(),
                    "Tratamiento actualizado",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();

            Navigation.findNavController(
                    vistaFragmento
            ).navigateUp();
        });

        dialog.show();
    }
}