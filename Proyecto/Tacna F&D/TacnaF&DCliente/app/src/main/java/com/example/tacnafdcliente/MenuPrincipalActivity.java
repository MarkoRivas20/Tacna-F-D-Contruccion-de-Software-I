package com.example.tacnafdcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuPrincipalActivity extends AppCompatActivity {

    PantallaPrincipal Fragment_Pantalla_Principal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().hide();

        Fragment_Pantalla_Principal = new PantallaPrincipal();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorfragment,Fragment_Pantalla_Principal).commit();
    }
}
