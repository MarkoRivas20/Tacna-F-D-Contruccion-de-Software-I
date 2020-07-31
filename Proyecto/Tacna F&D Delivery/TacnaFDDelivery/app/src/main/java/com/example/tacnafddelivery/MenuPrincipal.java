package com.example.tacnafddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MenuPrincipal extends AppCompatActivity {

    ListaEstablecimiento Fragment_Lista_Establecimiento;
    SeguimientoPedido Fragment_Seguimiento_Pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().hide();

        String Seguimiento = GetSeguimientoFromSharedPreferences("Seguimiento");

        if(Seguimiento == null || Seguimiento.equals(""))
        {
            Fragment_Lista_Establecimiento = new ListaEstablecimiento();

            getSupportFragmentManager().beginTransaction().add(R.id.contenedorfragment,Fragment_Lista_Establecimiento).commit();
        }
        else
        {
            Fragment_Seguimiento_Pedido = new SeguimientoPedido();

            getSupportFragmentManager().beginTransaction().add(R.id.contenedorfragment,Fragment_Seguimiento_Pedido).commit();
        }

    }

    private String GetSeguimientoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("info_estado_pedido", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
