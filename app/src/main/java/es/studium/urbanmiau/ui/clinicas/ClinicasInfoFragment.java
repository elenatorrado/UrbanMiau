package es.studium.urbanmiau.ui.clinicas;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton; // 🔴 Nuevo

import es.studium.urbanmiau.R;

public class ClinicasInfoFragment extends Fragment {

    // Declaramos las variables donde guardaremos los datos recibidos
    private int idClinica;
    private String nombre, telefono, ciudad, direccion;

    // Elementos visuales de la pantalla de información
    private TextView tvInfoNombre, tvInfoTelefono, tvInfoCiudad, tvInfoDireccion;

    //  Botón flotante para editar y el ViewModel
    private FloatingActionButton btnEditarClinica;
    private ClinicasViewModel viewModel;

    public ClinicasInfoFragment() {
        // Constructor público vacío
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Rescatamos los datos que viajan en el Bundle desde el ClinicasFragment
        if (getArguments() != null) {
            idClinica = getArguments().getInt("idClinica");
            nombre = getArguments().getString("nombreClinica");
            telefono = getArguments().getString("telefonoClinica");
            ciudad = getArguments().getString("ciudadClinica");
            direccion = getArguments().getString("direccionClinica");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos tu layout de detalle (asegúrate de que el nombre del archivo XML sea el correcto)
        return inflater.inflate(R.layout.activity_clinicas_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Vinculamos los componentes visuales con sus IDs
        tvInfoNombre = view.findViewById(R.id.tvInfoNombre);
        tvInfoTelefono = view.findViewById(R.id.tvInfoTelefono);
        tvInfoCiudad = view.findViewById(R.id.tvInfoCiudad);
        tvInfoDireccion = view.findViewById(R.id.tvInfoDireccion);

        //  Vincular el nuevo botón flotante de edición
        btnEditarClinica = view.findViewById(R.id.btnEditarClinica);

        //  Inicializamos el ViewModel asociado a la actividad para comunicarnos con la API
        viewModel = new ViewModelProvider(requireActivity()).get(ClinicasViewModel.class);

        //  Pintamos los datos recibidos en la pantalla
        if (nombre != null) {
            tvInfoNombre.setText(nombre);
            tvInfoTelefono.setText(telefono);
            tvInfoCiudad.setText(ciudad);
            tvInfoDireccion.setText(direccion);
        }

        // 🔴 3. Lógica para el botón Editar
        btnEditarClinica.setOnClickListener(v -> mostrarDialogoEdicion(view));
    }

    //  Despliega el menú inferior con los datos cargados para editar
    private void mostrarDialogoEdicion(View vistaFragmento) {
        com.google.android.material.bottomsheet.BottomSheetDialog dialog =
                new com.google.android.material.bottomsheet.BottomSheetDialog(requireContext());

        View vistaDialogo = getLayoutInflater().inflate(R.layout.dialog_alta_clinica, null);
        dialog.setContentView(vistaDialogo);

        // Vinculamos los elementos del formulario
        EditText etNombre = vistaDialogo.findViewById(R.id.etAltaNombre);
        EditText etTelefono = vistaDialogo.findViewById(R.id.etAltaTelefono);
        EditText etDireccion = vistaDialogo.findViewById(R.id.etAltaDireccion);
        Spinner spCiudad = vistaDialogo.findViewById(R.id.spAltaCiudad); // 🟢 Tu spinner
        Button btnCancelar = vistaDialogo.findViewById(R.id.btnCancelarAlta);
        Button btnGuardar = vistaDialogo.findViewById(R.id.btnGuardarAlta);

        // Autocompletamos los textos
        etNombre.setText(nombre);
        etTelefono.setText(telefono);
        etDireccion.setText(direccion);

        // 🔴 1. INYECTAR LAS CIUDADES al Spinner (Igual que en el alta)
        String[] ciudadesMock = {"Sevilla", "Dos Hermanas"};

        // Usamos el layout nativo simple_spinner_item
        android.widget.ArrayAdapter<String> adapterCiudades = new android.widget.ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, ciudadesMock);

        adapterCiudades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCiudad.setAdapter(adapterCiudades);

        // 🔴 2. SELECCIONAR LA CIUDAD ACTUAL por defecto
        // Buscamos en qué posición del array está la ciudad actual de esta clínica
        if (ciudad != null) {
            for (int i = 0; i < ciudadesMock.length; i++) {
                if (ciudadesMock[i].equalsIgnoreCase(ciudad)) {
                    spCiudad.setSelection(i); // Deja marcado "Sevilla" o "Dos Hermanas" según corresponda
                    break;
                }
            }
        }

        // Acción del botón Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        // Acción del botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString().trim();
            String nuevoTelefono = etTelefono.getText().toString().trim();
            String nuevaDireccion = etDireccion.getText().toString().trim();

            // 🔴 3. CAPTURAR LA NUEVA CIUDAD elegida en el Spinner
            String ciudadSeleccionada = spCiudad.getSelectedItem().toString();

            if (nuevoNombre.isEmpty() || nuevoTelefono.isEmpty() || nuevaDireccion.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Enviamos los datos actualizados incluyendo la ciudad elegida
                viewModel.actualizarClinica(idClinica, nuevoNombre, nuevaDireccion, nuevoTelefono, ciudadSeleccionada);

                Toast.makeText(requireContext(), "Clínica actualizada con éxito", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                androidx.navigation.Navigation.findNavController(vistaFragmento).navigateUp();
            }
        });

        dialog.show();
    }
}