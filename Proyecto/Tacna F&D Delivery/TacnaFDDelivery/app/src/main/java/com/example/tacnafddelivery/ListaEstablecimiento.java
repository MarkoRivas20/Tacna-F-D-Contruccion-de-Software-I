package com.example.tacnafddelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafddelivery.Adaptador.EstablecimientoAdapter;
import com.example.tacnafddelivery.model.Establecimiento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaEstablecimiento extends Fragment {

    public ListaEstablecimiento() {
        // Required empty public constructor
    }

    String Id_Usuario = "";

    Button Btnperfil;

    TextView Txtid;

    private RecyclerView Recycler_View;
    private EstablecimientoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    ResultSet Result_Set;
    AlertDialog Alert_Dialog;

    List<Establecimiento> Items;

    String ID_Establecimiento = "";
    String nombre = "";
    String distrito = "";
    String categoria = "";
    String direccion = "";
    String telefono = "";
    String descripcion = "";
    String capacidad = "";
    String totalresenas = "";
    String puntuacion = "";
    String url_imagen_logo = "";
    String url_imagen_documento = "";
    String estado = "";
    String puntogeofrafico = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_establecimiento, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaEstablecimiento) ;

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

        Txtid = (TextView) v.findViewById(R.id.txtmiid);

        Id_Usuario = GetFromSharedPreferences("ID");
        Txtid.setText("Mi ID : " + Id_Usuario);

        RellenarLista();

        Adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"ID: " + Items.get(Recycler_View.getChildAdapterPosition(v)).getID_Establecimiento(),Toast.LENGTH_SHORT).show();
                ID_Establecimiento = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getID_Establecimiento());
                nombre = Items.get(Recycler_View.getChildAdapterPosition(v)).getNombre();
                distrito = Items.get(Recycler_View.getChildAdapterPosition(v)).getDistrito();
                categoria = Items.get(Recycler_View.getChildAdapterPosition(v)).getCategoria();
                direccion = Items.get(Recycler_View.getChildAdapterPosition(v)).getDireccion();
                telefono = Items.get(Recycler_View.getChildAdapterPosition(v)).getTelefono();
                descripcion = Items.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion();
                capacidad = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getCapacidad());
                totalresenas = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getTotalResenas());
                puntuacion = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getPuntuacion());
                url_imagen_logo = Items.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen_Logo();
                url_imagen_documento = Items.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen_Documento();
                puntogeofrafico = Items.get(Recycler_View.getChildAdapterPosition(v)).getPuntoGeografico();
                estado = Items.get(Recycler_View.getChildAdapterPosition(v)).getEstado();

                SaveInfoSharedPreferences(ID_Establecimiento,nombre,direccion,url_imagen_logo);

                ListaPedidos listaPedidos = new ListaPedidos();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaPedidos);
                transaction.commit();




            }
        });
        Recycler_View.setAdapter(Adaptador);

        Btnperfil = (Button) v.findViewById(R.id.btnperfil);
        Btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerfilUsuario fragmentPerfil = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, fragmentPerfil);
                transaction.commit();
            }
        });


        return v;
    }

    public Connection ConectarDB(){

        Connection cnn = null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");


        }catch (Exception e){

        }

        return cnn;
    }

    public void RellenarLista(){


        try {

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("select e.* from Repartidor_Establecimiento re inner join Establecimiento e " +
                    "on re.ID_Establecimiento=e.ID_Establecimiento where re.ID_Usuario_Repartidor=" + Id_Usuario);

            Items = new ArrayList<>();

            while (Result_Set.next()){

                Establecimiento establecimiento = new Establecimiento();
                establecimiento.setID_Establecimiento(Result_Set.getInt(1));
                establecimiento.setID_Usuario_Propietario(Result_Set.getInt(2));
                establecimiento.setDistrito(Result_Set.getString(4));
                establecimiento.setCategoria(Result_Set.getString(5));
                establecimiento.setTelefono(Result_Set.getString(7));
                establecimiento.setDescripcion(Result_Set.getString(8));
                establecimiento.setCapacidad(Result_Set.getInt(9));
                establecimiento.setPuntoGeografico(Result_Set.getString(13));
                establecimiento.setUrl_Imagen_Logo(Result_Set.getString(12));
                establecimiento.setUrl_Imagen_Documento(Result_Set.getString(15));
                establecimiento.setNombre(Result_Set.getString(3));
                establecimiento.setDireccion(Result_Set.getString(6));
                establecimiento.setTotalResenas(Result_Set.getInt(10));
                establecimiento.setPuntuacion(Result_Set.getDouble(11));
                establecimiento.setEstado(Result_Set.getString(14));

                Items.add(establecimiento);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new EstablecimientoAdapter(Items, getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
        }


    }

    private void SaveInfoSharedPreferences(String ID, String nombre_establecimiento, String direccion_establecimiento, String url_establecimiento){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("nombre_establecimiento", nombre_establecimiento);
        editor.putString("direccion_establecimiento", direccion_establecimiento);
        editor.putString("url_establecimiento", url_establecimiento);

        editor.apply();
    }

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }


}
