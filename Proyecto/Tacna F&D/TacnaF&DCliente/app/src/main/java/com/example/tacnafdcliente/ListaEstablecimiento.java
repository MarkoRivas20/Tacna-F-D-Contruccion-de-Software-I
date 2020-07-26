package com.example.tacnafdcliente;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tacnafdcliente.Adaptador.ListaEstablecimientoAdapter;
import com.example.tacnafdcliente.Model.Establecimiento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListaEstablecimiento extends Fragment {

    public ListaEstablecimiento() {
        // Required empty public constructor
    }
    Spinner Spinnercategoria;
    Spinner Spinnerdistrito;

    String Nombre = "";
    String Distrito = "";
    String Categoria = "";
    String Capacidad = "";

    EditText Txtnombre;
    EditText Txtcapacidad;

    ResultSet Result_Set;

    private RecyclerView Recycler_View;
    private ListaEstablecimientoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<Establecimiento> Items;

    ArrayList<Establecimiento> Lista_Filtrada;

    Button Btnperfil;
    Button Btnprincipal;

    boolean Boolean = false;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_establecimiento, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaEstablecimiento) ;

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


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {


                        BuscarEstablecimiento buscarEstablecimiento = new BuscarEstablecimiento();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedorfragment, buscarEstablecimiento);
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

        Txtnombre = (EditText)v.findViewById(R.id.txtnombre);
        Txtcapacidad = (EditText)v.findViewById(R.id.txtcapacidad);

        Nombre = GetSearchFromSharedPreferences("Nombre");
        if (GetSearchFromSharedPreferences("Distrito").equals("Seleccione un Distrito"))
        {
            Distrito = "";
        }
        else
        {
            Distrito = GetSearchFromSharedPreferences("Distrito");
        }

        if (GetSearchFromSharedPreferences("Categoria").equals("Seleccione una Categoria"))
        {
            Categoria = "";
        }
        else
        {
            Categoria = GetSearchFromSharedPreferences("Categoria");
        }

        if (GetSearchFromSharedPreferences("Capacidad").equals("") || GetSearchFromSharedPreferences("Capacidad").equals("0"))
        {
            Capacidad = "0";
        }
        else
        {
            Capacidad = GetSearchFromSharedPreferences("Capacidad");
            Txtcapacidad.setText(Capacidad);
        }
        Txtnombre.setText(Nombre);



        final String[] categorias = {"Seleccione una Categoria","Restaurante","Cafeteria","Bar"};
        Spinnercategoria = (Spinner) v.findViewById(R.id.spinnercategoria);

        Spinnercategoria.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorias));

        final String[] distritos = {"Seleccione un Distrito","Tacna","Alto del Alianza","Calana","Pachia","Palca","Pocollay","Ciudad Nueva"};
        Spinnerdistrito = (Spinner)v.findViewById(R.id.spinnerdistrito);
        Spinnerdistrito.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, distritos));

        for (int i=0; i<categorias.length; i++)
        {
            if ((GetSearchFromSharedPreferences("Categoria")).equals(categorias[i]))
            {
                Spinnercategoria.setSelection(i);
                break;
            }
            else
            {

            }
        }
        for (int i=0; i<distritos.length; i++)
        {
            String b = GetSearchFromSharedPreferences("Distrito");
            if (b.equals(distritos[i]))
            {
                Spinnerdistrito.setSelection(i);
                break;
            }
            else
            {

            }
        }




        RellenarLista();
        Adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveInfoSharedPreferences(String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getID_Establecimiento()),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getNombre(), Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDistrito(),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getCategoria(), Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDireccion(),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getTelefono(), Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion(),
                        String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getCapacidad()),
                        String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getTotalResenas()),
                        String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getPuntuacion()),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen_Logo(),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen_Documento(),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getPuntoGeografico(),
                        Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getEstado());


                SaveSearchSharedPreferences(Nombre, Distrito, Categoria, Capacidad);

                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();

                Toast.makeText(getActivity(), String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getID_Establecimiento()), Toast.LENGTH_SHORT).show();

            }
        });
        Recycler_View.setAdapter(Adaptador);

        filter(Nombre, Capacidad, Categoria, Distrito);

        Txtnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                Nombre = s.toString();
                filter(Nombre, Capacidad, Categoria, Distrito);
            }
        });

        Txtcapacidad.addTextChangedListener (new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                Capacidad = s.toString();
                if(s.toString().equals("") || Txtcapacidad.length() == 0)
                {
                    Capacidad = "0";
                }
                else
                {

                }
                filter(Nombre, Capacidad, Categoria, Distrito);
            }
        });

        Spinnercategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                Categoria = categorias[position];
                if(Categoria.equals("Seleccione una Categoria"))
                {
                    Categoria = "";
                }
                else
                {
                    Categoria = categorias[position];
                }

                filter(Nombre, Capacidad, Categoria, Distrito);
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

        Spinnerdistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                Distrito = distritos[position];

                if(Distrito.equals("Seleccione un Distrito"))
                {
                    Distrito = "";
                }
                else
                {
                    Distrito = distritos[position];
                }

                filter(Nombre, Capacidad, Categoria, Distrito);
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });





        return v;
    }

    private void filter (String nombre, String capacidad, String categoria, String distrito){
        Lista_Filtrada = new ArrayList<>();

        for (Establecimiento item:Items)
        {
            if (item.getNombre().toLowerCase().contains(nombre.toLowerCase())
                    && Integer.parseInt(capacidad)<=item.getCapacidad()
                    && item.getCategoria().contains(categoria)
                    && item.getDistrito().contains(distrito))
            {
                Lista_Filtrada.add(item);
                Boolean = true;
            }
            else
            {

            }
        }

        Adaptador.filterlist(Lista_Filtrada);
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

    public void RellenarLista(){


        try {

            Statement stm2 = ConnectionDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Establecimiento where Estado='Activo'");

            Items = new ArrayList<>();

            while (Result_Set.next())
            {
                Establecimiento establecimiento = new Establecimiento();
                establecimiento.setID_Establecimiento(Result_Set.getInt(1));
                establecimiento.setID_Usuario_Propietario(Result_Set.getInt(2));
                establecimiento.setDistrito(Result_Set.getString(4));
                establecimiento.setCategoria(Result_Set.getString(5));
                establecimiento.setTelefono(Result_Set.getString(7));
                establecimiento.setDescripcion(Result_Set.getString(8));
                establecimiento.setCapacidad(Result_Set.getInt(9));
                establecimiento.setPuntoGeografico(Result_Set.getString(14));
                establecimiento.setUrl_Imagen_Logo(Result_Set.getString(12));
                establecimiento.setUrl_Imagen_Documento(Result_Set.getString(13));
                establecimiento.setNombre(Result_Set.getString(3));
                establecimiento.setDireccion(Result_Set.getString(6));
                establecimiento.setTotalResenas(Result_Set.getInt(10));
                establecimiento.setPuntuacion(Result_Set.getDouble(11));
                establecimiento.setEstado(Result_Set.getString(15));

                Items.add(establecimiento);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new ListaEstablecimientoAdapter(Items,getActivity());




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
    private void SaveSearchSharedPreferences(String Nombre, String Distrito, String Categoria, String Capacidad){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_search", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Nombre", Nombre);
        editor.putString("Distrito", Distrito);
        editor.putString("Categoria", Categoria);
        editor.putString("Capacidad", Capacidad);
        editor.apply();
    }

    private String GetSearchFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_search", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
