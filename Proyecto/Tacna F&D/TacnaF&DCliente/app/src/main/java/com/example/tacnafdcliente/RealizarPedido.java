package com.example.tacnafdcliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.tacnafdcliente.Model.Pedido;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import it.sephiroth.android.library.numberpicker.NumberPicker;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


public class RealizarPedido extends Fragment {


    public RealizarPedido() {
        // Required empty public constructor
    }
    String Direccion_Destino = "";
    String Punto_Geografico_Destino = "";


    String bid_establecimiento = "";

    boolean Booleano = false;
    boolean Booleano_2 = false;
    int posicion = 0;

    AlertDialog Alert_Dialog;

    ResultSet Result_Set;
    ResultSet Result_Set2;

    Spinner Spinner_Items_Menu;

    List<ItemMenu> Items;
    List<DetallePedido> Items_Detalle_Pedido = new ArrayList<>();

    Button Btnatras;
    Button Btnagregar;
    Button Btnpagar;

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

    String Urls = "https://data.fixer.io/api/latest?access_key=Api&base=PEN&symbols=USD&format=1";
    String Json_Result = "";

    String Precio_Dolar = "";

    String Codigo_Paypal = "";

    String Descripcion_Pedido = "";

    String Fecha_Actual = "";

    String Id_Cliente = "";
    String Nombre_Cliente = "";

    private static final int PAYPAL_REQUEST_CODE=7171;

    private static PayPalConfiguration CONFIG;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    ArrayList<String> Nombres_Items_Menu = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_realizar_pedido, container, false);

        inicializarfirebase();

        Id_Cliente = GetFromSharedPreferences("ID");
        Nombre_Cliente = GetFromSharedPreferences("nombre") + " " + GetFromSharedPreferences("apellido");

        Date date = new Date();
        String strDateFormat = "dd-MM-YYYY HH:mm:ss";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        Fecha_Actual = objSDF.format(date);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_DetallePedido) ;
        Txtsubtotal = (TextView) v.findViewById(R.id.txtsubtotal);
        Txttotal = (TextView) v.findViewById(R.id.txttotal);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();



        bid_establecimiento = GetInfoFromSharedPreferences("ID");

        Bundle bundle=getArguments();
        Direccion_Destino = bundle.getString("direcciondestino");
        Punto_Geografico_Destino = bundle.getString("puntogeograficodestino");
        Codigo_Paypal = bundle.getString("codigopaypal");

        CONFIG = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Codigo_Paypal);

        Intent intent=new Intent(getActivity().getApplicationContext(),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,CONFIG);
        getActivity().startService(intent);


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
                    Booleano_2 = false;
                }

                if(Booleano_2)
                {
                    int Nueva_Cantidad = Number_Picker.getProgress() + Items_Detalle_Pedido.get(posicion).getCantidad();
                    if(Nueva_Cantidad <= 20)
                    {
                        int Cantidad_Anterior = Items_Detalle_Pedido.get(posicion).getCantidad();
                        int Cantidad_Nueva = Number_Picker.getProgress() + Cantidad_Anterior;
                        Precio_Total_Item = Precio_Unitario * (Cantidad_Nueva);

                        Items_Detalle_Pedido.get(posicion).setCantidad(Cantidad_Nueva);
                        Items_Detalle_Pedido.get(posicion).setPrecio_Total(Precio_Total_Item);

                        Subtotal = Subtotal + (Precio_Unitario*Number_Picker.getProgress());
                        Total = Subtotal + 5;
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Solo se permite una cantidad maxima de 20",Toast.LENGTH_SHORT).show();
                    }


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




                }

                Txtsubtotal.setText("Subtotal: S/."+Subtotal);
                Txttotal.setText("Total: S/."+Total);

                Recycler_View.setHasFixedSize(true);
                Layout_Manager = new LinearLayoutManager(getActivity());
                Recycler_View.setLayoutManager(Layout_Manager);

                Adaptador = new DetallePedidoAdapter(Items_Detalle_Pedido,getActivity());

                Adaptador.setOnItemClickListener(new DetallePedidoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Subtotal=Subtotal-Items_Detalle_Pedido.get(position).getPrecio_Total();
                        Total=Subtotal+5;

                        Txtsubtotal.setText("Subtotal: S/."+Subtotal);
                        Txttotal.setText("Total: S/."+Total);

                        Items_Detalle_Pedido.remove(position);
                        Adaptador.notifyItemRemoved(position);
                    }
                });
                Recycler_View.setAdapter(Adaptador);





            }
        });

        Btnpagar = (Button) v.findViewById(R.id.btnpagar);
        Btnpagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Descripcion_Pedido = "";
                if(Items_Detalle_Pedido.size() != 0)
                {
                    for(int i=0; i<Items_Detalle_Pedido.size();i++)
                    {
                        Descripcion_Pedido = Descripcion_Pedido + Items_Detalle_Pedido.get(i).getCantidad() + "x " + Items_Detalle_Pedido.get(i).getNombre_Item_Menu() + ", ";
                    }
                    Descripcion_Pedido = Descripcion_Pedido.substring(0,Descripcion_Pedido.length()-2);
                    webService task = new webService();
                    task.execute();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Seleccione al menos un item",Toast.LENGTH_SHORT).show();
                }
            }
        });






        return v;
    }

    private void inicializarfirebase(){
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseStorage=FirebaseStorage.getInstance();
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

            Spinner_Items_Menu.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Nombres_Items_Menu));
            ID_Item_Menu = Items.get(0).getID_Item_Menu();
            Precio_Unitario = Double.parseDouble(Items.get(0).getPrecio().toString());
            Url_Imagen = Items.get(0).getUrl_Imagen();

            Alert_Dialog.dismiss();


        }


    }


    private class webService extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            try {
                URL url = new URL(Urls);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                {
                    return null;
                }
                else
                {

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0)
                {
                    return null;
                }
                else
                {

                }

                forecastJsonStr=buffer.toString();
                Json_Result=buffer.toString();
                return forecastJsonStr;

            }
            catch (IOException e)
            {
                return null;
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                else
                {

                }
                if(reader != null)
                {
                    try {
                        reader.close();
                    }
                    catch (final IOException e)
                    {

                    }
                }
                else
                {

                }
            }


        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Alert_Dialog.dismiss();
            getResultado();
        }
    }

    public void getResultado(){
        try{
            String redDivisa;
            JSONObject reader = new JSONObject(Json_Result);
            JSONObject rates = reader.getJSONObject("rates");
            redDivisa = rates.getString("USD");
            Precio_Dolar = redDivisa;
            Configurar_Pago();
        }
        catch (Exception e)
        {

        }
    }

    public void Configurar_Pago(){
        Double Soles_A_Dolares = Double.parseDouble(Precio_Dolar) * Total;
        Double Comision = Soles_A_Dolares * (5.4/100) + 0.3;
        Double Total_Con_Comision = Soles_A_Dolares + Comision;
        Comision = Total_Con_Comision * (5.4/100) + 0.3;
        Total_Con_Comision = Soles_A_Dolares + Comision;
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(Total_Con_Comision)),"USD","Pago del Pedido", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent=new Intent(getActivity().getApplicationContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,CONFIG);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==PAYPAL_REQUEST_CODE){
            if(resultCode==getActivity().RESULT_OK){
                PaymentConfirmation confirmation= data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirmation!=null){
                    try{

                        String paymentDetails=confirmation.toJSONObject().toString(4);

                        RegistrarPedido registrarPedido = new RegistrarPedido(getActivity().getApplicationContext());
                        registrarPedido.execute();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }

            }
            else if(requestCode== Activity.RESULT_CANCELED){
                Toast.makeText(getActivity().getApplicationContext(),"Cancelada",Toast.LENGTH_SHORT).show();

            }else if(requestCode==PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(getActivity().getApplicationContext(),"Invalida",Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    public class RegistrarPedido extends AsyncTask<Void, Void, Boolean> {


        private Context mContext = null;

        RegistrarPedido (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (Void... voids) {



            try {

                Statement stm = ConectarDB().createStatement();
                stm.execute("insert into Pedido(ID_Usuario_Cliente,ID_Establecimiento,Descripcion,Fecha,Estado,Precio_Total,Direccion_Destino," +
                        "PuntoGeografico_Destino) values(" + Id_Cliente + "," + bid_establecimiento + ",'" + Descripcion_Pedido + "','" + Fecha_Actual + "','Pendiente'," + Total +
                        ",'" + Direccion_Destino + "','"+Punto_Geografico_Destino+"')");

                Result_Set2 = stm.executeQuery("select * from Pedido where Descripcion='" + Descripcion_Pedido + "' and Fecha='"+Fecha_Actual+"'");

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


            try {

                if(Result_Set2.next())
                {

                    Pedido pedido = new Pedido();
                    pedido.setID_Pedido(Result_Set2.getInt(1));
                    pedido.setUsuario_Cliente(Nombre_Cliente);
                    pedido.setID_Establecimiento(Integer.parseInt(bid_establecimiento));
                    pedido.setID_Usuario_Repartidor(0);
                    pedido.setDescripcion(Descripcion_Pedido);
                    pedido.setFecha(Fecha_Actual);
                    pedido.setEstado("Pendiente");
                    pedido.setPrecio_Total(Total);
                    pedido.setDireccion_Destino(Direccion_Destino);
                    pedido.setPuntoGeografico_Destino(Punto_Geografico_Destino);
                    databaseReference.child("Pedido").child(String.valueOf(Result_Set2.getInt(1))).setValue(pedido);


                }
                else
                {

                }

            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            Alert_Dialog.dismiss();

            ListaPedidos listaPedidos = new ListaPedidos();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaPedidos);
            transaction.commit();


        }


    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
