package com.example.tacnafdcliente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


public class DatosPedido extends Fragment implements OnMapReadyCallback {


    public DatosPedido() {
        // Required empty public constructor
    }

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

    String Mi_Comentario = "";
    Float Mi_Puntuacion = (float) 0.0;

    Button Btnatras;
    Button Btnsiguiente;

    EditText Txtnombre_Cliente;
    EditText Txtnombre_Establecimiento;
    EditText TxtDireccion;

    private GoogleMap Mapa;
    private CustomMapView Map_View;
    String Punto_Geografico = "-18.007667/-70.239441";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_datos_pedido, container, false);

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

        String Nombre_Cliente = GetFromSharedPreferences("nombre") + " " + GetFromSharedPreferences("apellido");

        final Bundle bundle = getArguments();

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

        TxtDireccion = (EditText) v.findViewById(R.id.txtdireccion_pedido);

        Txtnombre_Cliente = (EditText) v.findViewById(R.id.txtnombrecliente);
        Txtnombre_Cliente.setText(Nombre_Cliente);
        Txtnombre_Cliente.setEnabled(false);

        Txtnombre_Establecimiento = (EditText) v.findViewById(R.id.txtnombreestablecimiento);
        Txtnombre_Establecimiento.setText(bnombre);
        Txtnombre_Establecimiento.setEnabled(false);

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                listaItemsMenu.setArguments(bundle2);

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
                    bundle2.putString("direcciondestino",TxtDireccion.getText().toString());
                    bundle2.putString("puntogeograficodestino",Punto_Geografico);

                    RealizarPedido realizarPedido = new RealizarPedido();
                    realizarPedido.setArguments(bundle2);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedorfragment, realizarPedido);
                    transaction.commit();
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
        Mapa.moveCamera(CameraUpdateFactory.newLatLng(Tacna));

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
}
