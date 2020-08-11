package com.example.tacnafdbusiness;

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
    String burl_imagen_logo = "";

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



        bid_establecimiento = GetInfoFromSharedPreferences("ID");
        burl_imagen_logo = GetInfoFromSharedPreferences("Url_Imagen_Logo");

        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(GetInfoFromSharedPreferences("Nombre"));


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


                SaveInfoCuponSharedPreferences(String.valueOf(Items.get(position).getID_Cupon()), String.valueOf(Items.get(position).getTitulo()),
                        String.valueOf(Items.get(position).getUrl_Imagen()), String.valueOf(Items.get(position).getDescripcion()),
                        String.valueOf(Items.get(position).getFecha_Inicio()), String.valueOf(Items.get(position).getFecha_Final()),
                        String.valueOf(Items.get(position).getEstado()), String.valueOf(Items.get(position).getPorcentaje_Descuento()));

                ModificarCupon modificarCupon = new ModificarCupon();
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

    public Connection ConectarDB(){

        Connection cnn = null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://tacnafyd.database.windows.net:1433;databaseName=TacnaFyD;user=MarkoRivas;password=Tacna2018.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=ContruccionI.database.windows.net;loginTimeout=30;");


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
                cupon.setPorcentaje_Descuento(Integer.parseInt(Result_Set.getString(9)));
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

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private void SaveInfoCuponSharedPreferences(String ID_Cupon, String Titulo, String Url_Imagen_Cupon, String Descripcion_Cupon,
                                                String Fecha_Inicio, String Fecha_Final,String Estado_Cupon, String Descuento_Cupon){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_cupon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID_Cupon", ID_Cupon);
        editor.putString("Titulo", Titulo);
        editor.putString("Url_Imagen_Cupon", Url_Imagen_Cupon);
        editor.putString("Descripcion_Cupon", Descripcion_Cupon);
        editor.putString("Fecha_Inicio", Fecha_Inicio);
        editor.putString("Fecha_Final", Fecha_Final);
        editor.putString("Estado_Cupon", Estado_Cupon);
        editor.putString("Descuento_Cupon", Descuento_Cupon);
        editor.apply();
    }
}
