package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tacnafdcliente.Adaptador.ReseñasAdapter;
import com.example.tacnafdcliente.Model.MiCupon;
import com.example.tacnafdcliente.Model.Reseñas;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaResenas2 extends Fragment {

    public ListaResenas2() {
        // Required empty public constructor
    }

    String bid_establecimiento = "";
    String burl_imagen_logo = "";

    boolean Booleano = false;

    ImageView Img_Logo;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnprincipal;
    Button Btnruta;
    Button Btncupon;
    Button Btnmenu;
    Button Btneliminar_Resena;
    Button Btnmodificar_Resena;

    TextView Txtnombre;
    TextView Lbltotal;
    TextView Lblpuntuacion;

    RatingBar Ratingbar_Calificacion_Total;
    RatingBar Ratingbar_Calificacion;

    EditText Txtcomentario;


    ProgressBar Progress_Bar_1;
    ProgressBar Progress_Bar_2;
    ProgressBar Progress_Bar_3;
    ProgressBar Progress_Bar_4;
    ProgressBar Progress_Bar_5;

    int Contador1 = 0;
    int Contador2 = 0;
    int Contador3 = 0;
    int Contador4 = 0;
    int Contador5 = 0;

    ResultSet Result_Set_1;
    ResultSet Result_Set_2;
    ResultSet Result_Set_3;
    AlertDialog Alert_Dialog;

    private RecyclerView Recycler_View;
    private ReseñasAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<Reseñas> Items_Resena;

    List<MiCupon> Items_Cupon_Usuario;

    String Id_Usuario = "";

    Double Puntuacion_Total = 0.0;
    int Total_Resenas = 0;

    int Nro_Cupones = 0;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lista_resenas2, container, false);


        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eliminar Reseña");
        builder.setMessage("¿Esta seguro que desea eliminar su reseña? Usted perdera 1 punto");
        builder.setCancelable(false);
        builder.setPositiveButton("Si, estoy seguro", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new EliminarResena (getActivity()).execute(new String[]{"Eliminarresena"});

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialogInterface, int i) {

            }
        });


        Id_Usuario = GetFromSharedPreferences("ID");
        Nro_Cupones = GetNroCuponesFromSharedPreferences("NroCupones");

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_resenas) ;
        Progress_Bar_1 = (ProgressBar) v.findViewById(R.id.progress);
        Progress_Bar_2 = (ProgressBar) v.findViewById(R.id.progress2);
        Progress_Bar_3 = (ProgressBar) v.findViewById(R.id.progress3);
        Progress_Bar_4 = (ProgressBar) v.findViewById(R.id.progress4);
        Progress_Bar_5 = (ProgressBar) v.findViewById(R.id.progress5);

        Txtcomentario = (EditText) v.findViewById(R.id.txtcomentario);
        Ratingbar_Calificacion = (RatingBar) v.findViewById(R.id.rbcalificacion);
        Ratingbar_Calificacion_Total = (RatingBar) v.findViewById(R.id.ratingbarlistaresenas);
        Lblpuntuacion = (TextView) v.findViewById(R.id.lblpuntuacion);
        Lbltotal = (TextView) v.findViewById(R.id.lbltotal);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();



        bid_establecimiento = GetInfoFromSharedPreferences("ID");
        burl_imagen_logo = GetInfoFromSharedPreferences("Url_Imagen_Logo");


         Booleano = Boolean.valueOf(GetResenaFromSharedPreferences("Bandera_Resena"));




        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);

        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(GetInfoFromSharedPreferences("Nombre"));

        Lbltotal.setText(GetInfoFromSharedPreferences("Total_Resenas"));
        Lblpuntuacion.setText(GetInfoFromSharedPreferences("Puntuacion"));
        Ratingbar_Calificacion_Total.setRating(Float.parseFloat(GetInfoFromSharedPreferences("Puntuacion")));



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

        Btnprincipal = (Button)v.findViewById(R.id.btnprincipal);
        Btnprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
                transaction.commit();
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

        Btnruta = (Button) v.findViewById(R.id.btnruta);
        Btnruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RutaMapa rutaMapa = new RutaMapa();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, rutaMapa);
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

        Txtcomentario.setText(GetResenaFromSharedPreferences("Mi_Comentario"));
        Ratingbar_Calificacion.setRating(Float.parseFloat(GetResenaFromSharedPreferences("Mi_Puntuacion")));
        Ratingbar_Calificacion.setIsIndicator(true);


        Btneliminar_Resena = (Button) v.findViewById(R.id.btneliminarresena);
        Btneliminar_Resena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.show();



            }
        });

        Btnmodificar_Resena = (Button) v.findViewById(R.id.btnmodificarresena);
        Btnmodificar_Resena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ModificarResena modificarResena = new ModificarResena();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, modificarResena);
                transaction.commit();
            }
        });

        new ListarResenaPorEstablecimiento (getActivity()).execute(new String[]{"Listarresenas"});

        return v;
    }

    private void SaveNroCuponesSharedPreferences (int nrocupones){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Numero_Cupones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("NroCupones", nrocupones);

        editor.apply();
    }

    private int GetNroCuponesFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Numero_Cupones", Context.MODE_PRIVATE);
        return sharedPref.getInt(Key,0);
    }

    public Connection ConnectionDB(){

        Connection cnn = null;
        try {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");


        }catch (Exception e){

        }

        return cnn;
    }

    public class ListarResenaPorEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ListarResenaPorEstablecimiento (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {




            try {

                Statement stm2 = ConnectionDB().createStatement();
                Result_Set_1 = stm2.executeQuery("select r.ID_Resena,r.ID_Usuario_Cliente,u.Nombre+' '+u.Apellido as Nombre ,r.ID_Establecimiento," +
                        "r.Fecha,r.Descripcion,r.Calificacion from Resena r inner join Usuario_Cliente u on r.ID_Usuario_Cliente=u.ID_Usuario_Cliente " +
                        "where ID_Establecimiento=" + bid_establecimiento);

            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            RellenarLista();

            Alert_Dialog.dismiss();


        }


    }

    public void RellenarLista(){
        try {
            int Nro_Resenas = 0;

            Items_Resena = new ArrayList<>();

            while (Result_Set_1.next())
            {

                if (Id_Usuario.equals(Result_Set_1.getString(2)))
                {
                    Nro_Resenas++;
                }
                else
                {
                    Reseñas reseñas = new Reseñas();
                    reseñas.setID_Resena(Result_Set_1.getInt(1));
                    reseñas.setID_Usuario_Cliente(Result_Set_1.getString(3));
                    reseñas.setID_Establecimiento(Result_Set_1.getInt(4));
                    reseñas.setFecha(Result_Set_1.getDate(5));
                    reseñas.setDescripcion(Result_Set_1.getString(6));
                    reseñas.setCalificacion(Result_Set_1.getDouble(7));

                    Items_Resena.add(reseñas);
                    Nro_Resenas = Items_Resena.size();
                }

                if (Result_Set_1.getDouble(7) <= 1.0)
                {
                    Contador1++;
                }
                else if (Result_Set_1.getDouble(7) <= 2.0)
                {
                    Contador2++;
                }
                else if (Result_Set_1.getDouble(7) <= 3.0)
                {
                    Contador3++;
                }
                else if (Result_Set_1.getDouble(7) <= 4.0)
                {
                    Contador4++;
                }
                else if (Result_Set_1.getDouble(7) <= 5.0)
                {
                    Contador5++;
                }


            }


            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new ReseñasAdapter(Items_Resena,getActivity());


            Recycler_View.setAdapter(Adaptador);


            Progress_Bar_1.setMax(Nro_Resenas);
            Progress_Bar_2.setMax(Nro_Resenas);
            Progress_Bar_3.setMax(Nro_Resenas);
            Progress_Bar_4.setMax(Nro_Resenas);
            Progress_Bar_5.setMax(Nro_Resenas);

            Progress_Bar_1.setProgress(Contador1);
            Progress_Bar_2.setProgress(Contador2);
            Progress_Bar_3.setProgress(Contador3);
            Progress_Bar_4.setProgress(Contador4);
            Progress_Bar_5.setProgress(Contador5);




        }catch (Exception e){
            Log.e("Error", e.toString());
        }
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    public class EliminarResena extends AsyncTask <String, Integer, Boolean> {


        private Context mContext2 = null;

        EliminarResena (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm = ConnectionDB().createStatement();
                stm.execute("Delete from Resena where ID_Establecimiento=" + bid_establecimiento + " and ID_Usuario_Cliente=" + Id_Usuario);

                Statement stm3 = ConnectionDB().createStatement();
                Result_Set_2 = stm3.executeQuery("select * from Resena where ID_Establecimiento=" + bid_establecimiento);

            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){


            new ActualizarEstablecimiento (getActivity()).execute(new String[]{"Act"});
            //mDialog.dismiss();




        }


    }

    public class ActualizarEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext4 = null;

        ActualizarEstablecimiento (Context context4){
            mContext4 = context4;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                while (Result_Set_2.next())
                {

                    Puntuacion_Total = Puntuacion_Total+Result_Set_2.getDouble(5);
                    Total_Resenas++;
                }
                if (Total_Resenas == 0)
                {
                    Puntuacion_Total = 0.0;
                }
                else
                {
                    Puntuacion_Total = Puntuacion_Total / (Total_Resenas * 1.0);
                }


                Statement stm=ConnectionDB().createStatement();
                stm.execute("Update Establecimiento set TotalReseñas=" + Total_Resenas + ", Puntuacion=" + Puntuacion_Total +
                        " where ID_Establecimiento="+bid_establecimiento);



            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){
            if (Nro_Cupones == 0)
            {

                new ListarCuponUsuarioPorEstado (getActivity()).execute(new String[]{"Actcuponusuario"});

            }
            else
            {
                Alert_Dialog.dismiss();

                Nro_Cupones = Nro_Cupones-1;
                SaveNroCuponesSharedPreferences(Nro_Cupones);

                SaveInfoSharedPreferences(GetInfoFromSharedPreferences("ID"), GetInfoFromSharedPreferences("Nombre"), GetInfoFromSharedPreferences("Distrito"),
                        GetInfoFromSharedPreferences("Categoria"), GetInfoFromSharedPreferences("Direccion"), GetInfoFromSharedPreferences("Telefono"),
                        GetInfoFromSharedPreferences("Descripcion"), GetInfoFromSharedPreferences("Capacidad"), String.valueOf(Total_Resenas),
                        String.valueOf(Puntuacion_Total), GetInfoFromSharedPreferences("Url_Imagen_Logo"), GetInfoFromSharedPreferences("Url_Imagen_Documento"),
                        GetInfoFromSharedPreferences("Punto_Geografico"), GetInfoFromSharedPreferences("Estado"));
                SaveResenaSharedPreferences(false, "", (float) 0.0);

                ListaResenas listaResenas = new ListaResenas();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaResenas);
                transaction.commit();
            }



        }


    }

    public class ListarCuponUsuarioPorEstado extends AsyncTask <String, Integer, Boolean> {


        private Context mContext5 = null;

        ListarCuponUsuarioPorEstado (Context context5){
            mContext5 = context5;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm4 = ConnectionDB().createStatement();
                Result_Set_3 = stm4.executeQuery("select * from Cupon_Usuario where ID_Usuario_Cliente=" + Id_Usuario + " and Estado='Activo'");

            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            try {

                Items_Cupon_Usuario = new ArrayList<>();

                while (Result_Set_3.next())
                {
                    MiCupon miCupon = new MiCupon();
                    miCupon.setID_Cupon_Usuario(Result_Set_3.getInt(1));
                    miCupon.setID_Cupon(Result_Set_3.getInt(2));
                    miCupon.setID_Usuario_Cliente(Result_Set_3.getInt(3));
                    miCupon.setFecha(Result_Set_3.getDate(4));
                    miCupon.setEstado(Result_Set_3.getString(5));
                    Items_Cupon_Usuario.add(miCupon);
                }

                if (Items_Cupon_Usuario.size() == 0)
                {

                    Alert_Dialog.dismiss();

                    Nro_Cupones=Nro_Cupones-1;
                    //savenrocuponesSharedPreferences(nrocupones);

                    //Nro_Cupones = 0;
                    SaveNroCuponesSharedPreferences(Nro_Cupones);

                    SaveInfoSharedPreferences(GetInfoFromSharedPreferences("ID"), GetInfoFromSharedPreferences("Nombre"), GetInfoFromSharedPreferences("Distrito"),
                            GetInfoFromSharedPreferences("Categoria"), GetInfoFromSharedPreferences("Direccion"), GetInfoFromSharedPreferences("Telefono"),
                            GetInfoFromSharedPreferences("Descripcion"), GetInfoFromSharedPreferences("Capacidad"), String.valueOf(Total_Resenas),
                            String.valueOf(Puntuacion_Total), GetInfoFromSharedPreferences("Url_Imagen_Logo"), GetInfoFromSharedPreferences("Url_Imagen_Documento"),
                            GetInfoFromSharedPreferences("Punto_Geografico"), GetInfoFromSharedPreferences("Estado"));
                    SaveResenaSharedPreferences(false, "", (float) 0.0);


                    ListaResenas listaResenas = new ListaResenas();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.contenedorfragment, listaResenas);
                    transaction.commit();



                }
                else
                {
                    new EliminarCuponUsuario (getActivity()).execute(new String[]{"borrarcuponusuario"});
                }

            }catch (Exception e){
                Log.e("Error", e.toString());
            }



            //mDialog.dismiss();




        }


    }

    public class EliminarCuponUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext6 = null;

        EliminarCuponUsuario (Context context6){
            mContext6 = context6;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                int cantidadcuponesusuario = Items_Cupon_Usuario.size()-1;
                int ultimoid = Items_Cupon_Usuario.get(cantidadcuponesusuario).getID_Cupon_Usuario();

                Statement stm = ConnectionDB().createStatement();
                stm.execute("Delete from Cupon_Usuario where ID_Cupon_Usuario=" + ultimoid);


            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            //mDialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){


            //new myasynctask4(getActivity()).execute(new String[]{"Act"});
            Alert_Dialog.dismiss();

            Nro_Cupones = 4;
            SaveNroCuponesSharedPreferences(Nro_Cupones);

            SaveInfoSharedPreferences(GetInfoFromSharedPreferences("ID"), GetInfoFromSharedPreferences("Nombre"), GetInfoFromSharedPreferences("Distrito"),
                    GetInfoFromSharedPreferences("Categoria"), GetInfoFromSharedPreferences("Direccion"), GetInfoFromSharedPreferences("Telefono"),
                    GetInfoFromSharedPreferences("Descripcion"), GetInfoFromSharedPreferences("Capacidad"), String.valueOf(Total_Resenas),
                    String.valueOf(Puntuacion_Total), GetInfoFromSharedPreferences("Url_Imagen_Logo"), GetInfoFromSharedPreferences("Url_Imagen_Documento"),
                    GetInfoFromSharedPreferences("Punto_Geografico"), GetInfoFromSharedPreferences("Estado"));
            SaveResenaSharedPreferences(false, "", (float) 0.0);

            ListaResenas listaResenas = new ListaResenas();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaResenas);
            transaction.commit();




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

    private void SaveResenaSharedPreferences(Boolean Bandera_Resena, String Mi_Comentario, Float Mi_Puntuacion){

        SharedPreferences sharedPref = getActivity().getSharedPreferences("info_resena", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(Bandera_Resena){
            editor.putString("Bandera_Resena", "true");
        }
        else
        {
            editor.putString("Bandera_Resena", "false");
        }
        editor.putString("Mi_Comentario", Mi_Comentario);
        editor.putString("Mi_Puntuacion", String.valueOf(Mi_Puntuacion));
        editor.apply();
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
