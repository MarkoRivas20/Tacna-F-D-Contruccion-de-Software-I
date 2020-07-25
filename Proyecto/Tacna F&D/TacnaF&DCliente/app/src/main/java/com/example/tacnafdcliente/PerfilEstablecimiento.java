package com.example.tacnafdcliente;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.ViewUtils;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdcliente.Adaptador.CarruselAdapter;
import com.example.tacnafdcliente.Model.Carrusel;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class PerfilEstablecimiento extends Fragment {

    public PerfilEstablecimiento() {
        // Required empty public constructor
    }

    private RecyclerView Recycler_View;
    private CarruselAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    ResultSet Result_Set_1;
    ResultSet Result_Set_2;
    List<Carrusel> Items;

    AlertDialog Alert_Dialog;

    TextView Txtnombre;
    TextView Txtdistrito;
    TextView Txtcategoria;
    TextView Txtdireccion;
    TextView Txtdescripcion;
    TextView Txtcapacidad;
    TextView Txttelefono;
    TextView Btnopciones;

    String url_imagen_logo = "";
    String ID_Establecimiento = "";

    ImageView Img_Logo;

    Button Btnllamar;
    Button Btnruta;
    Button Btnperfil;
    Button Btnreseñas;
    Button Btncupon;
    Button Btnmenu;
    Button Btnprincipal;

    private static final int REQUEST_CALL_PHONE = 0;

    String id_Usuario = "";
    String Mi_Comentario = "";
    Float Mi_Puntuacion = (float) 0.0;

    boolean Booleano = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil_establecimiento, container, false);

        id_Usuario = GetFromSharedPreferences("ID");

        Recycler_View = (RecyclerView) v.findViewById(R.id.carruselfotos) ;

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtdistrito = (TextView) v.findViewById(R.id.lbldistrito);
        Txtcategoria = (TextView) v.findViewById(R.id.lblcategoria);
        Txtdireccion = (TextView) v.findViewById(R.id.lbldireccion);
        Txtdescripcion = (TextView) v.findViewById(R.id.lbldescripcion);
        Txtcapacidad = (TextView) v.findViewById(R.id.lblcapacidad);
        Txttelefono = (TextView) v.findViewById(R.id.lbltelefono);
        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);
        Btnllamar = (Button) v.findViewById(R.id.btnllamar);
        Btnopciones = (TextView) v.findViewById(R.id.Btnopciones);
        registerForContextMenu(Btnopciones);

        Txtnombre.setText(GetInfoFromSharedPreferences("Nombre"));
        Txtdistrito.setText(GetInfoFromSharedPreferences("Distrito"));
        Txtcategoria.setText(GetInfoFromSharedPreferences("Categoria"));
        Txtdireccion.setText(GetInfoFromSharedPreferences("Direccion"));
        Txtdescripcion.setText(GetInfoFromSharedPreferences("Descripcion"));
        Txtcapacidad.setText(GetInfoFromSharedPreferences("Capacidad"));
        Txttelefono.setText(GetInfoFromSharedPreferences("Telefono"));
        url_imagen_logo = GetInfoFromSharedPreferences("Url_Imagen_Logo");
        ID_Establecimiento = GetInfoFromSharedPreferences("ID");

        Picasso.with(getContext()).load(url_imagen_logo).into(Img_Logo);


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {


                        ListaEstablecimiento fragmentEstablecimiento = new ListaEstablecimiento();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedorfragment, fragmentEstablecimiento);
                        transaction.commit();

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



        Btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Txttelefono.getText().toString()));
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                }
                else
                {
                    startActivity(i);
                }
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

        Btnruta = (Button) v.findViewById(R.id.btnruta);
        Btnruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RutaMapa rutaMapa = new RutaMapa();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, rutaMapa);
                transaction.commit();
            }
        });

        Btnmenu = (Button) v.findViewById(R.id.btnmenu);
        Btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaItemsMenu);
                transaction.commit();
            }
        });

        Btnreseñas = (Button) v.findViewById(R.id.btnreseñas);
        Btnreseñas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Booleano)
                {
                    ListaResenas listaResenas = new ListaResenas();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedorfragment, listaResenas);
                    transaction.commit();
                }
                else
                {
                    ListaResenas2 listaResenas2 = new ListaResenas2();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedorfragment, listaResenas2);
                    transaction.commit();
                }


            }
        });

        Btncupon = (Button) v.findViewById(R.id.btncupon);
        Btncupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ListaCupon listaCupon = new ListaCupon();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaCupon);
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


        RellenarCarrusel();

        Recycler_View.setAdapter(Adaptador);

        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        menu.add(Menu.NONE, 1, 1, "Visualizar Documento");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(getActivity().getApplicationContext(), "Selected Item: " +item.getItemId(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()){
            case 1:
                VisualizarDocumento visualizarDocumento = new VisualizarDocumento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, visualizarDocumento);
                transaction.commit();
                break;
        }
        return true;
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    public Connection ConnectionDB(){

        Connection cnn = null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");


        }catch (Exception e){

        }

        return cnn;
    }

    public void RellenarCarrusel(){
        try {

            Statement stm2 = ConnectionDB().createStatement();
            Result_Set_1 = stm2.executeQuery("Select * from Imagen_Establecimiento where ID_Establecimiento=" + ID_Establecimiento);

            Statement stm3 = ConnectionDB().createStatement();
            Result_Set_2 = stm3.executeQuery("Select * from Resena where ID_Establecimiento=" + ID_Establecimiento + " and ID_Usuario_Cliente=" + id_Usuario);

            if (Result_Set_2.next())
            {
                Booleano = true;
                Mi_Comentario = Result_Set_2.getString(4);
                Mi_Puntuacion = (float)Result_Set_2.getDouble(5);

            }
            else
            {

            }


            SaveResenaSharedPreferences(Booleano,Mi_Comentario,Mi_Puntuacion);

            Items = new ArrayList<>();

            while (Result_Set_1.next())
            {
                Carrusel establecimiento = new Carrusel();
                establecimiento.setUrl_Imagen(Result_Set_1.getString(3));

                Items.add(establecimiento);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new CarruselAdapter(Items, getActivity());



        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private void SaveResenaSharedPreferences(Boolean Bandera_Resena, String Mi_Comentario, Float Mi_Puntuacion){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_resena", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(Bandera_Resena){
            editor.putString("Bandera_Resena", "true");
        }
        else
        {
            editor.putString("Bandera_Resena", "false");
        }
        editor.putString("Mi_Comentario", Mi_Comentario);
        editor.putString("Mi_Puntuacion", String.valueOf(Mi_Puntuacion));
        editor.apply();
    }


}
