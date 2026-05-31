package es.studium.urbanmiau.ui.clinicas; // Ajusta tu paquete

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.studium.urbanmiau.R;
import es.studium.urbanmiau.ui.clinicas.Clinica;

public class ClinicasFragment extends Fragment {

    private RecyclerView rvClinicas;
    private ClinicaAdapter adapter;
    private List<Clinica> listaClinicas;
    private ClinicasViewModel viewModel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout general donde tienes tu RecyclerView para clínicas
        View vista = inflater.inflate(R.layout.fragment_clinicas, container, false); // Cambia por el nombre real de tu layout

        // 1. Inicializar el RecyclerView
        rvClinicas = vista.findViewById(R.id.rvClinicas); // Asegúrate de que el ID coincida en tu XML
        rvClinicas.setLayoutManager(new LinearLayoutManager(getContext()));

        listaClinicas = new ArrayList<>();
        // Modificamos esta línea para añadir el evento de clic:
        adapter = new ClinicaAdapter(
                listaClinicas,
                clinica -> abrirDetalleClinica(clinica),
                clinica -> mostrarDialogoConfirmacionBorrar(clinica)
        );

        rvClinicas.setAdapter(adapter);

        return vista;
    }

    private void abrirDetalleClinica(Clinica clinica) {
        // 1. Empaquetamos los datos en un Bundle (Se queda igual, solo vigilando tus getters)
        Bundle paqueteDatos = new Bundle();
        paqueteDatos.putInt("idClinica", clinica.getIdClinica());
        paqueteDatos.putString("nombreClinica", clinica.getNombre());
        paqueteDatos.putString("telefonoClinica", clinica.getTelefono());
        paqueteDatos.putString("ciudadClinica", clinica.getCiudad());
        paqueteDatos.putString("direccionClinica", clinica.getDireccion());

        // 2. Usamos el NavController oficial para saltar usando la acción del nav_graph
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.action_nav_clinicas_to_clinicasInfoFragment, paqueteDatos);
    }
    private void mostrarDialogoConfirmacionBorrar(Clinica clinica) {
        // Creamos un cuadro de alerta
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("¿Eliminar clínica?")
                .setMessage("¿Estás seguro de que deseas eliminar la clínica \"" + clinica.getNombre() + "\"? Esta acción borrará el registro de la base de datos de forma permanente.")
                .setIcon(android.R.drawable.ic_dialog_alert) // Icono nativo de advertencia (triángulo exclamación)
                .setPositiveButton("Sí, borrar", (dialog, which) -> {
                    viewModel.eliminarClinica(clinica.getIdClinica());
                    Toast.makeText(getContext(), "Solicitando eliminación...", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Vincular con el ViewModel
        viewModel = new ViewModelProvider(this).get(ClinicasViewModel.class);

        // 3. OBSERVAR los cambios en la lista de clínicas
        viewModel.getListaClinicas().observe(getViewLifecycleOwner(), clinicas -> {
            if (clinicas != null) {
                listaClinicas.clear();
                listaClinicas.addAll(clinicas);
                adapter.notifyDataSetChanged();
            }
        });

        // Observar posibles errores
        viewModel.getError().observe(getViewLifecycleOwner(), mensajeError -> {
            if (mensajeError != null) {
                Toast.makeText(getContext(), mensajeError, Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Mandar a cargar los datos al iniciar
        viewModel.cargarClinicas();
        // 1. Dentro de onViewCreated de tu ClinicasFragment, localizas el botón '+'
        FloatingActionButton fabAddClinica = view.findViewById(R.id.floatingActionButton2); // Ajusta al ID de tu botón +
        fabAddClinica.setOnClickListener(v -> mostrarDialogoAlta());
    }
    private void mostrarDialogoAlta() {
        // Creamos el contenedor del diálogo moderno que emerge desde abajo
        com.google.android.material.bottomsheet.BottomSheetDialog dialog =
                new com.google.android.material.bottomsheet.BottomSheetDialog(requireContext());

        View vistaDialogo = getLayoutInflater().inflate(R.layout.dialog_alta_clinica, null);
        dialog.setContentView(vistaDialogo);

        // Vinculamos los elementos del xml del formulario
        EditText etNombre = vistaDialogo.findViewById(R.id.etAltaNombre);
        EditText etTelefono = vistaDialogo.findViewById(R.id.etAltaTelefono);
        EditText etDireccion = vistaDialogo.findViewById(R.id.etAltaDireccion);
        Spinner spCiudad = vistaDialogo.findViewById(R.id.spAltaCiudad);
        Button btnCancelar = vistaDialogo.findViewById(R.id.btnCancelarAlta);
        Button btnGuardar = vistaDialogo.findViewById(R.id.btnGuardarAlta);

        // Hardcodeadas las ciudades de momento
        String[] ciudadesMock = {"Sevilla", "Dos Hermanas"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, ciudadesMock);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCiudad.setAdapter(adapter);

        // Acción del botón Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        // Acción del botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();

            // Obtenemos el ID simulado de la ciudad según la posición elegida (Sevilla=1, Dos Hermanas=2)
            String ciudadSeleccionada = spCiudad.getSelectedItem().toString();


            // Validación simple de campos vacíos
            if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "Por favor, rellena todos los campos", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                // Enviamos los datos al ViewModel para que ejecute el alta en la base de datos
//  CORRECTO: Llama a la instancia de la clase que está activa
                viewModel.insertarClinica(nombre, direccion, telefono, ciudadSeleccionada);
                android.widget.Toast.makeText(requireContext(), "Clínica guardada con éxito", android.widget.Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Cerramos el formulario
            }
        });

        dialog.show();
    }
}