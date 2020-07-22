package com.example.tacnafdcliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class BuscarEstablecimiento extends Fragment {

    public BuscarEstablecimiento() {
        // Required empty public constructor
    }

    Button Btnperfil;
    Button Btnprincipal;
    Button Btnbuscar;

    Spinner Spinner_Categoria;
    Spinner Spinner_Distrito;

    EditText Txtnombre;
    EditText Txtcapacidad;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_buscar_establecimiento, container, false);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        return true;
                    }
                    else
                    {

                    }
                }
                else
                {

                }
                return false;
            }
        });

        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtcapacidad = (EditText) v.findViewById(R.id.txtcapacidad);

        Btnperfil = (Button) v.findViewById(R.id.btnperfil);
        Btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilUsuario perfilUsuario = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilUsuario);
                transaction.commit();
            }
        });

        Btnprincipal = (Button) v.findViewById(R.id.btnprincipal);
        Btnprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
                transaction.commit();
            }
        });

        String[] categorias = {"Seleccione una Categoria","Restaurante","Cafeteria","Bar"};
        Spinner_Categoria = (Spinner) v.findViewById(R.id.spinnercategoria);

        Spinner_Categoria.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorias));

        String[] distritos = {"Seleccione un Distrito","Tacna","Alto del Alianza","Calana","Pachia","Palca","Pocollay","Ciudad Nueva"};
        Spinner_Distrito = (Spinner) v.findViewById(R.id.spinnerdistrito);
        Spinner_Distrito.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, distritos));


        Btnbuscar = (Button) v.findViewById(R.id.btnbuscar);
        Btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveSearchSharedPreferences(Txtnombre.getText().toString(), Spinner_Distrito.getSelectedItem().toString(),
                        Spinner_Categoria.getSelectedItem().toString(), Txtcapacidad.getText().toString());

                ListaEstablecimiento listaEstablecimiento = new ListaEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaEstablecimiento);
                transaction.commit();
            }
        });

        return v;
    }

    private void SaveSearchSharedPreferences(String Nombre, String Distrito, String Categoria, String Capacidad){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_search", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Nombre", Nombre);
        editor.putString("Distrito", Distrito);
        editor.putString("Categoria", Categoria);
        editor.putString("Capacidad", Capacidad);
        editor.apply();
    }
}
