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

    AlertDialog Alert_Dialog;

    Button Btnperfil;
    Button Btnprincipal;
    Button Btnmodificar;

    final Bundle bundle2=new Bundle();

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

        Bundle bundle = getArguments();

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



        bundle2.putString("id_establecimiento", bid_establecimiento);
        bundle2.putString("nombre", bnombre);
        bundle2.putString("distrito", bdistrito);
        bundle2.putString("categoria", bcategoria);
        bundle2.putString("direccion", bdireccion);
        bundle2.putString("telefono", btelefono);
        bundle2.putString("descripcion", bdescripcion);
        bundle2.putString("capacidad", bcapacidad);
        bundle2.putString("totalresenas", btotalresenas);
        bundle2.putString("puntuacion", bpuntuacion);
        bundle2.putString("url_imagen_logo", burl_imagen_logo);
        bundle2.putString("puntogeografico", bpuntogeografico);
        bundle2.putString("estado" ,bestado);
        bundle2.putString("nombreb", Nombre);
        bundle2.putString("distritob", Distrito);
        bundle2.putString("categoriab", Categoria);
        bundle2.putString("capacidadb", Capacidad);
        bundle2.putBoolean("banderaresena", Booleano);
        bundle2.putString("micomentario", bundle.getString("micomentario"));
        bundle2.putFloat("mipuntuacion", bundle.getFloat("mipuntuacion"));

        Ratingbar_Calificacion.setRating(bundle.getFloat("mipuntuacion"));
        Txtcomentario.setText(bundle.getString("micomentario"));
        Lblnombre.setText(bundle.getString("nombreestablecimiento"));


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

                        listaResenas2.setArguments(bundle2);
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
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");


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
                        "', Calificacion=" + Ratingbar_Calificacion.getRating() + ", Fecha='" + Fecha_Actual
                        +"' where ID_Establecimiento="+bid_establecimiento+" and ID_Usuario_Cliente="+Id_Usuario);

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
            bundle2.putString("totalresenas", String.valueOf(Total_Resenas));
            bundle2.putString("puntuacion", String.valueOf(df.format(Puntuacion_Total)));
            bundle2.putString("micomentario", Txtcomentario.getText().toString());
            bundle2.putFloat("mipuntuacion", Ratingbar_Calificacion.getRating());


            ListaResenas2 listaResenas2 = new ListaResenas2();

            listaResenas2.setArguments(bundle2);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaResenas2);
            transaction.commit();


        }


    }
}
