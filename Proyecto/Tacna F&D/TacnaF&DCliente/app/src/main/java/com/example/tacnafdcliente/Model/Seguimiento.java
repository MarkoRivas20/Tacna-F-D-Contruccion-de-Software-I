package com.example.tacnafdcliente.Model;

public class Seguimiento {
    private int ID_Pedido;
    private String PuntoGeografico;

    public Seguimiento(){

    }

    public int getID_Pedido() {
        return ID_Pedido;
    }

    public void setID_Pedido(int ID_Pedido) {
        this.ID_Pedido = ID_Pedido;
    }

    public String getPuntoGeografico() {
        return PuntoGeografico;
    }

    public void setPuntoGeografico(String puntoGeografico) {
        PuntoGeografico = puntoGeografico;
    }
}
