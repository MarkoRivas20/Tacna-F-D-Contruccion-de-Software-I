package com.example.tacnafddelivery;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;


public class DetallePedido extends Fragment implements OnMapReadyCallback {


    public DetallePedido() {
        // Required empty public constructor
    }

    Button Btnatras;
    Button Btnperfil;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);

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
                PerfilUsuario perfilUsuario = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilUsuario);
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
    public void onMapReady (GoogleMap googleMap) {
        Mapa = googleMap;
        Mapa.clear();


        String []LatLng = GetPedidoFromSharedPreferences("Punto_Geografico").split("/");
        LatLng lugar = new LatLng(Double.parseDouble(LatLng[0]), Double.parseDouble(LatLng[1]));
        Mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));
        Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Mapa.moveCamera(CameraUpdateFactory.zoomTo(11));

    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetPedidoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_pedido", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
