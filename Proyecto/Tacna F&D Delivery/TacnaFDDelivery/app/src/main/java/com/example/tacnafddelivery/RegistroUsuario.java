package com.example.tacnafddelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    Button Btnfoto;

    ResultSet Result_Set;
    ResultSet Result_Set2;
    AlertDialog Alert_Dialog;

    boolean Booleano = false;
    boolean Booleano2 = false;

    private static final int PICK_IMAGE = 100;

    Uri Image_Uri;

    ImageView Foto_Gallery;

    String Foto = "";
    String Id_Repartidor = "";

    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);getSupportActionBar().hide();


        InicializarFirebase();

        Txtemail = (EditText) findViewById(R.id.txtemail);
        Txtclave = (EditText) findViewById(R.id.txtclave);
        Txtnombre = (EditText) findViewById(R.id.txtnombre);
        Txtapellido = (EditText) findViewById(R.id.txtapellido);

        Btncrear = (Button) findViewById(R.id.btncrear);
        Btnfoto = (Button) findViewById(R.id.btnfoto);

        Foto_Gallery = (ImageView) findViewById(R.id.foto);

        Btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });



        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(RegistroUsuario.this)
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txtemail.length() == 0 || Txtclave.length() == 0 || Txtnombre.length() == 0 || Txtapellido.length() == 0 || !Booleano)
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

                    if (!Booleano)
                    {
                        Toast.makeText(getApplicationContext(),"Seleccione una Foto", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            Image_Uri = data.getData();
            Foto_Gallery.setImageURI(Image_Uri);
            Booleano = true;
        }
        else
        {
            Booleano = false;
        }
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

    public class BuscarUsuario extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        BuscarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConnectionDB().createStatement();
                Result_Set = stm2.executeQuery("Select * from Usuario_Repartidor where Email='" + Txtemail.getText().toString() + "'");

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
                    Booleano2 = true;
                }
                else
                {

                }

            }catch (Exception e){



            }

            if (Booleano2)
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


    public class RegistrarUsuario extends AsyncTask<String, Integer, Boolean> {


        private Context mContext2 = null;

        RegistrarUsuario(Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground(String... strings) {


            try {

                Statement stm = ConnectionDB().createStatement();
                stm.execute("INSERT INTO Usuario_Repartidor(Email,Contrasena,Nombre,Apellido,Url_Foto) values('" + Txtemail.getText().toString() + "','" + Txtclave.getText().toString()
                        + "','"+Txtnombre.getText().toString() + "','"+Txtapellido.getText().toString() + "','"+Foto+"')");

                Result_Set2 = stm.executeQuery("Select * from Usuario_Repartidor where Email='" +Txtemail.getText().toString()+"'" );

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

            //Alert_Dialog.dismiss();

            try {

                if (Result_Set2.next())
                {
                    Id_Repartidor = Result_Set2.getString(1);
                }
                else
                {

                }

            }catch (Exception e){
                Log.e("Error", e.toString());


            }

            new ActualizarRepartidor (getApplicationContext()).execute(new String[]{"SubirYActualizar"});





        }


    }



    public class ActualizarRepartidor extends AsyncTask <String, Integer, Boolean> {


        private Context mContext3 = null;

        ActualizarRepartidor (Context context3){
            mContext3 = context3;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            final StorageReference filePath = Storage_Reference.child("Repartidores").child(Id_Repartidor).child(Image_Uri.getLastPathSegment());



            filePath.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Foto = uri.toString();

                            try {

                                Statement stm = ConnectionDB().createStatement();
                                stm.execute("Update Usuario_Repartidor set Url_Foto='" + Foto + "' where ID_Usuario_Repartidor=" + Id_Repartidor);

                            }catch (Exception e){
                                Log.e("Error", e.toString());
                            }
                        }
                    });

                }
            });




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

    private void InicializarFirebase (){

        FirebaseApp.initializeApp(getApplicationContext());
        Storage_Reference = FirebaseStorage.getInstance().getReference();
        Firebase_Storage = FirebaseStorage.getInstance();
    }
}