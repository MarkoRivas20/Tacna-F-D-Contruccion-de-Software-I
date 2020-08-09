package com.example.tacnafddelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafddelivery.model.Pedido;
import com.example.tacnafddelivery.model.Seguimiento;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class DetallePedido extends Fragment implements OnMapReadyCallback {


    public DetallePedido() {
        // Required empty public constructor
    }

    Button Btnatras;
    Button Btnperfil;
    Button Btnrealizar_Seguimiento;

    TextView Txtid_Pedido;
    TextView Txtnombre_Cliente_Pedido;
    TextView Txtnombre_Establecimiento;
    TextView Txtdescripcion_Pedido;
    TextView Txtdireccion_Establecimiento;
    TextView Txtfecha_Pedido;
    TextView Txtdireccion_Pedido;

    ImageView Foto_Establecimiento;

    private GoogleMap Mapa;
    private CustomMapView Map_View;

    double Latitude = 0.0;
    double Longitud = 0.0;


    Location locationA = new Location("Punto A");
    Location locationB = new Location("Punto B");

    float distancia = (float) 0.0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    AlertDialog Alert_Dialog;

    ResultSet Result_Set;

    LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);

        inicializarfirebase();

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        String [] Punto_Geografico = GetInfoFromSharedPreferences("puntogeografico_establecimiento").split("/");
        locationB.setLatitude(Double.parseDouble(Punto_Geografico[0]));
        locationB.setLongitude(Double.parseDouble(Punto_Geografico[1]));

        RequestPermission();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListenerNetwork);


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

        Txtid_Pedido = (TextView) v.findViewById(R.id.txtid_pedido);
        Txtnombre_Cliente_Pedido = (TextView) v.findViewById(R.id.txtnombre_cliente_pedido);
        Txtnombre_Establecimiento = (TextView) v.findViewById(R.id.txtnombre_establecimiento);
        Txtdescripcion_Pedido = (TextView) v.findViewById(R.id.txtdescripcion_pedido);
        Txtdireccion_Establecimiento = (TextView) v.findViewById(R.id.txtdireccion_establecimiento);
        Txtfecha_Pedido = (TextView) v.findViewById(R.id.txtfecha_pedido);
        Txtdireccion_Pedido = (TextView) v.findViewById(R.id.txtdireccion_pedido);
        Foto_Establecimiento = (ImageView) v.findViewById(R.id.Imagen_establecimiento);

        Txtid_Pedido.setText("ID: " + GetPedidoFromSharedPreferences("ID"));
        Txtnombre_Cliente_Pedido.setText("Nombre Cliente: " + GetPedidoFromSharedPreferences("nombre_cliente"));
        Txtnombre_Establecimiento.setText(GetInfoFromSharedPreferences("nombre_establecimiento"));
        Txtdescripcion_Pedido.setText("Descripcion: " + GetPedidoFromSharedPreferences("descripcion_pedido"));
        Txtdireccion_Establecimiento.setText(GetInfoFromSharedPreferences("direccion_establecimiento"));
        Txtfecha_Pedido.setText("Fecha: " + GetPedidoFromSharedPreferences("fecha_pedido"));
        Txtdireccion_Pedido.setText("Direccion: " + GetPedidoFromSharedPreferences("direccion_pedido"));
        Picasso.with(getContext()).load(GetInfoFromSharedPreferences("url_establecimiento")).into(Foto_Establecimiento);

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.removeUpdates(locationListenerNetwork);
                ListaPedidos listaPedidos = new ListaPedidos();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaPedidos);
                transaction.commit();
            }
        });

        Btnperfil = (Button) v.findViewById(R.id.btnperfil);
        Btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.removeUpdates(locationListenerNetwork);
                PerfilUsuario perfilUsuario = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilUsuario);
                transaction.commit();

            }
        });

        Btnrealizar_Seguimiento = (Button) v.findViewById(R.id.btnrealizarseguimiento);
        Btnrealizar_Seguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveSeguimientoSharedPreferences("Aceptado");
                ListarPedido task = new ListarPedido(getActivity());
                task.execute();

            }
        });

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady (GoogleMap googleMap) {
        Mapa = googleMap;
        Mapa.clear();


        String []LatLng = GetPedidoFromSharedPreferences("Punto_Geografico").split("/");
        LatLng lugar = new LatLng(Double.parseDouble(LatLng[0]), Double.parseDouble(LatLng[1]));
        Mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));
        Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Mapa.moveCamera(CameraUpdateFactory.zoomTo(11));

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(lugar);
        Mapa.clear();
        Mapa.animateCamera(CameraUpdateFactory.newLatLng(lugar));
        Mapa.addMarker(markerOptions);


    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            Longitud = location.getLongitude();
            Latitude = location.getLatitude();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    locationA.setLatitude(Latitude);
                    locationA.setLongitude(Longitud);

                    distancia = locationA.distanceTo(locationB);

                    if(distancia<=50)
                    {
                        Btnrealizar_Seguimiento.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Btnrealizar_Seguimiento.setVisibility(View.INVISIBLE);
                    }

                    Toast.makeText(getActivity(),"distancia: "+ distancia,Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

        }
        @Override
        public void onProviderDisabled(String s) {

        }

    };

    private void RequestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION},1);
    }

    public class ListarPedido extends AsyncTask<Void, Void, Boolean> {


        private Context mContext = null;

        ListarPedido (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (Void... voids) {

            try {
                locationManager.removeUpdates(locationListenerNetwork);
                Statement stm2 = ConnectionDB().createStatement();
                Result_Set = stm2.executeQuery("Select * from Pedido where ID_Pedido='" + GetPedidoFromSharedPreferences("ID") + "'");

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

            //Alert_Dialog.dismiss();

            try {

                while(Result_Set.next())
                {

                    if(Result_Set.getString(7).equals("Pendiente"))
                    {
                        ActualizarPedido actualizarPedido = new ActualizarPedido(getActivity());
                        actualizarPedido.execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Este pedido ya fue aceptado",Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public class ActualizarPedido extends AsyncTask<Void, Void, Boolean> {


        private Context mContext2 = null;

        ActualizarPedido (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (Void... voids) {



            try {


                Statement stm = ConnectionDB().createStatement();
                stm.executeUpdate("Update Pedido set Estado='En Curso', ID_Usuario_Repartidor="+GetFromSharedPreferences("ID")+
                        " where ID_Pedido='" + GetPedidoFromSharedPreferences("ID") + "'");




            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            Alert_Dialog.dismiss();

            Pedido pedido = new Pedido();
            pedido.setDescripcion(GetPedidoFromSharedPreferences("descripcion_pedido"));
            pedido.setDireccion_Destino(GetPedidoFromSharedPreferences("direccion_pedido"));
            pedido.setEstado("En Curso");
            pedido.setFecha(GetPedidoFromSharedPreferences("fecha_pedido"));
            pedido.setID_Establecimiento(Integer.parseInt(GetInfoFromSharedPreferences("ID")));
            pedido.setID_Pedido(Integer.parseInt(GetPedidoFromSharedPreferences("ID")));
            pedido.setID_Usuario_Repartidor(Integer.parseInt(GetFromSharedPreferences("ID")));
            pedido.setPrecio_Total(Double.parseDouble(GetPedidoFromSharedPreferences("precio_total")));
            pedido.setPuntoGeografico_Destino(GetPedidoFromSharedPreferences("Punto_Geografico"));
            pedido.setUsuario_Cliente(GetPedidoFromSharedPreferences("nombre_cliente"));
            databaseReference.child("Pedido").child(GetPedidoFromSharedPreferences("ID")).setValue(pedido);



            SeguimientoPedido seguimientoPedido = new SeguimientoPedido();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, seguimientoPedido);
            transaction.commit();






        }


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

    private void inicializarfirebase(){
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }


    private void SaveSeguimientoSharedPreferences(String seguimiento){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_estado_pedido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Seguimiento", seguimiento);

        editor.apply();
    }
    private String GetSeguimientoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_estado_pedido", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetPedidoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_pedido", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
