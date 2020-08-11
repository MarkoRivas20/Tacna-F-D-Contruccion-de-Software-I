package com.example.tacnafdcliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tacnafdcliente.Model.Pedido;
import com.example.tacnafdcliente.Model.Seguimiento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;


public class SeguimientoPedido extends Fragment implements OnMapReadyCallback {

    public SeguimientoPedido() {
        // Required empty public constructor
    }

    Button Btnatras;

    ImageView Imagen_Establecimiento;

    TextView Txtnombre_Establecimiento;
    TextView Txtprecio_Pedido;
    TextView TxtDescripcion_Pedido;
    TextView Lbldistancia;
    TextView Lbltiempo;

    private GoogleMap Mapa;
    private CustomMapView Map_View;

    JSONObject jso;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_seguimiento_pedido, container, false);

        inicializarfirebase();

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
        Txtprecio_Pedido = (TextView) v.findViewById(R.id.txtprecio_pedido);
        TxtDescripcion_Pedido = (TextView) v.findViewById(R.id.txtdescripcion_pedido);
        Lbldistancia = (TextView) v.findViewById(R.id.lbldistancia);
        Lbltiempo = (TextView) v.findViewById(R.id.lbltiempo);

        Picasso.with(getContext()).load(GetInfoFromSharedPreferences("Url_Imagen_Logo")).into(Imagen_Establecimiento);
        Txtnombre_Establecimiento.setText(GetInfoFromSharedPreferences("Nombre"));
        Txtprecio_Pedido.setText("Precio: " + GetPedidoFromSharedPreferences("precio_total"));
        TxtDescripcion_Pedido.setText("Descripcion: " + GetPedidoFromSharedPreferences("descripcion_pedido"));

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaPedidos listaPedidos = new ListaPedidos();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaPedidos);
                transaction.commit();
            }
        });

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa = googleMap;

        databaseReference.child("Seguimiento").child(GetPedidoFromSharedPreferences("ID")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Mapa.clear();

                Seguimiento seguimiento = dataSnapshot.getValue(Seguimiento.class);
                String []Latlng_Repartidor = seguimiento.getPuntoGeografico().split("/");
                LatLng Ubicacion_Repartidor = new LatLng(Double.parseDouble(Latlng_Repartidor[0]),Double.parseDouble(Latlng_Repartidor[1]));

                Mapa.moveCamera(CameraUpdateFactory.newLatLng(Ubicacion_Repartidor));
                Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Mapa.moveCamera(CameraUpdateFactory.zoomTo(13));

                Mapa.addMarker(new MarkerOptions().position(Ubicacion_Repartidor).title("Repartidor").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                String desde = Latlng_Repartidor[0] + "," + Latlng_Repartidor[1];
                String[] hastapuntos = GetPedidoFromSharedPreferences("Punto_Geografico").split("/");
                String hasta = hastapuntos[0] + "," + hastapuntos[1];

                LatLng Ubicacion_Pedido = new LatLng(Double.parseDouble(hastapuntos[0]), Double.parseDouble(hastapuntos[1]));

                Mapa.addMarker(new MarkerOptions().position(Ubicacion_Pedido).title("Destino").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                String url="https://maps.googleapis.com/maps/api/directions/json?origin="+desde+"&destination="+hasta+"&key=API";

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
                locationA.setLatitude(Double.parseDouble(Latlng_Repartidor[0]));
                locationA.setLongitude(Double.parseDouble(Latlng_Repartidor[1]));

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    }


    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetPedidoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_pedido", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

}
