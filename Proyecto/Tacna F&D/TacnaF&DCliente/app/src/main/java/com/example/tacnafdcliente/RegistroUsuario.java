package com.example.tacnafdcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;

public class RegistroUsuario extends AppCompatActivity {

    EditText Txtemail;
    EditText Txtclave;
    EditText Txtnombre;
    EditText Txtapellido;

    Button Btncrear;

    ResultSet Result_Set;
    AlertDialog Alert_Dialog;

    boolean Booleano = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        getSupportActionBar().hide();

        Txtemail = (EditText) findViewById(R.id.txtemail);
        Txtclave = (EditText) findViewById(R.id.txtclave);
        Txtnombre = (EditText) findViewById(R.id.txtnombre);
        Txtapellido = (EditText) findViewById(R.id.txtapellido);

        Btncrear = (Button) findViewById(R.id.btncrear);



        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(RegistroUsuario.this)
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Booleano = false;
                if (Txtemail.length() == 0 || Txtclave.length() == 0 || Txtnombre.length() == 0 || Txtapellido.length() == 0)
                {
                    if (Txtclave.length() == 0)
                    {
                        Txtclave.setError("Espacio Requerido");
                    }
                    else
                    {

                    }

                    if (Txtnombre.length() == 0)
                    {
                        Txtnombre.setError("Espacio Requerido");
                    }
                    else
                    {

                    }

                    if (Txtapellido.length() == 0)
                    {
                        Txtapellido.setError("Espacio Requerido");
                    }
                    else
                    {

                    }

                    if (Txtemail.length() == 0)
                    {
                        Txtemail.setError("Espacio Requerido");
                    }
                    else
                    {

                    }
                }
                else
                {
                    new BuscarUsuario (RegistroUsuario.this).execute(new String[]{"Buscar"});
                }


            }
        });
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

    public class BuscarUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        BuscarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConnectionDB().createStatement();
                Result_Set = stm2.executeQuery("Select * from Usuario_Cliente where Email='" + Txtemail.getText().toString() + "'");

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
        protected  void onPostExecute(Boolean result){


            try {

                if (Result_Set.next())
                {
                    Booleano = true;
                }
                else
                {

                }

            }catch (Exception e){



            }

            if (Booleano)
            {
                Txtemail.setError("Este correo ya esta siendo usado");
                Alert_Dialog.dismiss();
            }
            else
            {

                new RegistrarUsuario (RegistroUsuario.this).execute(new String[]{"Registrar"});
            }

            //

        }


    }


    public class RegistrarUsuario extends AsyncTask<String, Integer, Boolean> {


        private Context mContext2 = null;

        RegistrarUsuario(Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground(String... strings) {


            try {

                Statement stm = ConnectionDB().createStatement();
                stm.execute("INSERT INTO Usuario_Cliente(Email,Contrasena,Nombre,Apellido) values('" + Txtemail.getText().toString() + "','" + Txtclave.getText().toString()
                        + "','"+Txtnombre.getText().toString() + "','"+Txtapellido.getText().toString() + "')");

            }catch (Exception e){

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

            Toast.makeText(getApplicationContext(),"se registro con exito", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);



        }


    }
}
