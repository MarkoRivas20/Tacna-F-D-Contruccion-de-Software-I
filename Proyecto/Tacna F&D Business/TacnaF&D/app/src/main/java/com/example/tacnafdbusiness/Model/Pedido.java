package com.example.tacnafdbusiness.Model;

public class Pedido {

    private int ID_Pedido;
    private String Usuario_Cliente;
    private int ID_Establecimiento;
    private String Usuario_Repartidor;
    private String Descripcion;
    private String Fecha;
    private String Estado;
    private Double Precio_Total;
    private String Direccion_Destino;
    private String PuntoGeografico_Destino;

    public Pedido(){

    }

    public int getID_Pedido() {
        return ID_Pedido;
    }

    public void setID_Pedido(int ID_Pedido) {
        this.ID_Pedido = ID_Pedido;
    }

    public String getUsuario_Cliente() {
        return Usuario_Cliente;
    }

    public void setUsuario_Cliente(String Usuario_Cliente) {
        this.Usuario_Cliente = Usuario_Cliente;
    }

    public int getID_Establecimiento() {
        return ID_Establecimiento;
    }

    public void setID_Establecimiento(int ID_Establecimiento) {
        this.ID_Establecimiento = ID_Establecimiento;
    }

    public String getUsuario_Repartidor() {
        return Usuario_Repartidor;
    }

    public void setUsuario_Repartidor(String Usuario_Repartidor) {
        this.Usuario_Repartidor = Usuario_Repartidor;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public Double getPrecio_Total() {
        return Precio_Total;
    }

    public void setPrecio_Total(Double precio_Total) {
        Precio_Total = precio_Total;
    }

    public String getDireccion_Destino() {
        return Direccion_Destino;
    }

    public void setDireccion_Destino(String direccion_Destino) {
        Direccion_Destino = direccion_Destino;
    }

    public String getPuntoGeografico_Destino() {
        return PuntoGeografico_Destino;
    }

    public void setPuntoGeografico_Destino(String puntoGeografico_Destino) {
        PuntoGeografico_Destino = puntoGeografico_Destino;
    }


}
