package com.example.tacnafdbusiness.Model;

import java.util.Date;

public class Reseñas {


    private int ID_Resena;
    private String ID_Usuario_Cliente;
    private int ID_Establecimiento;
    private String Descripcion;
    private Double Calificacion;
    private Date Fecha;


    public Reseñas(){

    }

    public int getID_Resena() {
        return ID_Resena;
    }

    public void setID_Resena(int ID_Resena) {
        this.ID_Resena = ID_Resena;
    }

    public String getID_Usuario_Cliente() {
        return ID_Usuario_Cliente;
    }

    public void setID_Usuario_Cliente(String ID_Usuario_Cliente) {
        this.ID_Usuario_Cliente = ID_Usuario_Cliente;
    }

    public int getID_Establecimiento() {
        return ID_Establecimiento;
    }

    public void setID_Establecimiento(int ID_Establecimiento) {
        this.ID_Establecimiento = ID_Establecimiento;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Double getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(Double calificacion) {
        Calificacion = calificacion;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }


}
