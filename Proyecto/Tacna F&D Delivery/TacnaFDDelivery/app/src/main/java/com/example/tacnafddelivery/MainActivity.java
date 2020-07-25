package com.example.tacnafddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    EditText Txtemail;
    EditText Txtclave;
    Button Btnregistro;
    Button Btnlogin;

    ResultSet Result_Set;
    AlertDialog Alert_Dialog;

    TextView Lblrecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);getSupportActionBar().hide();

        String Id = GetFromSharedPreferences("ID");

        if (Id == null || Id.equals(""))
        {

            Txtemail = (EditText) findViewById(R.id.txtemail);
            Txtclave = (EditText) findViewById(R.id.txtclave);

            Btnregistro = (Button) findViewById(R.id.btnregistro);
            Btnlogin = (Button) findViewById(R.id.btnlogin);

            Alert_Dialog = new SpotsDialog.Builder()
                    .setContext(MainActivity.this)
                    .setMessage("Espere")
                    .setCancelable(false)
                    .build();

            Btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Txtemail.length() == 0 || Txtclave.length() == 0)
                    {
                        if (Txtemail.length() == 0)
                        {
                            Txtemail.setError("Ingrese un correo");
                        }
                        if (Txtclave.length() == 0)
                        {
                            Txtclave.setError("Ingrese una contraseña");
                        }
                    }
                    else
                    {

                        new BuscarUsuario (MainActivity.this).execute(new String[]{"Login"});
                    }
                }
            });

            Btnregistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegistroUsuario.class);
                    startActivity(intent);
                }
            });

        }
        else
        {

            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
            startActivity(intent);

        }

        Lblrecover = (TextView) findViewById(R.id.lblrecover);

        Lblrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecuperarContrasena_BuscarEmail.class);
                startActivity(intent);
            }
        });



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


    public class BuscarUsuario extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        BuscarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm = ConectarDB().createStatement();
                Result_Set = stm.executeQuery("Select * from Usuario_Repartidor " +
                        "where Email='" + Txtemail.getText().toString() + "' and Contrasena='" + Txtclave.getText().toString() + "'");

            }catch (Exception e){

            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute (Boolean result){

            Alert_Dialog.dismiss();

            try {

                if (Result_Set.next())
                {


                    final String ID = Result_Set.getString(1);
                    final String nombre = Result_Set.getString(4);
                    final String apellido = Result_Set.getString(5);
                    final String email = Result_Set.getString(2);
                    final String clave = Result_Set.getString(3);
                    final String url_foto = Result_Set.getString(6);
                    SaveLoginSharedPreferences(ID, email, clave, nombre, apellido, url_foto);
                    Intent intent = new Intent(getApplicationContext(),MenuPrincipal.class);


                    startActivity(intent);
                }
                else
                {
                    Txtemail.setError("Correo electronico o contraseña invalidos");
                    Txtclave.setError("Correo electronico o contraseña invalidos");
                }

            }catch (Exception e){

            }




        }


    }

    private void SaveLoginSharedPreferences(String ID, String email, String clave, String nombre, String apellido, String url_foto){
        SharedPreferences sharedPref = getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("email", email);
        editor.putString("clave", clave);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("url_foto", url_foto);

        editor.apply();
    }

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
