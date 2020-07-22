package com.example.tacnafdcliente;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class RutaMapa extends Fragment implements OnMapReadyCallback {

    public RutaMapa() {
        // Required empty public constructor
    }

    String bid_establecimiento = "";
    String burl_imagen_logo = "";
    String bpuntogeografico = "";

    boolean Booleano = false;

    ImageView Img_Logo;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnprincipal;
    Button Btnmenu;
    Button Btncupon;
    Button Btnreseñas;

    TextView Txtnombre;
    TextView Lbldistancia;
    TextView Lbltiempo;


    private GoogleMap Mapa;
    private CustomMapView Map_View;


    private LocationManager Location_Manager;
    private Location Locacion;
    String Mi_Ltd = "";
    String Mi_Lng = "";
    double Latitude = 0.0;
    double Longitud = 0.0;
    Geocoder Geocodificador;

    private FusedLocationProviderClient Fused_Location_Provider_Client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_ruta_mapa, container, false);


        RequestPermission();
        Fused_Location_Provider_Client = LocationServices.getFusedLocationProviderClient(getActivity());
        ObtenerLtdLng();

        Lbldistancia = (TextView) v.findViewById(R.id.lbldistancia);
        Lbltiempo = (TextView) v.findViewById(R.id.lbltiempo);



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


        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);


        return v;
    }

    private void RequestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION},1);
    }

    private void ObtenerLtdLng(){
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            return;
        }
        else
        {

        }

        Fused_Location_Provider_Client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    Latitude = location.getLatitude();
                    Longitud = location.getLongitude();
                }
                else
                {

                }
            }
        });


    }

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
            Mapa.setMyLocationEnabled(true);

            Location_Manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Locacion = Location_Manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Mi_Ltd = String.valueOf(Latitude);
            Mi_Lng = String.valueOf(Longitud);
            Geocodificador = new Geocoder(getContext(), Locale.getDefault());
            try {

                String desde = Mi_Ltd + "," + Mi_Lng;
                String[] hastapuntos = bpuntogeografico.split("/");
                String hasta = hastapuntos[0] + "," + hastapuntos[1];

                final LatLng farma1 = new LatLng(Double.parseDouble(hastapuntos[0]), Double.parseDouble(hastapuntos[1]));

                Mapa.addMarker(new MarkerOptions().position(farma1).title("Puntos").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                new DibujarRuta (getContext(), Mapa, desde, hasta).execute();

                Location locationA = new Location("punto A");
                locationA.setLatitude(Double.parseDouble(Mi_Ltd));
                locationA.setLongitude(Double.parseDouble(Mi_Lng));

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



                //new RutaMapa(getContext(),mapa,desde,hasta).execute();





            }catch (Exception ex)
            {

            }
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
