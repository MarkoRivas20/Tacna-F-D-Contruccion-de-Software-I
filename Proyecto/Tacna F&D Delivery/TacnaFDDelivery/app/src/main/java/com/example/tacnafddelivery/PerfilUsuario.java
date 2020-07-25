package com.example.tacnafddelivery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;


public class PerfilUsuario extends Fragment {


    public PerfilUsuario() {
        // Required empty public constructor
    }

    TextView Lblbienvenida;

    Button Btnestablecimiento;
    Button Btnactualizar_Datos;
    Button Btnactualizar_Foto;
    Button Btncerrar;


    EditText Txtemail;
    EditText Txtclave;
    EditText Txtnombre;
    EditText Txtapellido;

    AlertDialog Alert_Dialog;

    String Id = "";
    String Email = "";
    String Clave = "";
    String Nombre = "";
    String Apellido = "";
    String Url_Foto = "";

    String Foto = "";
    String Nuevo_Url_Foto = "";

    ImageView Foto_Gallery;

    private static final int PICK_IMAGE = 100;

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;
    Uri Image_Uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        InicializarFirebase();

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

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

        Id = GetFromSharedPreferences("ID");
        Email = GetFromSharedPreferences("email");
        Clave = GetFromSharedPreferences("clave");
        Nombre = GetFromSharedPreferences("nombre");
        Apellido = GetFromSharedPreferences("apellido");
        Url_Foto = GetFromSharedPreferences("url_foto");

        Lblbienvenida = (TextView) v.findViewById(R.id.lblbienvenida);


        Lblbienvenida.setText("¡Hola " + Nombre + "!");


        Txtemail = (EditText) v.findViewById(R.id.txtemail);
        Txtclave = (EditText) v.findViewById(R.id.txtclave);
        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtapellido = (EditText) v.findViewById(R.id.txtapellido);
        Foto_Gallery = (ImageView) v.findViewById(R.id.foto);

        Picasso.with(getContext()).load(Url_Foto).into(Foto_Gallery);

        Txtemail.setText(Email);
        Txtclave.setText(Clave);
        Txtnombre.setText(Nombre);
        Txtapellido.setText(Apellido);

        Txtemail.setEnabled(false);

        Btnestablecimiento = (Button) v.findViewById(R.id.btnestablecimientos);
        Btnestablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaEstablecimiento listaEstablecimiento = new ListaEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaEstablecimiento);
                transaction.commit();
            }
        });

        Btnactualizar_Datos = (Button) v.findViewById(R.id.btnmodificar);
        Btnactualizar_Datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Txtclave.length() == 0 || Txtnombre.length() == 0 || Txtapellido.length() == 0)
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
                }
                else
                {
                    new ActualizarUsuario (getActivity()).execute(new String[]{"Actualizar"});
                }

            }
        });

        Btnactualizar_Foto = (Button) v.findViewById(R.id.btnactualizarfoto);
        Btnactualizar_Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        Btncerrar = (Button) v.findViewById(R.id.btncerrar);

        Btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String IDa = "";
                final String nombrea = "";
                final String apellidoa = "";
                final String emaila = "";
                final String clavea = "";
                final String fotoa = "";
                SaveLoginSharedPreferences(IDa, emaila, clavea, nombrea, apellidoa, fotoa);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return v;
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


    public class ActualizarUsuario extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConectarDB().createStatement();
                stm2.execute("Update Usuario_Repartidor set Contrasena='" + Txtclave.getText().toString() + "',Nombre='" + Txtnombre.getText().toString() + "'" +
                        ",Apellido='" + Txtapellido.getText().toString() + "' where ID_Usuario_Repartidor='" + Id + "'");

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


            Toast.makeText(getActivity(),"Datos Actualizados", Toast.LENGTH_SHORT).show();

            Lblbienvenida.setText("¡Hola " + Txtnombre.getText().toString() + "!");
            SaveLoginSharedPreferences(Id, Email, Txtclave.getText().toString(), Txtnombre.getText().toString(), Txtapellido.getText().toString(),
                    Url_Foto);

            Alert_Dialog.dismiss();

        }


    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE)
        {
            Image_Uri = data.getData();
            Foto_Gallery.setImageURI(Image_Uri);

            final StorageReference filePath2 = Storage_Reference.child("Repartidores").child(Id).child(Image_Uri.getLastPathSegment());

            filePath2.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Foto = uri.toString();

                            try {

                                Statement stm = ConectarDB().createStatement();
                                stm.execute("Update Usuario_Repartidor set Url_Foto='" + Foto + "' where ID_Usuario_Repartidor=" + Id);

                                SaveLoginSharedPreferences(Id, Email, Txtclave.getText().toString(), Txtnombre.getText().toString(), Txtapellido.getText().toString(),
                                        Foto);

                            }catch (Exception e){
                                Log.e("Error", e.toString());
                            }

                            StorageReference photoeliminar = Firebase_Storage.getReferenceFromUrl(Url_Foto);
                            photoeliminar.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Url_Foto=Foto;
                                }
                            });



                        }
                    });

                }
            });
        }
        else
        {

        }
    }

    private void InicializarFirebase(){

        FirebaseApp.initializeApp(getActivity());
        Firebase_Database = FirebaseDatabase.getInstance();
        Database_Reference = Firebase_Database.getReference();
        Storage_Reference = FirebaseStorage.getInstance().getReference();
        Firebase_Storage = FirebaseStorage.getInstance();
    }

    private void SaveLoginSharedPreferences(String ID, String email, String clave, String nombre, String apellido, String url_foto){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
