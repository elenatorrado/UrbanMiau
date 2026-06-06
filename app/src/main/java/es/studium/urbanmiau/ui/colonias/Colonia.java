package es.studium.urbanmiau.ui.colonias;

public class Colonia {

    private int idColonia;
    private String nombre;
    private String cuidador;
    private String ciudad;
    private String clinica;

    public Colonia(int idColonia,
                   String nombre,
                   String cuidador,
                   String ciudad,
                   String clinica) {

        this.idColonia = idColonia;
        this.nombre = nombre;
        this.cuidador = cuidador;
        this.ciudad = ciudad;
        this.clinica = clinica;
    }

    public int getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(int idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuidador() {
        return cuidador;
    }

    public void setCuidador(String cuidador) {
        this.cuidador = cuidador;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getClinica() {
        return clinica;
    }

    public void setClinica(String clinica) {
        this.clinica = clinica;
    }
}