package com.example.tacnafdcliente.Model;

public class Establecimiento {


    private int ID_Establecimiento;
    private int ID_Usuario_Propietario;
    private String Nombre;
    private String Distrito;
    private String Categoria;
    private String Direccion;
    private String Telefono;
    private String Descripcion;
    private int Capacidad;
    private int TotalResenas;
    private Double Puntuacion;
    private String Url_Imagen_Logo;
    private String Url_Imagen_Documento;
    private String PuntoGeografico;
    private String Estado;

    public Establecimiento() {

    }

    public int getID_Establecimiento() {
        return ID_Establecimiento;
    }

    public void setID_Establecimiento(int ID_Establecimiento) {
        this.ID_Establecimiento = ID_Establecimiento;
    }

    public int getID_Usuario_Propietario() {
        return ID_Usuario_Propietario;
    }

    public void setID_Usuario_Propietario(int ID_Usuario_Propietario) {
        this.ID_Usuario_Propietario = ID_Usuario_Propietario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDistrito() {
        return Distrito;
    }

    public void setDistrito(String distrito) {
        Distrito = distrito;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getCapacidad() {
        return Capacidad;
    }

    public void setCapacidad(int capacidad) {
        Capacidad = capacidad;
    }

    public int getTotalResenas() {
        return TotalResenas;
    }

    public void setTotalResenas(int totalResenas) {
        TotalResenas = totalResenas;
    }

    public Double getPuntuacion() {
        return Puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        Puntuacion = puntuacion;
    }

    public String getUrl_Imagen_Logo() {
        return Url_Imagen_Logo;
    }

    public void setUrl_Imagen_Logo(String url_Imagen_Logo) {
        Url_Imagen_Logo = url_Imagen_Logo;
    }

    public String getUrl_Imagen_Documento() {
        return Url_Imagen_Documento;
    }

    public void setUrl_Imagen_Documento(String url_Imagen_Documento) {
        Url_Imagen_Documento = url_Imagen_Documento;
    }

    public String getPuntoGeografico() {
        return PuntoGeografico;
    }

    public void setPuntoGeografico(String puntoGeografico) {
        PuntoGeografico = puntoGeografico;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
