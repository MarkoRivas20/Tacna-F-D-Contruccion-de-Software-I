package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdbusiness.Adaptador.CarruselAdapter;
import com.example.tacnafdbusiness.Adaptador.ImagenesEstablecimientoAdapter;
import com.example.tacnafdbusiness.Adaptador.ListaEstablecimientoAdapter;
import com.example.tacnafdbusiness.Model.Carrusel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class PerfilEstablecimiento extends Fragment implements OnMapReadyCallback {

    public PerfilEstablecimiento() {
        // Required empty public constructor
    }

    private RecyclerView Recycler_View;
    private CarruselAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    ResultSet Result_Set;
    List<Carrusel> Items;

    TextView Txtnombre;
    TextView Txtdistrito;
    TextView Txtcategoria;
    TextView Txtdireccion;
    TextView Txtdescripcion;
    TextView Txtcapacidad;
    TextView Txttelefono;
    TextView Btnopciones;

    String Url_Imagen_Logo = "";
    String Punto_Geofragico = "";
    String Id_Establecimiento = "";

    ImageView Img_Logo;

    Button Btnmodificar;
    Button Btnfotos;
    Button Btnperfil;
    Button Btnresenas;
    Button Btncupon;
    Button Btnmenu;


    private GoogleMap Mapa;
    private CustomMapView Map_View;

    AlertDialog Alert_Dialog;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil_establecimiento, container, false);

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
        Btnmodificar = (Button) v.findViewById(R.id.btnmodificarestablecimiento);
        Btnopciones = (TextView) v.findViewById(R.id.Btnopciones);


        registerForContextMenu(Btnopciones);


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        ListaEstablecimientos fragmentEstablecimiento = new ListaEstablecimientos();
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


        Txtnombre.setText(GetInfoFromSharedPreferences("Nombre"));
        Txtdistrito.setText(GetInfoFromSharedPreferences("Distrito"));
        Txtcategoria.setText(GetInfoFromSharedPreferences("Categoria"));
        Txtdireccion.setText(GetInfoFromSharedPreferences("Direccion"));
        Txtdescripcion.setText(GetInfoFromSharedPreferences("Descripcion"));
        Txtcapacidad.setText(GetInfoFromSharedPreferences("Capacidad"));
        Txttelefono.setText(GetInfoFromSharedPreferences("Telefono"));
        Url_Imagen_Logo = GetInfoFromSharedPreferences("Url_Imagen_Logo");
        Punto_Geofragico = GetInfoFromSharedPreferences("Punto_Geografico");
        Id_Establecimiento = GetInfoFromSharedPreferences("ID");

        Picasso.with(getContext()).load(Url_Imagen_Logo).into(Img_Logo);

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);



        Btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ModificarEstablecimiento modificarEstablecimiento = new ModificarEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, modificarEstablecimiento);
                transaction.commit();
            }
        });

        Btnfotos = (Button) v.findViewById(R.id.btnimagenes);
        Btnfotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagenesEstablecimiento imagenesEstablecimiento = new ImagenesEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, imagenesEstablecimiento);
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

        Btnresenas = (Button) v.findViewById(R.id.btnreseñas);
        Btnresenas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaResenas listaResenas = new ListaResenas();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaResenas);
                transaction.commit();
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

        RellenarCarrusel();

        Recycler_View.setAdapter(Adaptador);

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        menu.add(Menu.NONE, 1, 1, "Visualizar Documento");
        menu.add(Menu.NONE, 2, 2, "Gestionar Repartidores");
        menu.add(Menu.NONE, 3, 3, "Visualizar Pedidos");
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
            case 2:
                ListaRepartidores listaRepartidores = new ListaRepartidores();
                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                transaction2.replace(R.id.contenedorfragment, listaRepartidores);
                transaction2.commit();
                break;
            case 3:
                ListaPedidos listaPedidos = new ListaPedidos();
                FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                transaction3.replace(R.id.contenedorfragment, listaPedidos);
                transaction3.commit();
                break;
        }
        return true;
    }

    @Override
    public void onMapReady (GoogleMap googleMap) {

        Mapa = googleMap;


        // Add a marker in Sydney and move the camera
        String[] ltdlng = Punto_Geofragico.split("/");

        LatLng lugar = new LatLng(Double.parseDouble(ltdlng[0]), Double.parseDouble(ltdlng[1]));
        Mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));


    }

    public Connection ConectarDB(){

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

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Imagen_Establecimiento where ID_Establecimiento=" + Id_Establecimiento);

            Items = new ArrayList<>();

            while (Result_Set.next()){
                Carrusel establecimiento = new Carrusel();
                establecimiento.setUrl_Imagen(Result_Set.getString(3));

                Items.add(establecimiento);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new CarruselAdapter(Items,getActivity());



        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }


    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }



}

