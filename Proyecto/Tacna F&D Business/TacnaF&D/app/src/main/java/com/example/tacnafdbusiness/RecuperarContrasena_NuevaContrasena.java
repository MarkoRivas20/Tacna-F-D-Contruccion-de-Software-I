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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;

public class RecuperarContrasena_NuevaContrasena extends AppCompatActivity {

    Button Btnclave;
    EditText Txtnueva_Clave;
    EditText Txtnueva_Clave_Repite;
    String Id_Usuario;
    AlertDialog Alert_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena__nueva_contrasena);


        getSupportActionBar().hide();

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(RecuperarContrasena_NuevaContrasena.this)
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Bundle datos = this.getIntent().getExtras();
        Id_Usuario = datos.getString("idusuario");

        Txtnueva_Clave = (EditText) findViewById(R.id.txtnuevaclave);
        Txtnueva_Clave_Repite = (EditText) findViewById(R.id.txtnuevaclave2);
        Btnclave = (Button) findViewById(R.id.btnnuevaclave);

        Btnclave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txtnueva_Clave.length() == 0 || Txtnueva_Clave_Repite.length() == 0){


                    if (Txtnueva_Clave.length() == 0){
                        Txtnueva_Clave.setError("Campo requerido");
                    }
                    else
                    {

                    }

                    if (Txtnueva_Clave_Repite.length() == 0){
                        Txtnueva_Clave_Repite.setError("Campo requerido");
                    }
                    else
                    {

                    }

                }
                else
                {
                    if (Txtnueva_Clave.getText().toString().equals(Txtnueva_Clave_Repite.getText().toString())){

                        new ActualizarUsuario (RecuperarContrasena_NuevaContrasena.this).execute(new String[]{"Cambiarcontrasena"});

                    }
                    else
                    {
                        Txtnueva_Clave_Repite.setError("Las contraseñas deben ser iguales");
                    }
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

    public class ActualizarUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConectarDB().createStatement();
                stm2.execute("Update Usuario_Propietario set Contrasena='" + Txtnueva_Clave.getText().toString() + "' where ID_Usuario_Propietario='" + Id_Usuario + "'");

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


            Alert_Dialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }


    }

}