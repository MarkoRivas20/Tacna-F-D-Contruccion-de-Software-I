package com.example.tacnafdcliente;

import android.os.Bundle;

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

import com.example.tacnafdcliente.Adaptador.CuponAdapter;
import com.example.tacnafdcliente.Model.Cupon;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListaCupon extends Fragment {


    public ListaCupon() {
        // Required empty public constructor
    }
    String Nombre = "";
    String Distrito = "";
    String Categoria = "";
    String Capacidad = "";

    String bid_establecimiento = "";
    String bnombre = "";
    String bdistrito = "";
    String bcategoria = "";
    String bdireccion = "";
    String btelefono = "";
    String bdescripcion = "";
    String bcapacidad = "";
    String btotalresenas = "";
    String bpuntuacion = "";
    String burl_imagen_logo = "";
    String bpuntogeografico = "";
    String bestado = "";

    boolean Booleano = false;

    ImageView Img_Logo;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnprincipal;
    Button Btnruta;
    Button Btnmenu;
    Button Btnreseñas;

    TextView Txtnombre;


    private RecyclerView Recycler_View;
    private CuponAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<Cupon> Items;

    ResultSet Result_Set;

    String Mi_Comentario = "";
    Float Mi_Puntuacion = (float) 0.0;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_cupon, container, false);


        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaCupon) ;

        Bundle bundle = getArguments();

        bid_establecimiento = bundle.getString("id_establecimiento");
        bnombre = bundle.getString("nombre");
        bdistrito = bundle.getString("distrito");
        bcategoria = bundle.getString("categoria");
        bdireccion = bundle.getString("direccion");
        btelefono = bundle.getString("telefono");
        bdescripcion = bundle.getString("descripcion");
        bcapacidad = bundle.getString("capacidad");
        btotalresenas = bundle.getString("totalresenas");
        bpuntuacion = bundle.getString("puntuacion");
        burl_imagen_logo = bundle.getString("url_imagen_logo");
        bpuntogeografico = bundle.getString("puntogeografico");
        bestado = bundle.getString("estado");
        Nombre = bundle.getString("nombreb");
        Distrito = bundle.getString("distritob");
        Categoria = bundle.getString("categoriab");
        Capacidad = bundle.getString("capacidadb");
        Booleano = bundle.getBoolean("banderaresena");
        Mi_Comentario = bundle.getString("micomentario");
        Mi_Puntuacion = bundle.getFloat("mipuntuacion");

        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(bnombre);

        final Bundle bundle2 = new Bundle();

        bundle2.putString("id_establecimiento",bid_establecimiento);
        bundle2.putString("nombre",bnombre);
        bundle2.putString("distrito",bdistrito);
        bundle2.putString("categoria",bcategoria);
        bundle2.putString("direccion",bdireccion);
        bundle2.putString("telefono",btelefono);
        bundle2.putString("descripcion",bdescripcion);
        bundle2.putString("capacidad",bcapacidad);
        bundle2.putString("totalresenas",btotalresenas);
        bundle2.putString("puntuacion",bpuntuacion);
        bundle2.putString("url_imagen_logo",burl_imagen_logo);
        bundle2.putString("puntogeografico",bpuntogeografico);
        bundle2.putString("estado",bestado);
        bundle2.putString("nombreb",Nombre);
        bundle2.putString("distritob",Distrito);
        bundle2.putString("categoriab",Categoria);
        bundle2.putString("capacidadb",Capacidad);
        bundle2.putBoolean("banderaresena",Booleano);
        bundle2.putString("micomentario",Mi_Comentario);
        bundle2.putFloat("mipuntuacion",Mi_Puntuacion);

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
                        bundle3.putString("nombre",Nombre);
                        bundle3.putString("distrito",Distrito);
                        bundle3.putString("categoria",Categoria);
                        bundle3.putString("capacidad",Capacidad);

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

        Btndescripcion = (Button) v.findViewById(R.id.btndescripcion);
        Btndescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                perfilEstablecimiento.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
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

        Btnreseñas = (Button)v.findViewById(R.id.btnreseñas);
        Btnreseñas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!Booleano)
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

        Btnprincipal = (Button)v.findViewById(R.id.btnprincipal);
        Btnprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
                transaction.commit();
            }
        });

        BuscarCupones();
        Recycler_View.setAdapter(Adaptador);

        return v;
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

    public void BuscarCupones() {

        try {

            Statement stm2 = ConnectionDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Cupon where ID_Establecimiento=" + bid_establecimiento + " and Estado='Activo'");

            Items = new ArrayList<>();

            while (Result_Set.next())
            {
                Cupon cupon = new Cupon();
                cupon.setID_Cupon(Result_Set.getInt(1));
                cupon.setID_Establecimiento(Result_Set.getInt(2));
                cupon.setTitulo(Result_Set.getString(3));
                cupon.setUrl_Imagen(Result_Set.getString(4));
                cupon.setDescripcion(Result_Set.getString(5));
                cupon.setFecha_Inicio(Result_Set.getDate(6));
                cupon.setFecha_Final(Result_Set.getDate(7));
                cupon.setEstado(Result_Set.getString(8));
                Items.add(cupon);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new CuponAdapter(Items,getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }
}
