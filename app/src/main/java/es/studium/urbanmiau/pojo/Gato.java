package es.studium.urbanmiau.pojo;

public class Gato {

    private int idGato;
    private String nombre;
    private String edad;
    private String sexo;
    private String nombreColonia;
    private String nombreCuidadora;
    private String nombreClinica;
    private String foto;

    // Constructor completo adaptado
    public Gato(int idGato,
                String nombre,
                String edad,
                String sexo,
                String nombreColonia,
                String nombreCuidadora,
                String nombreClinica,
                String foto) {
        this.idGato = idGato;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.nombreColonia = nombreColonia;
        this.nombreCuidadora = nombreCuidadora;
        this.nombreClinica = nombreClinica;
        this.foto = foto;
    }

    // Constructor vacío (esencial para la instanciación dinámica desde JSON)
    public Gato() {
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public int getIdGato() {
        return idGato;
    }

    public void setIdGato(int idGato) {
        this.idGato = idGato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombreColonia() {
        return nombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }

    public String getNombreCuidadora() {
        return nombreCuidadora;
    }

    public void setNombreCuidadora(String nombreCuidadora) {
        this.nombreCuidadora = nombreCuidadora;
    }


    public String getNombreClinica() {
        return nombreClinica;
    }


    public void setNombreClinica(String nombreClinica) {
        this.nombreClinica = nombreClinica;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}