package com.example.tacnafdcliente.Model;

public class ItemMenu {


    private int ID_Establecimiento;
    private String Nombre;
    private String Descripcion;
    private Double Precio;
    private String Url_Imagen;

    public ItemMenu(){

    }

    private int ID_Item_Menu;

    public int getID_Item_Menu() {
        return ID_Item_Menu;
    }

    public void setID_Item_Menu(int ID_Item_Menu) {
        this.ID_Item_Menu = ID_Item_Menu;
    }

    public int getID_Establecimiento() {
        return ID_Establecimiento;
    }

    public void setID_Establecimiento(int ID_Establecimiento) {
        this.ID_Establecimiento = ID_Establecimiento;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }

    public String getUrl_Imagen() {
        return Url_Imagen;
    }

    public void setUrl_Imagen(String url_Imagen) {
        Url_Imagen = url_Imagen;
    }


}
