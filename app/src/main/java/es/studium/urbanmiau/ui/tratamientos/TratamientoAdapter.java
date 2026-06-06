package es.studium.urbanmiau.ui.tratamientos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.urbanmiau.R;

public class TratamientoAdapter
        extends RecyclerView.Adapter<TratamientoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Tratamiento tratamiento);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Tratamiento tratamiento);
    }

    private final List<Tratamiento> lista;
    private final OnItemClickListener clickListener;
    private final OnItemLongClickListener longClickListener;

    public TratamientoAdapter(
            List<Tratamiento> lista,
            OnItemClickListener clickListener,
            OnItemLongClickListener longClickListener
    ) {
        this.lista = lista;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.item_tratamiento,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Tratamiento tratamiento = lista.get(position);

        holder.tvDescripcion.setText(
                tratamiento.getDescripcion()
        );

        holder.itemView.setOnClickListener(v ->
                clickListener.onItemClick(tratamiento));

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(tratamiento);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescripcion =
                    itemView.findViewById(
                            R.id.tvDescripcionTratamiento
                    );
        }
    }
}