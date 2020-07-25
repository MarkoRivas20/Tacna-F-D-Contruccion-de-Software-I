package com.example.tacnafdcliente;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdcliente.Adaptador.ItemMenuAdapter;
import com.example.tacnafdcliente.Model.ItemMenu;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListaItemsMenu extends Fragment {


    public ListaItemsMenu() {
        // Required empty public constructor
    }

    String bid_establecimiento = "";
    String burl_imagen_logo = "";

    boolean Booleano = false;

    ImageView Img_Logo;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnprincipal;
    Button Btnruta;
    Button Btncupon;
    Button Btnreseñas;
    Button Btnrealizar_Pedido;


    TextView Txtnombre;
    TextView Btnopciones;

    ResultSet Result_Set;


    private RecyclerView Recycler_View;
    private ItemMenuAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<ItemMenu> Items;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_items_menu, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaMenu) ;

        Btnopciones = (TextView) v.findViewById(R.id.Btnopciones);
        registerForContextMenu(Btnopciones);

        bid_establecimiento = GetInfoFromSharedPreferences("ID");
        burl_imagen_logo = GetInfoFromSharedPreferences("Url_Imagen_Logo");

        Booleano = Boolean.valueOf(GetResenaFromSharedPreferences("Bandera_Resena"));

        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(GetInfoFromSharedPreferences("Nombre"));


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

        Btndescripcion = (Button) v.findViewById(R.id.btndescripcion);
        Btndescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
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
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, rutaMapa);
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

        Btnrealizar_Pedido = (Button) v.findViewById(R.id.btnrealizarpedido);
        Btnrealizar_Pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatosPedido datosPedido = new DatosPedido();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, datosPedido);
                transaction.commit();
            }
        });

        BuscarMenu();


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

    public Connection ConnectionDB(){

        Connection cnn=null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");


        }catch (Exception e){

        }

        return cnn;
    }


    public void BuscarMenu(){

        try {

            Statement stm2 = ConnectionDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Item_Menu where ID_Establecimiento=" + bid_establecimiento);

            Items = new ArrayList<>();

            while (Result_Set.next())
            {
                ItemMenu itemMenu = new ItemMenu();
                itemMenu.setID_Item_Menu(Result_Set.getInt(1));
                itemMenu.setID_Establecimiento(Result_Set.getInt(2));
                itemMenu.setNombre(Result_Set.getString(3));
                itemMenu.setDescripcion(Result_Set.getString(4));
                itemMenu.setPrecio(Result_Set.getDouble(5));
                itemMenu.setUrl_Imagen(Result_Set.getString(6));
                Items.add(itemMenu);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new ItemMenuAdapter(Items,getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }


    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }


    private String GetResenaFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_resena", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
