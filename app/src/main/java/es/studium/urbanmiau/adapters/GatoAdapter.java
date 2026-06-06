package es.studium.urbanmiau.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import es.studium.urbanmiau.R;
import es.studium.urbanmiau.pojo.Gato;

public class GatoAdapter extends RecyclerView.Adapter<GatoAdapter.GatoViewHolder> {

    private final List<Gato> listaGatos;
    private final OnGatoClick listener;

    // Interfaz para gestionar las pulsaciones normales y largas desde el Fragment
    public interface OnGatoClick {
        void onClick(Gato g);
        void onLongClick(Gato g);
    }

    // Constructor del Adaptador
    public GatoAdapter(List<Gato> listaGatos, OnGatoClick listener) {
        this.listaGatos = listaGatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 🔍 Vinculado correctamente a tu archivo real: item_gatos.xml
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gatos, parent, false);
        return new GatoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull GatoViewHolder holder, int position) {
        Gato gatoActual = listaGatos.get(position);

        // 1. Asignar los campos de texto usando tus IDs del XML y los métodos de tu POJO Gato
        holder.tvNombre.setText(gatoActual.getNombre() != null ? gatoActual.getNombre() : "Sin nombre");

        // Controlamos el formato de la edad de forma limpia
        String edadTxt = gatoActual.getEdad();
        if (edadTxt != null && !edadTxt.trim().isEmpty()) {
            holder.tvEdad.setText(edadTxt.contains("año") || edadTxt.contains("mes") ? edadTxt : edadTxt + " años");
        } else {
            holder.tvEdad.setText("Edad desconocida");
        }

        // Métodos correctos de tu POJO vinculados a tus TextViews del XML
        holder.tvColonia.setText(gatoActual.getNombreColonia() != null ? gatoActual.getNombreColonia() : "Sin colonia");
        holder.tvCuidadora.setText(gatoActual.getNombreCuidadora() != null ? gatoActual.getNombreCuidadora() : "Sin cuidador/a");

        // 2. 🛡️ ESCUDO ANTICRASH DE GLIDE: Filtramos el "null" para que no congele la app
        String pathFoto = gatoActual.getFoto();

        if (pathFoto == null || pathFoto.trim().isEmpty() || pathFoto.equalsIgnoreCase("null") || pathFoto.endsWith("/null")) {
            // Si no hay foto en BD, cargamos el recurso local directamente sin peticiones de red
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.gatoimg)
                    .into(holder.imgGato);
        } else {
            // Si la URL es válida, Glide la carga de tu servidor
            Glide.with(holder.itemView.getContext())
                    .load(pathFoto)
                    .placeholder(R.drawable.gatoimg) // Imagen temporal de carga
                    .error(R.drawable.gatoimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)// Imagen de rescate si falla el servidor
                    .into(holder.imgGato);
        }

        // 3. Configurar los Listeners de pulsación para interactuar con los Diálogos
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(gatoActual);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onLongClick(gatoActual);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaGatos != null ? listaGatos.size() : 0;
    }

    // ==========================================
    // HOLDER MAPEADO CON TUS IDs DEL XML REAL
    // ==========================================
    public static class GatoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvEdad, tvColonia, tvCuidadora;
        ImageView imgGato;

        public GatoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Enlazamos milimétricamente con los IDs de tu layout item_gatos.xml
            tvNombre = itemView.findViewById(R.id.tvValorNombre);
            tvEdad = itemView.findViewById(R.id.tvValorEdad);
            tvColonia = itemView.findViewById(R.id.tvValorCiudad);    // ID usado para la colonia
            tvCuidadora = itemView.findViewById(R.id.tvValorClinica); // ID usado para el cuidador

            // Enlazamos el ImageView del lateral derecho
            imgGato = itemView.findViewById(R.id.imgGatosIcon);
        }
    }
}