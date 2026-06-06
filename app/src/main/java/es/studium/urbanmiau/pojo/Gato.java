package es.studium.urbanmiau.pojo;

public class Gato {

    private int idGato;
    private String nombre;
    private String edad;

    private String nombreColonia;
    private String nombreCuidadora;

    private String foto;

    public Gato(String nombre, String edad,
                String nombreColonia, String nombreCuidadora,
                String foto) {

        this.idGato = idGato;
        this.nombre = nombre;
        this.edad = edad;
        this.nombreColonia = nombreColonia;
        this.nombreCuidadora = nombreCuidadora;
        this.foto = foto;
    }

    public int getIdGato() { return idGato; }

    public String getNombre() { return nombre; }

    public String getEdad() { return edad; }

    public String getNombreColonia() { return nombreColonia; }

    public String getNombreCuidadora() { return nombreCuidadora; }

    public String getFoto() { return foto; }
}