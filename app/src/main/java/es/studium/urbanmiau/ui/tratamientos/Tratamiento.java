package es.studium.urbanmiau.ui.tratamientos;

public class Tratamiento {

    private int idTratamiento;
    private String descripcion;

    public Tratamiento(
            int idTratamiento,
            String descripcion
    ) {
        this.idTratamiento = idTratamiento;
        this.descripcion = descripcion;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}