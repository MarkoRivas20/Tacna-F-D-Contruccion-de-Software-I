package com.example.tacnafdbusiness;

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
    EditText Txtcelular;
    EditText Txtruc;
    EditText Txtcodigo_Paypal;

    Button Btncrear;

    ResultSet Result_Set;
    AlertDialog Alert_Dialog;
    int Contador = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        getSupportActionBar().hide();


        Txtemail = (EditText) findViewById(R.id.txtemail);
        Txtclave = (EditText) findViewById(R.id.txtclave);
        Txtnombre = (EditText) findViewById(R.id.txtnombre);
        Txtapellido = (EditText) findViewById(R.id.txtapellido);
        Txtcelular = (EditText) findViewById(R.id.txtcelular);
        Txtruc = (EditText) findViewById(R.id.txtruc);
        Txtcodigo_Paypal = (EditText) findViewById(R.id.txtcodigopaypal);

        Btncrear = (Button) findViewById(R.id.btncrear);



        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(RegistroUsuario.this)
                .setMessage("Espere")
                .setCancelable(false)
                .build();


        Btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contador = 0;

                if (Txtemail.length() == 0 || Txtclave.length() == 0 || Txtnombre.length() == 0
                        || Txtapellido.length() == 0 || Txtcelular.length() == 0 || Txtruc.length() == 0 || Txtcodigo_Paypal.length() == 0)
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
                    if (Txtcelular.length() == 0)
                    {
                        Txtcelular.setError("Espacio Requerido");
                    }
                    else
                    {

                    }
                    if (Txtruc.length() == 0)
                    {
                        Txtruc.setError("Espacio Requerido");
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
                    if (Txtcodigo_Paypal.length() == 0)
                    {
                        Txtcodigo_Paypal.setError("Espacio Requerido");
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

    public class BuscarUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        BuscarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConectarDB().createStatement();
                Result_Set = stm2.executeQuery("Select * from Usuario_Propietario where Email='" + Txtemail.getText().toString() + "'");

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


            try {

                if (Result_Set.next())
                {
                    Contador=1;
                }
                else
                {

                }

            }catch (Exception e){

            }

            if (Contador != 0)
            {
                Txtemail.setError("Este correo ya esta siendo usado");
                Alert_Dialog.dismiss();
            }
            else
            {

                new RegistrarUsuario (RegistroUsuario.this).execute(new String[]{"Registrar"});
            }



        }


    }


    public class RegistrarUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext2 = null;

        RegistrarUsuario (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


                try {

                    Statement stm = ConectarDB().createStatement();
                    stm.execute("INSERT INTO Usuario_Propietario(Email,Contrasena,Nombre,Apellido,RUC,Celular,Codigo_PayPal) " +
                            "values('" + Txtemail.getText().toString() + "','" + Txtclave.getText().toString() + "','" + Txtnombre.getText().toString()
                            + "','" + Txtapellido.getText().toString() + "','" + Txtruc.getText().toString() + "','" + Txtcelular.getText().toString()
                            + "','" + Txtcodigo_Paypal.getText().toString() + "')");

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