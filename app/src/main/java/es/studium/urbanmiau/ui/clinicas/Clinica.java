package es.studium.urbanmiau.ui.clinicas;

public class Clinica {
    public int idClinica;
    public String nombre;
    public String direccion;
    public String telefono;
    public String email;
    public String ciudad;


    public Clinica(int idClinica, String nombre, String direccion, String telefono,  String ciudad) {
        this.idClinica = idClinica;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.ciudad = ciudad;
    }
    public int getIdClinica() { return idClinica; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCiudad() { return ciudad; }
    @Override
    public String toString() {
        return nombre + " - " + telefono;
    }
}