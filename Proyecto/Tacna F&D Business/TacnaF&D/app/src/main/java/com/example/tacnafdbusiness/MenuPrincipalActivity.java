package com.example.tacnafdbusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuPrincipalActivity extends AppCompatActivity {

    ListaEstablecimientos Fragment_Establecimiento;
    PerfilUsuario Fragment_Perfil;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().hide();

        Fragment_Establecimiento = new ListaEstablecimientos();
        Fragment_Perfil = new PerfilUsuario();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorfragment,Fragment_Establecimiento).commit();
    }
}
