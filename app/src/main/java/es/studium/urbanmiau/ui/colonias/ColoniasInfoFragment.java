package es.studium.urbanmiau.ui.colonias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.studium.urbanmiau.R;

public class ColoniasInfoFragment extends Fragment {

    private TextView textNombreGato;
    private TextView valorColonia;
    private TextView valorClinica;
    private TextView textUbicacion;

    private int idColonia;
    private String nombre;
    private String ciudad;

    private ColoniasViewModel viewModel;

    public ColoniasInfoFragment() {
        // Constructor vacío obligatorio
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(
                R.layout.activity_coloniasinfo,
                container,
                false
        );

        textNombreGato = vista.findViewById(R.id.textNombreGato);
        valorColonia = vista.findViewById(R.id.valorColonia);
        valorClinica = vista.findViewById(R.id.valorClinica);
        textUbicacion = vista.findViewById(R.id.textUbicacion);

        viewModel = new ViewModelProvider(requireActivity())
                .get(ColoniasViewModel.class);

        Bundle datos = getArguments();

        if (datos != null) {

            idColonia = datos.getInt("idColonia");

            nombre = datos.getString(
                    "nombreColonia",
                    "Sin nombre"
            );

            ciudad = datos.getString(
                    "ciudadColonia",
                    "Sin ciudad"
            );

            String clinica = datos.getString(
                    "clinicaColonia",
                    "Sin clínica"
            );

            textNombreGato.setText(nombre);
            valorColonia.setText(nombre);
            valorClinica.setText(clinica);
            textUbicacion.setText(ciudad);
        }

        FloatingActionButton btnEditar =
                vista.findViewById(R.id.btnEditarColonia);

        btnEditar.setOnClickListener(v ->
                mostrarDialogoEdicion(vista)
        );

        return vista;
    }

    private void mostrarDialogoEdicion(View vistaFragmento) {

        com.google.android.material.bottomsheet.BottomSheetDialog dialog =
                new com.google.android.material.bottomsheet.BottomSheetDialog(
                        requireContext()
                );

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

        etNombre.setText(nombre);

        String[] ciudades = {
                "Madrid",
                "Toledo",
                "Sevilla",
                "Barcelona"
        };

        ArrayAdapter<String> adapterCiudades =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        ciudades
                );

        adapterCiudades.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCiudad.setAdapter(adapterCiudades);

        for (int i = 0; i < ciudades.length; i++) {

            if (ciudades[i].equalsIgnoreCase(ciudad)) {

                spCiudad.setSelection(i);
                break;
            }
        }

        btnCancelar.setOnClickListener(
                v -> dialog.dismiss()
        );

        btnGuardar.setOnClickListener(v -> {

            String nuevoNombre =
                    etNombre.getText()
                            .toString()
                            .trim();

            String nuevaCiudad =
                    spCiudad.getSelectedItem()
                            .toString();

            if (nuevoNombre.isEmpty()) {

                Toast.makeText(
                        requireContext(),
                        "Introduce un nombre",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            viewModel.actualizarColonia(
                    idColonia,
                    nuevoNombre,
                    nuevaCiudad
            );

            Toast.makeText(
                    requireContext(),
                    "Colonia actualizada",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();

            Navigation.findNavController(vistaFragmento)
                    .navigateUp();
        });

        dialog.show();
    }
}