package es.studium.urbanmiau.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.urbanmiau.R;
import es.studium.urbanmiau.pojo.Gato;

public class GatoAdapter extends RecyclerView.Adapter<GatoAdapter.ViewHolder> {

    public interface OnGatoClick {
        void onClick(Gato gato);
        void onLongClick(Gato gato);
    }

    private List<Gato> lista;
    private OnGatoClick listener;

    public GatoAdapter(List<Gato> lista, OnGatoClick listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gatos, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Gato g = lista.get(position);

        holder.tvNombre.setText(g.getNombre());
        holder.tvEdad.setText(g.getEdad() + " años");
        holder.tvColonia.setText(g.getNombreColonia());
        holder.tvCuidador.setText(g.getNombreCuidadora()    );
        holder.img.setImageResource(R.drawable.gatoimg);

        holder.itemView.setOnClickListener(v -> listener.onClick(g));

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(g);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvEdad, tvColonia, tvCuidador;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvValorNombre);
            tvEdad = itemView.findViewById(R.id.tvValorEdad);
            tvColonia = itemView.findViewById(R.id.tvValorCiudad);
            tvCuidador = itemView.findViewById(R.id.tvValorClinica);
            img = itemView.findViewById(R.id.imgGatosIcon);
        }
    }
}