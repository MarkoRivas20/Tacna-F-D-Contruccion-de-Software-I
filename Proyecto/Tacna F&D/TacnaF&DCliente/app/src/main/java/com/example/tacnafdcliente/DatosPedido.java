package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;


public class DatosPedido extends Fragment implements OnMapReadyCallback {


    public DatosPedido() {
        // Required empty public constructor
    }



    String bid_establecimiento = "";

    Button Btnatras;
    Button Btnsiguiente;

    EditText Txtnombre_Cliente;
    EditText Txtnombre_Establecimiento;
    EditText TxtDireccion;

    private GoogleMap Mapa;
    private CustomMapView Map_View;
    String Punto_Geografico = "-18.003328/-70.247577";

    ResultSet Result_Set;

    AlertDialog Alert_Dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_datos_pedido, container, false);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

        String Nombre_Cliente = GetFromSharedPreferences("nombre") + " " + GetFromSharedPreferences("apellido");

        Txtnombre_Establecimiento = (EditText) v.findViewById(R.id.txtnombreestablecimiento);

        if(GetEstablecimientoCuponFromSharedPreferences("ID").equals(""))
        {
            bid_establecimiento = GetInfoFromSharedPreferences("ID");
            Txtnombre_Establecimiento.setText(GetInfoFromSharedPreferences("Nombre"));
        }
        else
        {
            bid_establecimiento = GetEstablecimientoCuponFromSharedPreferences("ID");
            Txtnombre_Establecimiento.setText(GetEstablecimientoCuponFromSharedPreferences("Nombre"));
        }
        Txtnombre_Establecimiento.setEnabled(false);


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

        TxtDireccion = (EditText) v.findViewById(R.id.txtdireccion_pedido);

        Txtnombre_Cliente = (EditText) v.findViewById(R.id.txtnombrecliente);
        Txtnombre_Cliente.setText(Nombre_Cliente);
        Txtnombre_Cliente.setEnabled(false);

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveEstablecimientoCuponSharedPreferences("","","","");
                ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaItemsMenu);
                transaction.commit();
            }
        });

        Btnsiguiente = (Button) v.findViewById(R.id.btnsiguiente);
        Btnsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TxtDireccion.length() == 0 || Punto_Geografico.equals("")){
                    if(TxtDireccion.length() == 0){
                        TxtDireccion.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if(Punto_Geografico.equals(""))
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Seleccione el punto de entrega en el mapa",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                }
                else
                {

                    BuscarCodigoPaypal task = new BuscarCodigoPaypal(getActivity().getApplicationContext());
                    task.execute();
                }
            }
        });



        return v;
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa = googleMap;

        LatLng Tacna = new LatLng(-18.012580, -70.246520);
        Mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(Tacna,15));

        Mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Mapa.clear();
                String latitud = String.valueOf(latLng.latitude);
                String longitud = String.valueOf(latLng.longitude);

                MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.valueOf(latitud), Double.valueOf(longitud))).title("Aqui");
                Mapa.addMarker(marker);

                Punto_Geografico = latitud + "/" + longitud;
            }
        });
    }

    public Connection ConnectionDB(){

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

    public class BuscarCodigoPaypal extends AsyncTask<Void, Void, Boolean> {


        private Context mContext = null;

        BuscarCodigoPaypal (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (Void... voids) {



            try {

                Statement stm2 = ConnectionDB().createStatement();
                Result_Set = stm2.executeQuery("select up.Codigo_PayPal from Establecimiento e inner join Usuario_Propietario up \n" +
                        "on e.ID_Usuario_Propietario=up.ID_Usuario_Propietario where e.ID_Establecimiento=" + bid_establecimiento);

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

            Alert_Dialog.dismiss();
            try {

                if(Result_Set.next())
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("direcciondestino",TxtDireccion.getText().toString());
                    bundle.putString("puntogeograficodestino",Punto_Geografico);
                    bundle.putString("codigopaypal",Result_Set.getString(1));

                    RealizarPedido realizarPedido = new RealizarPedido();
                    realizarPedido.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedorfragment, realizarPedido);
                    transaction.commit();
                }
                else
                {

                }

            }catch (Exception e){
                Log.e("Error", e.toString());
            }




        }


    }

    private void SaveEstablecimientoCuponSharedPreferences(String ID, String Nombre, String Descuento,String ID_Cupon_Usuario){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_establecimiento_cupon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("Nombre", Nombre);
        editor.putString("Descuento", Descuento);
        editor.putString("ID_Cupon_Usuario", ID_Cupon_Usuario);
        editor.apply();
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetEstablecimientoCuponFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento_cupon", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
