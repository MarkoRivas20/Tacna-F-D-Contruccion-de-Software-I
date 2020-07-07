package com.example.tacnafdbusiness;

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

import com.example.tacnafdbusiness.Adaptador.CuponAdapter;
import com.example.tacnafdbusiness.Model.Cupon;
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
    String burl_imagen_documento = "";
    String bpuntogeografico = "";
    String bestado = "";

    ImageView Img_Logo;

    TextView Txtnombre;
    TextView Btnopciones;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnfoto;
    Button Btnresena;
    Button Btnanadir_Cupon;
    Button Btnmenu;


    private RecyclerView Recycler_View;
    private CuponAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<Cupon> Items;

    ResultSet Result_Set;

    final Bundle bundle2 = new Bundle();


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lista_cupon, container, false);

        Btnopciones = (TextView) v.findViewById(R.id.Btnopciones);
        registerForContextMenu(Btnopciones);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaCupon) ;

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
        burl_imagen_documento = bundle.getString("url_imagen_documento");
        bpuntogeografico = bundle.getString("puntogeografico");
        bestado = bundle.getString("estado");

        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(bnombre);



        bundle2.putString("id_establecimiento", bid_establecimiento);
        bundle2.putString("nombre", bnombre);
        bundle2.putString("distrito", bdistrito);
        bundle2.putString("categoria", bcategoria);
        bundle2.putString("direccion", bdireccion);
        bundle2.putString("telefono", btelefono);
        bundle2.putString("descripcion", bdescripcion);
        bundle2.putString("capacidad", bcapacidad);
        bundle2.putString("totalresenas", btotalresenas);
        bundle2.putString("puntuacion", bpuntuacion);
        bundle2.putString("url_imagen_logo", burl_imagen_logo);
        bundle2.putString("url_imagen_documento", burl_imagen_documento);
        bundle2.putString("puntogeografico", bpuntogeografico);
        bundle2.putString("estado", bestado);

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

        Btnfoto = (Button) v.findViewById(R.id.btnimagenes);
        Btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagenesEstablecimiento imagenesEstablecimiento  = new ImagenesEstablecimiento();
                imagenesEstablecimiento.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, imagenesEstablecimiento);
                transaction.commit();

            }
        });
        Btnresena = (Button) v.findViewById(R.id.btnreseñas);
        Btnresena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaResenas listaResenas = new ListaResenas();
                listaResenas.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaResenas);
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



        Btnanadir_Cupon = (Button) v.findViewById(R.id.btnanadircupon);
        Btnanadir_Cupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroCupon registroCupon = new RegistroCupon();
                registroCupon.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, registroCupon);
                transaction.commit();
            }
        });

        BuscarCupones();
        Recycler_View.setAdapter(Adaptador);

        Adaptador.setOnItemClickListener(new CuponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick (int position) {
                Toast.makeText(getActivity(),"Manten presionado para ver las opciones", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onModificar (int position) {
                final Bundle bundle3=new Bundle();

                bundle3.putString("id_establecimiento", bid_establecimiento);
                bundle3.putString("nombre", bnombre);
                bundle3.putString("distrito", bdistrito);
                bundle3.putString("categoria", bcategoria);
                bundle3.putString("direccion", bdireccion);
                bundle3.putString("telefono", btelefono);
                bundle3.putString("descripcion", bdescripcion);
                bundle3.putString("capacidad", bcapacidad);
                bundle3.putString("totalresenas", btotalresenas);
                bundle3.putString("puntuacion", bpuntuacion);
                bundle3.putString("url_imagen_logo", burl_imagen_logo);
                bundle3.putString("url_imagen_documento", burl_imagen_documento);
                bundle3.putString("puntogeografico", bpuntogeografico);
                bundle3.putString("estado", bestado);


                bundle3.putString("id_cupon", String.valueOf(Items.get(position).getID_Cupon()));
                bundle3.putString("titulo", String.valueOf(Items.get(position).getTitulo()));
                bundle3.putString("url_imagen", String.valueOf(Items.get(position).getUrl_Imagen()));
                bundle3.putString("descripcioncupon", String.valueOf(Items.get(position).getDescripcion()));
                bundle3.putString("fecha_inicio", String.valueOf(Items.get(position).getFecha_Inicio()));
                bundle3.putString("fecha_final", String.valueOf(Items.get(position).getFecha_Final()));
                bundle3.putString("estadocupon", String.valueOf(Items.get(position).getEstado()));

                ModificarCupon modificarCupon = new ModificarCupon();
                modificarCupon.setArguments(bundle3);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, modificarCupon);
                transaction.commit();

            }

            @Override
            public void onCancelar(int position) {

            }
        });

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
                visualizarDocumento.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, visualizarDocumento);
                transaction.commit();
                break;
            case 2:
                ListaRepartidores listaRepartidores = new ListaRepartidores();
                listaRepartidores.setArguments(bundle2);
                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                transaction2.replace(R.id.contenedorfragment, listaRepartidores);
                transaction2.commit();
                break;
            case 3:
                ListaPedidos listaPedidos = new ListaPedidos();
                listaPedidos.setArguments(bundle2);
                FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                transaction3.replace(R.id.contenedorfragment, listaPedidos);
                transaction3.commit();
                break;
        }
        return true;
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

    public void BuscarCupones(){

        try {

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Cupon where ID_Establecimiento=" + bid_establecimiento);

            Items = new ArrayList<>();

            while (Result_Set.next()){
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
