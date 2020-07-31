package com.example.tacnafddelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tacnafddelivery.model.Pedido;
import com.example.tacnafddelivery.model.Seguimiento;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SeguimientoPedido extends Fragment implements OnMapReadyCallback {

    public SeguimientoPedido() {
        // Required empty public constructor
    }

    Button Btnterminar_seguimiento;

    ImageView Imagen_Establecimiento;

    TextView Txtnombre_Establecimiento;
    TextView Txtdireccion_Destino;
    TextView TxtDescripcion_Pedido;
    TextView Lbldistancia;
    TextView Lbltiempo;

    private GoogleMap Mapa;
    private CustomMapView Map_View;


    JSONObject jso;

    double Latitude = 0.0;
    double Longitud = 0.0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    String Id_Pedido = "";

    LocationManager locationManager;

    AlertDialog Alert_Dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seguimiento_pedido, container, false);

        inicializarfirebase();
        Id_Pedido = GetPedidoFromSharedPreferences("ID");

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();


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


        Imagen_Establecimiento = (ImageView) v.findViewById(R.id.Imagen_establecimiento);

        Txtnombre_Establecimiento = (TextView) v.findViewById(R.id.txtnombre_establecimiento);
        Txtdireccion_Destino = (TextView) v.findViewById(R.id.txtdireccion_destino);
        TxtDescripcion_Pedido = (TextView) v.findViewById(R.id.txtdescripcion_pedido);
        Lbldistancia = (TextView) v.findViewById(R.id.lbldistancia);
        Lbltiempo = (TextView) v.findViewById(R.id.lbltiempo);

        Picasso.with(getContext()).load(GetInfoFromSharedPreferences("url_establecimiento")).into(Imagen_Establecimiento);
        Txtnombre_Establecimiento.setText(GetInfoFromSharedPreferences("nombre_establecimiento"));
        Txtdireccion_Destino.setText("Direccion: " + GetPedidoFromSharedPreferences("direccion_pedido"));
        TxtDescripcion_Pedido.setText("Descripcion: " + GetPedidoFromSharedPreferences("descripcion_pedido"));

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListenerNetwork);



        Btnterminar_seguimiento = (Button) v.findViewById(R.id.btnterminarseguimiento);
        Btnterminar_seguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager.removeUpdates(locationListenerNetwork);

                Pedido pedido = new Pedido();
                pedido.setDescripcion(GetPedidoFromSharedPreferences("descripcion_pedido"));
                pedido.setDireccion_Destino(GetPedidoFromSharedPreferences("direccion_pedido"));
                pedido.setEstado("Entregado");
                pedido.setFecha(GetPedidoFromSharedPreferences("fecha_pedido"));
                pedido.setID_Establecimiento(Integer.parseInt(GetInfoFromSharedPreferences("ID")));
                pedido.setID_Pedido(Integer.parseInt(GetPedidoFromSharedPreferences("ID")));
                pedido.setID_Usuario_Repartidor(Integer.parseInt(GetFromSharedPreferences("ID")));
                pedido.setPrecio_Total(Double.parseDouble(GetPedidoFromSharedPreferences("precio_total")));
                pedido.setPuntoGeografico_Destino(GetPedidoFromSharedPreferences("Punto_Geografico"));
                pedido.setUsuario_Cliente(GetPedidoFromSharedPreferences("nombre_cliente"));
                databaseReference.child("Pedido").child(GetPedidoFromSharedPreferences("ID")).setValue(pedido);

                SaveSeguimientoSharedPreferences("");

                new ActualizarPedido(getActivity()).execute(new String[]{"Actualizar"});

            }
        });

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

        return v;
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            Longitud = location.getLongitude();
            Latitude = location.getLatitude();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Seguimiento seguimiento= new Seguimiento();
                    seguimiento.setID_Pedido(Integer.parseInt(GetPedidoFromSharedPreferences("ID")));
                    seguimiento.setPuntoGeografico(Latitude+"/"+Longitud);
                    databaseReference.child("Seguimiento").child(Id_Pedido).setValue(seguimiento);
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


    @Override
    public void onMapReady (GoogleMap googleMap) {
        Mapa = googleMap;
        Mapa.clear();

        LatLng lugar = new LatLng(-18.0038755, -70.225904);

        Mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));
        Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Mapa.moveCamera(CameraUpdateFactory.zoomTo(11));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getContext(),"No se ha definido los permisos necesarios", Toast.LENGTH_SHORT).show();


        }
        else
        {

            String [] PuntoGeografico_Establecimiento = GetInfoFromSharedPreferences("puntogeografico_establecimiento").split("/");
            String Ltd_Origen = PuntoGeografico_Establecimiento[0];
            String Lng_Origen = PuntoGeografico_Establecimiento[1];
            try {

                String desde = Ltd_Origen + "," + Lng_Origen;
                String[] hastapuntos = GetPedidoFromSharedPreferences("Punto_Geografico").split("/");
                String hasta = hastapuntos[0] + "," + hastapuntos[1];

                final LatLng farma1 = new LatLng(Double.parseDouble(hastapuntos[0]), Double.parseDouble(hastapuntos[1]));

                Mapa.addMarker(new MarkerOptions().position(farma1).title("Puntos").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                String url="https://maps.googleapis.com/maps/api/directions/json?origin="+desde+"&destination="+hasta+"&key=Api";

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            jso=new JSONObject(response);
                            trazarRuta(jso);

                        }catch (JSONException e){}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);

                Location locationA = new Location("punto A");
                locationA.setLatitude(Double.parseDouble(Ltd_Origen));
                locationA.setLongitude(Double.parseDouble(Lng_Origen));

                Location locationB = new Location("punto B");
                locationB.setLatitude(Double.parseDouble(hastapuntos[0]));
                locationB.setLongitude(Double.parseDouble(hastapuntos[1]));

                float distancia = locationA.distanceTo(locationB);
                float tiempo = Math.round(distancia / 500);
                distancia = distancia / 1000;
                DecimalFormat df = new DecimalFormat("#.0");
                Lbldistancia.setText(df.format(distancia)+" km");

                String str = String.valueOf(tiempo);
                int intNumber = Integer.parseInt(str.substring(0, str.indexOf('.')));
                Lbltiempo.setText(intNumber+" minutos");


            }catch (Exception ex)
            {

            }
        }
    }

    private void trazarRuta(JSONObject jso) {
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        try{
            jRoutes=jso.getJSONArray("routes");
            for(int i=0;i<jRoutes.length();i++){
                jLegs=((JSONObject)(jRoutes.get(i))).getJSONArray("legs");
                for(int j=0;j<jLegs.length();j++){
                    jSteps=((JSONObject)(jLegs.get(j))).getJSONArray("steps");
                    for(int k=0;k<jSteps.length();k++){
                        String polyline=""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list= PolyUtil.decode(polyline);
                        Mapa.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(5));
                    }
                }
            }
        }catch (JSONException e){}
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

    public class ActualizarPedido extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarPedido (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                Statement stm = ConectarDB().createStatement();
                stm.execute("Update Pedido set Estado='Entregado' where ID_Pedido='" + GetPedidoFromSharedPreferences("ID") + "'");


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

            ListaEstablecimiento listaEstablecimiento = new ListaEstablecimiento();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaEstablecimiento);
            transaction.commit();

        }


    }

    private void SaveSeguimientoSharedPreferences(String seguimiento){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_estado_pedido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Seguimiento", seguimiento);

        editor.apply();
    }

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
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
}
