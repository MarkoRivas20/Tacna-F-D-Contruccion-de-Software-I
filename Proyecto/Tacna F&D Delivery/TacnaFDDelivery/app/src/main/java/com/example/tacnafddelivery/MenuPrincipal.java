package com.example.tacnafddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuPrincipal extends AppCompatActivity {

    ListaEstablecimiento Fragment_Lista_Establecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().hide();

        Fragment_Lista_Establecimiento = new ListaEstablecimiento();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorfragment,Fragment_Lista_Establecimiento).commit();
    }
}
