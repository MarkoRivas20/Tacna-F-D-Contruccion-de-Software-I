package com.example.tacnafddelivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ListaPedidos extends Fragment {

    public ListaPedidos() {
        // Required empty public constructor
    }

    Button Btnperfil;
    Button Btnatras;

    TextView Txtnombre_establecimiento;
    TextView Txtdireccion_establecimiento;
    ImageView Imgfoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_pedidos, container, false);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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

        Txtnombre_establecimiento = (TextView) v.findViewById(R.id.txtnombre_establecimiento);
        Txtnombre_establecimiento.setText(GetInfoFromSharedPreferences("nombre_establecimiento"));

        Txtdireccion_establecimiento = (TextView) v.findViewById(R.id.txtdireccion_establecimiento);
        Txtdireccion_establecimiento.setText(GetInfoFromSharedPreferences("direccion_establecimiento"));

        Imgfoto = (ImageView) v.findViewById(R.id.Imagen_establecimiento);
        Picasso.with(getContext()).load(GetInfoFromSharedPreferences("url_establecimiento")).into(Imgfoto);


        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaEstablecimiento listaEstablecimiento = new ListaEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaEstablecimiento);
                transaction.commit();
            }
        });

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
        return v;
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
