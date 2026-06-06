package es.studium.urbanmiau.ui.colonias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.urbanmiau.R;

public class ColoniaAdapter extends RecyclerView.Adapter<ColoniaAdapter.ColoniaViewHolder> {

    private List<Colonia> listaColonias;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Colonia colonia);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Colonia colonia);
    }

    public ColoniaAdapter(List<Colonia> listaColonias,
                          OnItemClickListener listener,
                          OnItemLongClickListener longClickListener) {
        this.listaColonias = listaColonias;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ColoniaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_colonia, parent, false);

        return new ColoniaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColoniaViewHolder holder, int position) {

        Colonia colonia = listaColonias.get(position);

        holder.tvNombre.setText(colonia.getNombre());
        holder.tvCuidador.setText(colonia.getCuidador());
        holder.tvCiudad.setText(colonia.getCiudad());
        holder.tvClinica.setText(colonia.getClinica());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(colonia);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(colonia);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaColonias.size();
    }

    public static class ColoniaViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        TextView tvCuidador;
        TextView tvCiudad;
        TextView tvClinica;

        public ColoniaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvValorColonia);
            tvCuidador = itemView.findViewById(R.id.tvValorCuidador);
            tvCiudad = itemView.findViewById(R.id.tvValorCiudad);
            tvClinica = itemView.findViewById(R.id.tvValorClinica);
        }
    }
}