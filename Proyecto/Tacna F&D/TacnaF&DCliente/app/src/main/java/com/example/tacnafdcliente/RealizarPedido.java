package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdcliente.Adaptador.DetallePedidoAdapter;
import com.example.tacnafdcliente.Adaptador.ListaEstablecimientoAdapter;
import com.example.tacnafdcliente.Model.DetallePedido;
import com.example.tacnafdcliente.Model.ItemMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import it.sephiroth.android.library.numberpicker.NumberPicker;


public class RealizarPedido extends Fragment {


    public RealizarPedido() {
        // Required empty public constructor
    }
    String Direccion_Destino = "";
    String Punto_Geografico_Destino = "";

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
    boolean Booleano_2 = false;
    int posicion = 0;

    String Mi_Comentario = "";
    Float Mi_Puntuacion = (float) 0.0;

    AlertDialog Alert_Dialog;

    ResultSet Result_Set;

    Spinner Spinner_Items_Menu;

    List<ItemMenu> Items;
    List<DetallePedido> Items_Detalle_Pedido = new ArrayList<>();

    Button Btnatras;
    Button Btnagregar;

    NumberPicker Number_Picker;

    int ID_Item_Menu = 0;
    Double Precio_Unitario = 0.0;
    Double Precio_Total_Item = 0.0;
    String Url_Imagen = "";

    private RecyclerView Recycler_View;
    private DetallePedidoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    TextView Txtsubtotal;
    TextView Txttotal;

    Double Subtotal = 0.0;
    Double Total = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_realizar_pedido, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_DetallePedido) ;
        Txtsubtotal = (TextView) v.findViewById(R.id.txtsubtotal);
        Txttotal = (TextView) v.findViewById(R.id.txttotal);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

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
        bpuntuacion= bundle.getString("puntuacion");
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
        Direccion_Destino = bundle.getString("direcciondestino");
        Punto_Geografico_Destino = bundle.getString("puntogeograficodestino");

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
        Spinner_Items_Menu = (Spinner) v.findViewById(R.id.spinneritemsmenu);

        RellenarSpinnerItemsMenu rellenarSpinnerItemsMenu= new RellenarSpinnerItemsMenu(getActivity().getApplicationContext());
        rellenarSpinnerItemsMenu.execute();



        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatosPedido datosPedido = new DatosPedido();
                datosPedido.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, datosPedido);
                transaction.commit();
            }
        });

        Spinner_Items_Menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ID_Item_Menu = Items.get(position).getID_Item_Menu();
                Precio_Unitario = Double.parseDouble(Items.get(position).getPrecio().toString());
                Url_Imagen = Items.get(position).getUrl_Imagen();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Number_Picker = (NumberPicker) v.findViewById(R.id.numberPicker);

        Btnagregar = (Button) v.findViewById(R.id.btnagregar);
        Btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Items_Detalle_Pedido.size() != 0)
                {
                    for(int i=0; i<Items_Detalle_Pedido.size();i++)
                    {
                        if(String.valueOf(ID_Item_Menu).equals(String.valueOf(Items_Detalle_Pedido.get(i).getID_Item_Menu()))){

                            Booleano_2 = true;
                            posicion = i;
                            break;

                        }
                        else
                        {
                            Booleano_2 = false;

                        }
                    }
                }
                else
                {

                }

                if(Booleano_2)
                {
                    int Cantidad_Anterior = Items_Detalle_Pedido.get(posicion).getCantidad();
                    int Cantidad_Nueva = Number_Picker.getProgress() + Cantidad_Anterior;
                    Precio_Total_Item = Precio_Unitario * (Cantidad_Nueva);

                    Items_Detalle_Pedido.get(posicion).setCantidad(Cantidad_Nueva);
                    Items_Detalle_Pedido.get(posicion).setPrecio_Total(Precio_Total_Item);

                    Subtotal = Subtotal + (Precio_Unitario*Number_Picker.getProgress());
                    Total = Subtotal + 5;

                    Recycler_View.setHasFixedSize(true);
                    Layout_Manager = new LinearLayoutManager(getActivity());
                    Recycler_View.setLayoutManager(Layout_Manager);

                    Adaptador = new DetallePedidoAdapter(Items_Detalle_Pedido,getActivity());
                    Recycler_View.setAdapter(Adaptador);
                }
                else
                {

                    Precio_Total_Item = Precio_Unitario * Number_Picker.getProgress();

                    DetallePedido detallePedido = new DetallePedido();
                    detallePedido.setID_Item_Menu(ID_Item_Menu);
                    detallePedido.setNombre_Item_Menu(Spinner_Items_Menu.getSelectedItem().toString());
                    detallePedido.setCantidad(Number_Picker.getProgress());
                    detallePedido.setPrecio_Unitario(Precio_Unitario);
                    detallePedido.setUrl_Imagen(Url_Imagen);
                    detallePedido.setPrecio_Total(Precio_Total_Item);

                    Items_Detalle_Pedido.add(detallePedido);

                    Subtotal = Subtotal + Precio_Total_Item;
                    Total = Subtotal + 5;

                    Recycler_View.setHasFixedSize(true);
                    Layout_Manager = new LinearLayoutManager(getActivity());
                    Recycler_View.setLayoutManager(Layout_Manager);

                    Adaptador = new DetallePedidoAdapter(Items_Detalle_Pedido,getActivity());
                    Recycler_View.setAdapter(Adaptador);
                }

                Txtsubtotal.setText("Subtotal: S/."+Subtotal);
                Txttotal.setText("Total: S/."+Total);





            }
        });




        return v;
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

    public class RellenarSpinnerItemsMenu extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        RellenarSpinnerItemsMenu (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                Statement stm2 = ConectarDB().createStatement();
                Result_Set = stm2.executeQuery("select * from Item_Menu where ID_Establecimiento=" + bid_establecimiento);

                ArrayList<String> Nombres_Items_Menu = new ArrayList<String>();

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
                    Nombres_Items_Menu.add(Result_Set.getString(3));
                }



                Spinner_Items_Menu.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Nombres_Items_Menu));


            }catch (Exception e){
                Log.e("Error", e.toString());
            }




            return true;
        }

        @Override
        protected  void onPreExecute(){

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            ID_Item_Menu = Items.get(0).getID_Item_Menu();
            Precio_Unitario = Double.parseDouble(Items.get(0).getPrecio().toString());
            Url_Imagen = Items.get(0).getUrl_Imagen();

            Alert_Dialog.dismiss();


        }


    }
}
