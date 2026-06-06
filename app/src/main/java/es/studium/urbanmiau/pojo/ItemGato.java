package es.studium.urbanmiau.pojo;

import androidx.annotation.NonNull;

public class ItemGato {
    private int id;
    private String nombre;

    // Constructor vacío por seguridad
    public ItemGato() {
    }

    // Constructor exacto que usa AltaGato
    public ItemGato(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // 🔥 CRÍTICO: Si este método no existe o no devuelve el nombre,
    // el Spinner se renderiza totalmente en blanco en la pantalla.
    @NonNull
    @Override
    public String toString() {
        return this.nombre != null ? this.nombre : "";
    }
}