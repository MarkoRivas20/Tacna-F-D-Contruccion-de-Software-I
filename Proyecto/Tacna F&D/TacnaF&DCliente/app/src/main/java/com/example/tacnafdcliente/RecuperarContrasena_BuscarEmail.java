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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dmax.dialog.SpotsDialog;

public class RecuperarContrasena_BuscarEmail extends AppCompatActivity {

    String Correo = "";
    String Clave = "";

    String Claverecover = "";
    String Mensaje = "";

    EditText Txtemail;
    Button Btnenviar;

    Session Sesion;

    AlertDialog Alert_Dialog;

    ResultSet Result_Set;

    boolean Booleano = false;

    String Id_Usuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena__buscar_email);

        getSupportActionBar().hide();

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(RecuperarContrasena_BuscarEmail.this)
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Correo = "no.reply.tacnafyd@gmail.com";
        Clave = "Tacna2018.";

        Txtemail = (EditText) findViewById(R.id.txtemailrecover);

        Btnenviar = (Button) findViewById(R.id.btnenviar);


        Btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txtemail.length() == 0)
                {
                    Txtemail.setError("Campo requerido");

                }
                else
                {
                    new BuscarUsuario (RecuperarContrasena_BuscarEmail.this).execute(new String[]{"BuscarEmail"});
                }


                //consulta();
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
        protected  void onPostExecute (Boolean result){

            try {

                if(Result_Set.next())
                {
                    Id_Usuario = Result_Set.getString(1);
                    Booleano = true;
                }
                else
                {
                    Booleano = false;
                }

            }catch (Exception e){
                Log.e("Error", e.toString());
            }



            new EnviarEmail (RecuperarContrasena_BuscarEmail.this).execute(new String[]{"EnviarCodigo"});
            //mDialog.dismiss();

        }


    }

    public class EnviarEmail extends AsyncTask <String, Integer, Boolean> {


        private Context mContext2 = null;

        EnviarEmail (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                if (Booleano)
                {

                    int numero = (int) (Math.random() * 999998) + 1;
                    Claverecover = String.valueOf(numero);
                    Mensaje = "Recientemente ha solicitado restablecer la contraseña de la cuenta asociada con esta dirección de correo electrónico. " +
                            "\n Introduzca este código en página de restablecimiento de contraseñas. \n "+Claverecover;


                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Properties properties = new Properties();

                    String cadena = Txtemail.getText().toString();

                    properties.put("mail.smtp.host", "smtp.googlemail.com");
                    properties.put("mail.smtp.socketFactory.port", "465");
                    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.port", "465");


                    try{

                        Sesion = Session.getDefaultInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(Correo,Clave);
                            }
                        });

                        if (Sesion != null)
                        {
                            Message message = new MimeMessage(Sesion);
                            message.setFrom(new InternetAddress(Correo));
                            message.setSubject("Recuperación de contraseñas");
                            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Txtemail.getText().toString()));
                            message.setContent(Mensaje,"text/html; charset=utf-8");

                            Transport.send(message);

                            Txtemail.setError("Correo electronico enviado");

                        }
                        else
                        {

                        }



                    }catch (Exception e){
                        Log.e("Error", e.toString());
                    }

                }
                else
                {

                }



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

            if (Booleano)
            {
                Intent intent = new Intent(getApplicationContext(), RecuperarContrasena_IngresarCodigo.class);
                intent.putExtra("claverecover", Claverecover);
                intent.putExtra("idusuario", Id_Usuario);
                startActivity(intent);
            }
            else
            {

                Txtemail.setError("Correo electronico no encontrado");
            }



            Alert_Dialog.dismiss();

        }


    }
}
