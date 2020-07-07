package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdcliente.Model.Cupon;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class PantallaPrincipal extends Fragment {


    public PantallaPrincipal() {
        // Required empty public constructor
    }


    Button Btnperfil;
    Button Btnestablecimiento;
    Button Btnmicupon;

    TextView Txtcontador;
    ArcSeekBar SeekBar;

    int Nro_Cupones = 0;

    int Total_Cupones = 0;



    AlertDialog Alert_Dialog;



    String Id_Usuario = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pantalla_principal, container, false);

        Id_Usuario = GetFromSharedPreferences("ID");

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Txtcontador = (TextView) v.findViewById(R.id.txtcontador);
        SeekBar = (ArcSeekBar) v.findViewById(R.id.seekbar);
        SeekBar.setMaxProgress(5);


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

        Nro_Cupones = GetNroCuponesFromSharedPreferences("NroCupones");



        if (Nro_Cupones == 0)
        {
            final int nrocupones = 0;
            SaveNroCuponesSharedPreferences(nrocupones);
            Txtcontador.setText(String.valueOf(nrocupones));
            SeekBar.setProgress(nrocupones);
        }
        else
        {

            /*
            if(nrocupones<0){
                txtcontador.setText(String.valueOf(nrocupones));
                seekBar.setProgress(0);
            }else{
                txtcontador.setText(String.valueOf(nrocupones));
                seekBar.setProgress(nrocupones);
            }*/

            Txtcontador.setText(String.valueOf(Nro_Cupones));
            SeekBar.setProgress(Nro_Cupones);


        }



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

        Btnestablecimiento = (Button) v.findViewById(R.id.btnestablecimientos);
        Btnestablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarEstablecimiento buscarEstablecimiento = new BuscarEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, buscarEstablecimiento);
                transaction.commit();
            }
        });

        Btnmicupon = (Button)v.findViewById(R.id.btnmicupon);
        Btnmicupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaMiCupon listaMiCupon = new ListaMiCupon();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaMiCupon);
                transaction.commit();
            }
        });

        //new myasynctask(getActivity()).execute(new String[]{"buscarcu"});


        return v;
    }

    private void SaveNroCuponesSharedPreferences (int nrocupones){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Numero_Cupones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("NroCupones", nrocupones);

        editor.apply();
    }

    private int GetNroCuponesFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Numero_Cupones", Context.MODE_PRIVATE);
        return sharedPref.getInt(Key,0);
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }





}
