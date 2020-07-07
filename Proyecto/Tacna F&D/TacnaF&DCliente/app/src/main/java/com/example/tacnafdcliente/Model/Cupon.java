package com.example.tacnafdcliente.Model;

import java.util.Date;

public class Cupon {

    private int ID_Cupon;
    private int ID_Establecimiento;
    private String Titulo;
    private String Url_Imagen;
    private String Descripcion;
    private Date Fecha_Inicio;
    private Date Fecha_Final;
    private String Estado;

    public Cupon(){

    }
    public int getID_Cupon() {
        return ID_Cupon;
    }

    public void setID_Cupon(int ID_Cupon) {
        this.ID_Cupon = ID_Cupon;
    }

    public int getID_Establecimiento() {
        return ID_Establecimiento;
    }

    public void setID_Establecimiento(int ID_Establecimiento) {
        this.ID_Establecimiento = ID_Establecimiento;
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

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

}
