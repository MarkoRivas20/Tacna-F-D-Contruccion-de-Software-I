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
import android.widget.Button;
import android.widget.EditText;

import com.example.tacnafdcliente.Adaptador.MiCuponAdapter;
import com.example.tacnafdcliente.Model.MiCupon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListaMiCupon extends Fragment {


    public ListaMiCupon() {
        // Required empty public constructor
    }

    private RecyclerView Recycler_View;
    private MiCuponAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<MiCupon> Items;

    ResultSet Result_Set;

    String Id_Usuario = "";

    EditText Txtbuscar;

    ArrayList<MiCupon> Lista_Filtrada;

    boolean Booleano = false;

    String Id_Cupon_Usuario = "";
    String Id_Cupon = "";
    String Nombre = "";
    String Fecha = "";
    String Titulo = "";
    String Url_Imagen = "";
    String Descripcion = "";
    String Fecha_Inicio = "";
    String Fecha_Final = "";

    Button Btnatras;
    Button Btnperfil;
    Button Btnestablecimiento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lista_mi_cupon, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_MisCupones);

        Id_Usuario = GetFromSharedPreferences("ID");

        Txtbuscar = (EditText) v.findViewById(R.id.txtbuscar);
        Txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                filter(s.toString());
            }
        });



        BuscarMisCupones();

        Adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Booleano)
                {
                    Id_Cupon_Usuario = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getID_Cupon_Usuario());
                    Id_Cupon = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getID_Cupon());
                    Nombre = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getNombre());
                    Fecha = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getFecha());
                    Titulo = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getTitulo());
                    Url_Imagen = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen());
                    Descripcion = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion());
                    Fecha_Inicio = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getFecha_Inicio());
                    Fecha_Final = String.valueOf(Items.get(Recycler_View.getChildAdapterPosition(v)).getFecha_Final());
                }
                else
                {
                    Id_Cupon_Usuario = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getID_Cupon_Usuario());
                    Id_Cupon = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getID_Cupon());
                    Nombre = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getNombre());
                    Fecha = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getFecha());
                    Titulo = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getTitulo());
                    Url_Imagen = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getUrl_Imagen());
                    Descripcion = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getDescripcion());
                    Fecha_Inicio = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getFecha_Inicio());
                    Fecha_Final = String.valueOf(Lista_Filtrada.get(Recycler_View.getChildAdapterPosition(v)).getFecha_Final());
                }

                Bundle bundle = new Bundle();
                bundle.putString("ID_Cupon_Usuario", Id_Cupon_Usuario);
                bundle.putString("ID_Cupon", Id_Cupon);
                bundle.putString("Nombre", Nombre);
                bundle.putString("Fecha", Fecha);
                bundle.putString("Titulo", Titulo);
                bundle.putString("Url_Imagen", Url_Imagen);
                bundle.putString("Descripcion", Descripcion);
                bundle.putString("Fecha_Inicio", Fecha_Inicio);
                bundle.putString("Fecha_Final", Fecha_Final);

                DetalleCupon detalleCupon = new DetalleCupon();
                detalleCupon.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, detalleCupon);
                transaction.commit();
            }
        });

        Recycler_View.setAdapter(Adaptador);


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


        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
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

        Btnestablecimiento = (Button) v.findViewById(R.id.btnestablecimientos);
        Btnestablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarEstablecimiento buscarEstablecimiento = new BuscarEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, buscarEstablecimiento);
                transaction.commit();
            }
        });

        return v;
    }

    private void filter (String text){
        Lista_Filtrada = new ArrayList<>();

        for (MiCupon item:Items)
        {
            if (item.getNombre().toLowerCase().contains(text.toLowerCase()))
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

    public void BuscarMisCupones(){

        try {

            Statement stm2=ConnectionDB().createStatement();
            Result_Set=stm2.executeQuery("select cu.ID_Cupon_Usuario,cu.ID_Cupon,e.Nombre,c.Titulo,c.Url_Imagen,c.Descripcion,c.Fecha_Inicio,c.Fecha_Final,cu.Fecha " +
                    "from Cupon_Usuario cu inner join Cupon c on cu.ID_Cupon=c.ID_Cupon inner join Establecimiento e on c.ID_Establecimiento=e.ID_Establecimiento " +
                    "where cu.ID_Usuario_Cliente=" + Id_Usuario + " and cu.Estado='Activo'");

            Items = new ArrayList<>();

            while (Result_Set.next())
            {
                MiCupon miCupon = new MiCupon();
                miCupon.setID_Cupon_Usuario(Result_Set.getInt(1));
                miCupon.setID_Cupon(Result_Set.getInt(2));
                miCupon.setNombre(Result_Set.getString(3));
                miCupon.setTitulo(Result_Set.getString(4));
                miCupon.setUrl_Imagen(Result_Set.getString(5));
                miCupon.setDescripcion(Result_Set.getString(6));
                miCupon.setFecha_Inicio(Result_Set.getDate(7));
                miCupon.setFecha_Final(Result_Set.getDate(8));
                miCupon.setFecha(Result_Set.getDate(9));
                Items.add(miCupon);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new MiCuponAdapter(Items,getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
