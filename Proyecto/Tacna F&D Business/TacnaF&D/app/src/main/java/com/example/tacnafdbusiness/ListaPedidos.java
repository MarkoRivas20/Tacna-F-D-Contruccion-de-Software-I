package com.example.tacnafdbusiness;

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

import com.example.tacnafdbusiness.Adaptador.PedidoAdapter;
import com.example.tacnafdbusiness.Adaptador.RepartidorAdapter;
import com.example.tacnafdbusiness.Model.Pedido;
import com.example.tacnafdbusiness.Model.Repartidores;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ListaPedidos extends Fragment {

    public ListaPedidos() {
        // Required empty public constructor
    }

    private List<Pedido> Lista_Pedido = new ArrayList<Pedido>();

    String bid_establecimiento = "";

    Button Btnatras;

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



        bid_establecimiento = GetInfoFromSharedPreferences("ID");



        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();

            }
        });

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


        databaseReference.child("Pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Lista_Pedido.clear();
                for(DataSnapshot objdatasnapshot : dataSnapshot.getChildren())
                {
                    Pedido p = objdatasnapshot.getValue(Pedido.class);
                    if(p.getID_Establecimiento() == Integer.parseInt(bid_establecimiento))
                    {
                        if(p.getUsuario_Repartidor() == null)
                        {
                            p.setUsuario_Repartidor("");
                        }
                        else
                        {

                        }
                        Lista_Pedido.add(p);

                        Recycler_View.setHasFixedSize(true);
                        Layout_Manager = new LinearLayoutManager(getActivity());
                        Recycler_View.setLayoutManager(Layout_Manager);

                        Adaptador = new PedidoAdapter(Lista_Pedido,getActivity());

                        Recycler_View.setAdapter(Adaptador);
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
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseStorage=FirebaseStorage.getInstance();
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
