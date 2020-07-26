package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;


public class ModificarResena extends Fragment {


    public ModificarResena() {
        // Required empty public constructor
    }

    String bid_establecimiento = "";

    boolean Booleano = false;

    AlertDialog Alert_Dialog;

    Button Btnperfil;
    Button Btnprincipal;
    Button Btnmodificar;

    TextView Lblnombre;

    RatingBar Ratingbar_Calificacion;
    EditText Txtcomentario;

    String Id_Usuario = "";
    String Fecha_Actual = "";

    ResultSet Result_Set_2;

    Double Puntuacion_Total = 0.0;
    int Total_Resenas = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_modificar_resena, container, false);

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Fecha_Actual = simpleDateFormat.format(new Date());

        Id_Usuario = GetFromSharedPreferences("ID");

        Lblnombre = (TextView) v.findViewById(R.id.lblnombre);
        Ratingbar_Calificacion = (RatingBar) v.findViewById(R.id.rbcalificacion);
        Txtcomentario = (EditText) v.findViewById(R.id.txtcomentario);
        Btnmodificar = (Button) v.findViewById(R.id.btnmodificarresena);


        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();



        bid_establecimiento = GetInfoFromSharedPreferences("ID");

        Booleano = Boolean.valueOf(GetResenaFromSharedPreferences("Bandera_Resena"));


        Ratingbar_Calificacion.setRating(Float.parseFloat(GetResenaFromSharedPreferences("Mi_Puntuacion")));
        Txtcomentario.setText(GetResenaFromSharedPreferences("Mi_Comentario"));
        Lblnombre.setText(GetInfoFromSharedPreferences("Nombre"));


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        ListaResenas2 listaResenas2 = new ListaResenas2();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedorfragment, listaResenas2);
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

        Btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Txtcomentario.length() == 0 || Ratingbar_Calificacion.getRating() == 0.0)
                {
                    if (Txtcomentario.length() == 0){
                        Txtcomentario.setError("Campo Requerido");
                    }
                    else
                    {

                    }

                    if (Ratingbar_Calificacion.getRating() == 0.0)
                    {
                        Toast.makeText(getActivity(),"Puntuacion Requerida", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                }
                else
                {
                    new ActualizarResena (getActivity()).execute(new String[]{"Act2"});
                }

            }
        });


        return v;
    }
    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
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

    public class ActualizarResena extends AsyncTask <String, Integer, Boolean> {


        private Context mContext2 = null;

        ActualizarResena (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm = ConnectionDB().createStatement();
                stm.execute("Update Resena set Descripcion='" + Txtcomentario.getText().toString() +
                        "', Calificacion=" + Ratingbar_Calificacion.getRating() + ", Fecha=Convert(date,'" + Fecha_Actual
                        +"',103) where ID_Establecimiento="+bid_establecimiento+" and ID_Usuario_Cliente="+Id_Usuario);

                Statement stm3 = ConnectionDB().createStatement();
                Result_Set_2 = stm3.executeQuery("select * from Resena where ID_Establecimiento="+bid_establecimiento);

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






            //mDialog.dismiss();

            new ActualizarEstablecimiento (getActivity()).execute(new String[]{"Act4"});


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

                    Puntuacion_Total = Puntuacion_Total + Result_Set_2.getDouble(5);
                    Total_Resenas++;
                }
                if (Total_Resenas==0)
                {
                    Puntuacion_Total = 0.0;
                }
                else
                {
                    Puntuacion_Total = Puntuacion_Total / (Total_Resenas * 1.0);
                }


                Statement stm = ConnectionDB().createStatement();
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

            Alert_Dialog.dismiss();

            DecimalFormat df = new DecimalFormat("#.0");
            SaveInfoSharedPreferences(GetInfoFromSharedPreferences("ID"), GetInfoFromSharedPreferences("Nombre"), GetInfoFromSharedPreferences("Distrito"),
                    GetInfoFromSharedPreferences("Categoria"), GetInfoFromSharedPreferences("Direccion"), GetInfoFromSharedPreferences("Telefono"),
                    GetInfoFromSharedPreferences("Descripcion"), GetInfoFromSharedPreferences("Capacidad"), String.valueOf(Total_Resenas),
                    String.valueOf(df.format(Puntuacion_Total)), GetInfoFromSharedPreferences("Url_Imagen_Logo"), GetInfoFromSharedPreferences("Url_Imagen_Documento"),
                    GetInfoFromSharedPreferences("Punto_Geografico"), GetInfoFromSharedPreferences("Estado"));

            SaveResenaSharedPreferences(true, Txtcomentario.getText().toString(), Ratingbar_Calificacion.getRating());


            ListaResenas2 listaResenas2 = new ListaResenas2();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaResenas2);
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
