package es.studium.urbanmiau.ui.clinicas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.studium.urbanmiau.R;
import java.util.List;

public class ClinicaAdapter extends RecyclerView.Adapter<ClinicaAdapter.ClinicaViewHolder> {

    private List<Clinica> listaClinicas;
    private OnItemClickListener listener;

    private OnItemLongClickListener longClickListener;

    // Interfaz para comunicar el clic al Fragment
    public interface OnItemClickListener {
        void onItemClick(Clinica clinica);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Clinica clinica);
    }

    // 2. Modificar el constructor para recibir el listener
    public ClinicaAdapter(List<Clinica> listaClinicas, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.listaClinicas = listaClinicas;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ClinicaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinicas, parent, false);
        return new ClinicaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicaViewHolder holder, int position) {
        Clinica clinica = listaClinicas.get(position);

        holder.tvNombre.setText(clinica.getNombre());
        holder.tvTelefono.setText(clinica.getTelefono());
        holder.tvCiudad.setText(clinica.getCiudad());
        holder.tvDireccion.setText(clinica.getDireccion());

        //  Clic normal
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(clinica);
            }
        });

        // Click sostenido
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(clinica);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaClinicas.size();
    }

    public static class ClinicaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvCiudad, tvDireccion;

        public ClinicaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvValorColonia);
            tvTelefono = itemView.findViewById(R.id.tvValorCuidador);
            tvCiudad = itemView.findViewById(R.id.tvValorCiudad);
            tvDireccion = itemView.findViewById(R.id.tvValorClinica);
        }
    }
}