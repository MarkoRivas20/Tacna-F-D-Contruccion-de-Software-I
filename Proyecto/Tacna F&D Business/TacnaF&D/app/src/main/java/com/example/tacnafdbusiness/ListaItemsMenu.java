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

import com.example.tacnafdbusiness.Adaptador.ItemMenuAdapter;
import com.example.tacnafdbusiness.Model.ItemMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnanadir_Item;
    Button Btnreseñas;
    Button Btncupon;
    Button Btnimagenes;

    ImageView Img_Logo;

    TextView Txtnombre;
    TextView Btnopciones;

    ResultSet Result_Set;


    private RecyclerView Recycler_View;
    private ItemMenuAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<ItemMenu> Items;

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;

    final Bundle bundle2 = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_items_menu, container, false);

        InicializarFirebase();

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaMenu) ;


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

        Btnopciones = (TextView) v.findViewById(R.id.Btnopciones);
        registerForContextMenu(Btnopciones);

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

        Btnreseñas = (Button) v.findViewById(R.id.btnreseñas);
        Btnreseñas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaResenas listaResenas = new ListaResenas();
                listaResenas.setArguments(bundle2);

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
                listaCupon.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaCupon);
                transaction.commit();
            }
        });

        Btnimagenes = (Button) v.findViewById(R.id.btnimagenes);
        Btnimagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagenesEstablecimiento imagenesEstablecimiento = new ImagenesEstablecimiento();
                imagenesEstablecimiento.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, imagenesEstablecimiento);
                transaction.commit();
            }
        });

        Btnanadir_Item = (Button) v.findViewById(R.id.btnanadiritem);
        Btnanadir_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroItemMenu registroItemMenu = new RegistroItemMenu();
                registroItemMenu.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, registroItemMenu);
                transaction.commit();
            }
        });

        BuscarMenu();

        Adaptador.setOnItemClickListener(new ItemMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick (int position) {
                Toast.makeText(getActivity(),"Manten presionado para ver las opciones", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onModificar (int position) {

                final Bundle bundle3 = new Bundle();

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


                bundle3.putString("id_item_menu", String.valueOf(Items.get(position).getID_Item_Menu()));
                bundle3.putString("nombremenu", String.valueOf(Items.get(position).getNombre()));
                bundle3.putString("descripcionmenu", String.valueOf(Items.get(position).getDescripcion()));
                bundle3.putString("precio", String.valueOf(Items.get(position).getPrecio()));
                bundle3.putString("url_imagen", String.valueOf(Items.get(position).getUrl_Imagen()));

                ModificarItemMenu modificarItemMenu = new ModificarItemMenu();
                modificarItemMenu.setArguments(bundle3);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, modificarItemMenu);
                transaction.commit();
            }

            @Override
            public void onEliminar (int position) {

                String Id_item_menu = String.valueOf(Items.get(position).getID_Item_Menu());
                String url = Items.get(position).getUrl_Imagen();
                EliminarItem(Id_item_menu,url);
            }

            @Override
            public void onCancelar (int position) {

            }
        });

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

    private void InicializarFirebase(){

        FirebaseApp.initializeApp(getActivity());
        Firebase_Database = FirebaseDatabase.getInstance();
        Database_Reference = Firebase_Database.getReference();
        Storage_Reference = FirebaseStorage.getInstance().getReference();
        Firebase_Storage = FirebaseStorage.getInstance();
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


    public void BuscarMenu(){

        try {

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Item_Menu where ID_Establecimiento=" + bid_establecimiento);

            Items = new ArrayList<>();

            while (Result_Set.next()){

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

    public void EliminarItem (String ID_Item, String url){
        try {

            Statement stm = ConectarDB().createStatement();
            stm.execute("Delete from Item_Menu where ID_Item_Menu=" + ID_Item);


        }catch (Exception e){

        }

        StorageReference photoref = Firebase_Storage.getReferenceFromUrl(url);
        photoref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),"Item Eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        BuscarMenu();
        Recycler_View.setAdapter(Adaptador);
    }
}
