package com.example.tacnafdcliente.Model;

import java.util.Date;

public class MiCupon {


    private int ID_Cupon_Usuario;
    private int ID_Cupon;
    private int ID_Usuario_Cliente;
    private String Nombre;
    private Date Fecha;
    private String Estado;
    private String Titulo;
    private String Url_Imagen;
    private String Descripcion;
    private Date Fecha_Inicio;
    private Date Fecha_Final;

    public MiCupon(){

    }

    public int getID_Cupon_Usuario() {
        return ID_Cupon_Usuario;
    }

    public void setID_Cupon_Usuario(int ID_Cupon_Usuario) {
        this.ID_Cupon_Usuario = ID_Cupon_Usuario;
    }

    public int getID_Cupon() {
        return ID_Cupon;
    }

    public void setID_Cupon(int ID_Cupon) {
        this.ID_Cupon = ID_Cupon;
    }

    public int getID_Usuario_Cliente() {
        return ID_Usuario_Cliente;
    }

    public void setID_Usuario_Cliente(int ID_Usuario_Cliente) {
        this.ID_Usuario_Cliente = ID_Usuario_Cliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getUrl_Imagen() {
        return Url_Imagen;
    }

    public void setUrl_Imagen(String url_Imagen) {
        Url_Imagen = url_Imagen;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Date getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(Date fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public Date getFecha_Final() {
        return Fecha_Final;
    }

    public void setFecha_Final(Date fecha_Final) {
        Fecha_Final = fecha_Final;
    }


}
