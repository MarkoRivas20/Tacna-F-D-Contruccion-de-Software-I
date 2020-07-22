package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdbusiness.Adaptador.ListaEstablecimientoAdapter;
import com.example.tacnafdbusiness.Model.Establecimiento;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaEstablecimientos extends Fragment {


    public ListaEstablecimientos() {
        // Required empty public constructor
    }

    Button Btnperfil;
    Button Btnestablecimiento;

    private RecyclerView Recycler_View;
    private ListaEstablecimientoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    ResultSet Result_Set;
    AlertDialog Alert_Dialog;

    String Id_Usuario = "";

    List<Establecimiento> Items;

    ArrayList<Establecimiento> Lista_Filtrada;

    EditText Txtbuscar;


    boolean Booleano = false;

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
    public View onCreateView (LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_establecimientos, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaEstablecimiento) ;



        Id_Usuario = GetFromSharedPreferences("ID");

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


        Btnestablecimiento = (Button) v.findViewById(R.id.btnagregarestablecimiento);
        Btnestablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroEstablecimiento registroEstablecimiento = new RegistroEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, registroEstablecimiento);
                transaction.commit();
            }
        });


        //new myasynctask(getActivity()).execute(new String[]{"Buscar"});


        RellenarLista();





        Adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Booleano)
                {
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
                }
                else
                {
                    Toast.makeText(getActivity(),"ID: " + Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getID_Establecimiento(),Toast.LENGTH_SHORT).show();
                    ID_Establecimiento = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getID_Establecimiento());
                    nombre = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getNombre();
                    distrito = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDistrito();
                    categoria = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getCategoria();
                    direccion = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDireccion();
                    telefono = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getTelefono();
                    descripcion = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion();
                    capacidad = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getCapacidad());
                    totalresenas = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getTotalResenas());
                    puntuacion = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getPuntuacion());
                    url_imagen_logo = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen_Logo();
                    url_imagen_documento = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen_Documento();
                    puntogeofrafico = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getPuntoGeografico();
                    estado = Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getEstado();
                }

                SaveInfoSharedPreferences(ID_Establecimiento,nombre,distrito,categoria,direccion,telefono,descripcion,
                        capacidad,totalresenas,puntuacion,url_imagen_logo,url_imagen_documento,puntogeofrafico,estado);

                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();




            }
        });
        Recycler_View.setAdapter(Adaptador);




        Txtbuscar = (EditText) v.findViewById(R.id.txtbuscar);
        Txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filter(s.toString());
            }
        });

        return v;
    }

    private void filter (String text){
        Lista_Filtrada = new ArrayList<>();

        for (Establecimiento item:Items)
        {
            if(item.getNombre().toLowerCase().contains(text.toLowerCase()))
            {
                Lista_Filtrada.add(item);
                Booleano = true;
            }
            else
            {

            }
        }

        Adaptador.filterlist(Lista_Filtrada);
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

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    public void RellenarLista(){


        try {

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Establecimiento where ID_Usuario_Propietario=" + Id_Usuario);

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

            Adaptador = new ListaEstablecimientoAdapter(Items, getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
        }


    }
    private void SaveInfoSharedPreferences(String ID, String Nombre, String Distrito, String Categoria, String Direccion, String Telefono,
                                           String Descripcion, String Capacidad, String Total_Resenas, String Puntuacion, String Url_Imagen_Logo,
                                           String Url_Imagen_Documento, String Punto_Geografico, String Estado){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("Nombre", Nombre);
        editor.putString("Distrito", Distrito);
        editor.putString("Categoria", Categoria);
        editor.putString("Direccion", Direccion);
        editor.putString("Telefono", Telefono);
        editor.putString("Descripcion", Descripcion);
        editor.putString("Capacidad", Capacidad);
        editor.putString("Total_Resenas", Total_Resenas);
        editor.putString("Puntuacion", Puntuacion);
        editor.putString("Url_Imagen_Logo", Url_Imagen_Logo);
        editor.putString("Url_Imagen_Documento", Url_Imagen_Documento);
        editor.putString("Punto_Geografico", Punto_Geografico);
        editor.putString("Estado", Estado);
        editor.apply();
    }


}
