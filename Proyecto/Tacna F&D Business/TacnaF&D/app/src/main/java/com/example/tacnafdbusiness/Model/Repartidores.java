package com.example.tacnafdbusiness.Model;

public class Repartidores {


    private int ID_Repartidor_Establecimiento;
    private int ID_Usuario_Repartidor;
    private String Email;
    private String Contrasena;
    private String Nombre;
    private String Apellido;
    private String Url_Foto;

    public Repartidores(){

    }

    public int getID_Repartidor_Establecimiento() {
        return ID_Repartidor_Establecimiento;
    }

    public void setID_Repartidor_Establecimiento(int ID_Repartidor_Establecimiento) {
        this.ID_Repartidor_Establecimiento = ID_Repartidor_Establecimiento;
    }


    public int getID_Usuario_Repartidor() {
        return ID_Usuario_Repartidor;
    }

    public void setID_Usuario_Repartidor(int ID_Usuario_Repartidor) {
        this.ID_Usuario_Repartidor = ID_Usuario_Repartidor;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getUrl_Foto() {
        return Url_Foto;
    }

    public void setUrl_Foto(String url_Foto) {
        Url_Foto = url_Foto;
    }


}
