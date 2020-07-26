package com.example.tacnafddelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafddelivery.Adaptador.PedidoAdapter;
import com.example.tacnafddelivery.model.Pedido;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ListaPedidos extends Fragment {

    public ListaPedidos() {
        // Required empty public constructor
    }

    Button Btnperfil;
    Button Btnatras;

    TextView Txtnombre_establecimiento;
    TextView Txtdireccion_establecimiento;
    ImageView Imgfoto;

    private List<Pedido> Lista_Pedido = new ArrayList<Pedido>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    private RecyclerView Recycler_View;
    private PedidoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_pedidos, container, false);

        inicializarfirebase();
        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaPedido) ;

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

        Txtnombre_establecimiento = (TextView) v.findViewById(R.id.txtnombre_establecimiento);
        Txtnombre_establecimiento.setText(GetInfoFromSharedPreferences("nombre_establecimiento"));

        Txtdireccion_establecimiento = (TextView) v.findViewById(R.id.txtdireccion_establecimiento);
        Txtdireccion_establecimiento.setText(GetInfoFromSharedPreferences("direccion_establecimiento"));

        Imgfoto = (ImageView) v.findViewById(R.id.Imagen_establecimiento);
        Picasso.with(getContext()).load(GetInfoFromSharedPreferences("url_establecimiento")).into(Imgfoto);


        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaEstablecimiento listaEstablecimiento = new ListaEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaEstablecimiento);
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

        databaseReference.child("Pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Lista_Pedido.clear();
                for(DataSnapshot objdatasnapshot : dataSnapshot.getChildren())
                {
                    Pedido p = objdatasnapshot.getValue(Pedido.class);
                    if(p.getID_Establecimiento() == Integer.parseInt(GetInfoFromSharedPreferences("ID")))
                    {
                        if(p.getEstado().equals("Pendiente"))
                        {
                            Lista_Pedido.add(p);

                            Recycler_View.setHasFixedSize(true);
                            Layout_Manager = new LinearLayoutManager(getActivity());
                            Recycler_View.setLayoutManager(Layout_Manager);

                            Adaptador = new PedidoAdapter(Lista_Pedido,getActivity());

                            Adaptador.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    SavePedidoSharedPreferences(String.valueOf(Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getID_Pedido()),
                                            Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getUsuario_Cliente(),
                                            Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion(),
                                            Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getFecha(),
                                            Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getDireccion_Destino(),
                                            Lista_Pedido.get(Recycler_View.getChildAdapterPosition(v)).getPuntoGeografico_Destino());

                                    DetallePedido detallePedido = new DetallePedido();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.contenedorfragment, detallePedido);
                                    transaction.commit();
                                }
                            });

                            Recycler_View.setAdapter(Adaptador);
                        }
                        else
                        {

                        }

                    }
                    else
                    {

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void inicializarfirebase(){
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseStorage=FirebaseStorage.getInstance();
    }

    private void SavePedidoSharedPreferences(String ID, String nombre_cliente, String descripcion_pedido, String fecha_pedido, String direccion_pedido, String Punto_Geografico){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_pedido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("nombre_cliente", nombre_cliente);
        editor.putString("descripcion_pedido", descripcion_pedido);
        editor.putString("fecha_pedido", fecha_pedido);
        editor.putString("direccion_pedido", direccion_pedido);
        editor.putString("Punto_Geografico", Punto_Geografico);
        editor.apply();
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
