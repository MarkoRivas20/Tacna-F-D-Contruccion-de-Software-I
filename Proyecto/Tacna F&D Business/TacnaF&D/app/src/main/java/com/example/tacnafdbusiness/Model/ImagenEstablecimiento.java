package com.example.tacnafdbusiness.Model;

public class ImagenEstablecimiento {


    private int ID_Imagen_Establecimiento;
    private int ID_Establecimiento;
    private String Url_Imagen;

    public ImagenEstablecimiento()
    {

    }

    public int getID_Imagen_Establecimiento() {
        return ID_Imagen_Establecimiento;
    }

    public void setID_Imagen_Establecimiento(int ID_Imagen_Establecimiento) {
        this.ID_Imagen_Establecimiento = ID_Imagen_Establecimiento;
    }

    public int getID_Establecimiento() {
        return ID_Establecimiento;
    }

    public void setID_Establecimiento(int ID_Establecimiento) {
        this.ID_Establecimiento = ID_Establecimiento;
    }

    public String getUrl_Imagen() {
        return Url_Imagen;
    }

    public void setUrl_Imagen(String url_Imagen) {
        Url_Imagen = url_Imagen;
    }


}
