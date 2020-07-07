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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    String url_imagen_logo = "";
    String puntogeofragico = "";
    String totalresenas = "";
    String puntuacion = "";
    String estado = "";
    String ID_Establecimiento = "";
    String nombre = "";
    String distrito = "";
    String categoria = "";
    String capacidad = "";

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

    final Bundle bundle2 = new Bundle();

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



        Bundle bundle = getArguments();

        Txtnombre.setText(bundle.getString("nombre"));
        Txtdistrito.setText(bundle.getString("distrito"));
        Txtcategoria.setText(bundle.getString("categoria"));
        Txtdireccion.setText(bundle.getString("direccion"));
        Txtdescripcion.setText(bundle.getString("descripcion"));
        Txtcapacidad.setText(bundle.getString("capacidad"));
        Txttelefono.setText(bundle.getString("telefono"));
        url_imagen_logo = bundle.getString("url_imagen_logo");
        puntogeofragico = bundle.getString("puntogeografico");
        totalresenas = bundle.getString("totalresenas");
        puntuacion = bundle.getString("puntuacion");
        estado = bundle.getString("estado");
        ID_Establecimiento = bundle.getString("id_establecimiento");
        nombre = bundle.getString("nombreb");
        distrito = bundle.getString("distritob");
        categoria = bundle.getString("categoriab");
        capacidad = bundle.getString("capacidadb");

        Picasso.with(getContext()).load(url_imagen_logo).into(Img_Logo);



        bundle2.putString("id_establecimiento", ID_Establecimiento);
        bundle2.putString("nombre", Txtnombre.getText().toString());
        bundle2.putString("distrito", Txtdistrito.getText().toString());
        bundle2.putString("categoria", Txtcategoria.getText().toString());
        bundle2.putString("direccion", Txtdireccion.getText().toString());
        bundle2.putString("telefono", Txttelefono.getText().toString());
        bundle2.putString("descripcion", Txtdescripcion.getText().toString());
        bundle2.putString("capacidad", Txtcapacidad.getText().toString());
        bundle2.putString("totalresenas", totalresenas);
        bundle2.putString("puntuacion", puntuacion);
        bundle2.putString("url_imagen_logo", url_imagen_logo);
        bundle2.putString("puntogeografico", puntogeofragico);
        bundle2.putString("estado", estado);
        bundle2.putString("nombreb", nombre);
        bundle2.putString("distritob", distrito);
        bundle2.putString("categoriab", categoria);
        bundle2.putString("capacidadb", capacidad);


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        Bundle bundle3 = new Bundle();
                        bundle3.putString("nombre", nombre);
                        bundle3.putString("distrito", distrito);
                        bundle3.putString("categoria", categoria);
                        bundle3.putString("capacidad", capacidad);

                        ListaEstablecimiento fragmentEstablecimiento = new ListaEstablecimiento();

                        fragmentEstablecimiento.setArguments(bundle3);
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
                rutaMapa.setArguments(bundle2);

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
                listaItemsMenu.setArguments(bundle2);

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
                    listaResenas.setArguments(bundle2);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedorfragment, listaResenas);
                    transaction.commit();
                }
                else
                {
                    ListaResenas2 listaResenas2 = new ListaResenas2();
                    listaResenas2.setArguments(bundle2);

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
                listaCupon.setArguments(bundle2);

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

            bundle2.putBoolean("banderaresena", Booleano);
            bundle2.putString("micomentario", Mi_Comentario);
            bundle2.putFloat("mipuntuacion", Mi_Puntuacion);

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


}
