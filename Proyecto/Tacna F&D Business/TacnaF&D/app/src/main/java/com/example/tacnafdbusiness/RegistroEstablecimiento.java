package com.example.tacnafdbusiness;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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


public class RegistroEstablecimiento extends Fragment implements OnMapReadyCallback {

    public RegistroEstablecimiento() {
        // Required empty public constructor
    }


    Button Btnseleccionar;
    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_DOCUMENT = 200;
    Uri Image_Uri;
    Uri Image_Document_Uri;
    ImageView Foto_Gallery;

    Spinner Spinner_Categoria;
    Spinner Spinner_Distrito;

    private GoogleMap Mapa;
    private CustomMapView Map_View;

    EditText Txtnombre;
    EditText Txtdireccion;
    EditText Txttelefono;
    EditText Txtdescripcion;
    EditText Txtcapacidad;
    EditText Txtdocumento;

    String Punto_Geografico = "-18.007667/-70.239441";
    String Foto = "";
    String Foto_Documento = "";

    AlertDialog Alert_Dialog;

    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;

    String Id_Usuario = "";
    String Id_Establecimiento = "";


    ResultSet Result_Set;

    Button Btnregistrar;
    Button Btnatras;

    int bandera = 0;
    int bandera2 = 0;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registro_establecimiento, container, false);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        ListaEstablecimientos fragmentEstablecimiento = new ListaEstablecimientos();
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

        bandera = 0;
        InicializarFirebase();
        Id_Usuario = GetFromSharedPreferences("ID");

        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtdireccion = (EditText) v.findViewById(R.id.txtdireccion);
        Txttelefono = (EditText) v.findViewById(R.id.txttelefono);
        Txtdescripcion = (EditText) v.findViewById(R.id.txtdescripcion);
        Txtcapacidad = (EditText) v.findViewById(R.id.txtcapacidad);
        Txtdocumento = (EditText) v.findViewById(R.id.txtdocumento);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Btnseleccionar = (Button) v.findViewById(R.id.btnseleccionar);
        Foto_Gallery = (ImageView) v.findViewById(R.id.imagenlogo);

        Btnseleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        Txtdocumento.setFocusable(false);
        Txtdocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE_DOCUMENT);
            }
        });


        String[] categorias = {"Seleccione una Categoria", "Restaurante", "Cafeteria", "Bar"};
        Spinner_Categoria = (Spinner) v.findViewById(R.id.spinnercategoria);
        Spinner_Categoria.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorias));

        String[] distritos = {"Seleccione un Distrito", "Tacna", "Alto del Alianza", "Calana", "Pachia", "Palca", "Pocollay", "Ciudad Nueva"};
        Spinner_Distrito = (Spinner) v.findViewById(R.id.spinnerdistrito);
        Spinner_Distrito.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, distritos));


        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

        Btnregistrar = (Button) v.findViewById(R.id.btnregistrar);
        Btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Txtnombre.length() == 0 || Txtdireccion.length() == 0 || Txttelefono.length() == 0 || Txtdescripcion.length() == 0 || Txtcapacidad.length() == 0
                        || Spinner_Categoria.getSelectedItemPosition() == 0 || Spinner_Distrito.getSelectedItemPosition() == 0 || bandera == 0 || bandera2 == 0
                        || Punto_Geografico.equals(""))
                {
                    if (Txtnombre.length() == 0)
                    {
                        Txtnombre.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if (Txtdireccion.length() == 0)
                    {
                        Txtdireccion.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if (Txttelefono.length() == 0)
                    {
                        Txttelefono.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if (Txtdescripcion.length() == 0)
                    {
                        Txtdescripcion.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if (Txtcapacidad.length() == 0)
                    {
                        Txtcapacidad.setError("Campo Requerido");
                    }
                    else
                    {

                    }
                    if (Spinner_Categoria.getSelectedItemPosition() == 0)
                    {
                        Toast.makeText(getActivity(),"Seleccione una categoria", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                    if (Spinner_Distrito.getSelectedItemPosition() == 0)
                    {
                        Toast.makeText(getActivity(),"Seleccione un distrito", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                    if (bandera == 0)
                    {
                        Toast.makeText(getActivity(),"Seleccione una Imagen", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                    if (bandera2 == 0)
                    {
                        Toast.makeText(getActivity(),"Seleccione el documento", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                    if (Punto_Geografico.equals(""))
                    {
                        Toast.makeText(getActivity(),"Marque el establecimiento en el Mapa", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }


                }
                else
                {

                    new RegistrarEstablecimiento (getActivity()).execute(new String[]{"Registrar"});
                }
            }
        });

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaEstablecimientos fragmentEstablecimiento = new ListaEstablecimientos();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, fragmentEstablecimiento);
                transaction.commit();
            }
        });


        return v;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE)
        {
            Image_Uri = data.getData();
            Foto_Gallery.setImageURI(Image_Uri);
            bandera = 1;
        }
        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE_DOCUMENT)
        {
            Image_Document_Uri = data.getData();
            Txtdocumento.setText(Image_Document_Uri.getLastPathSegment());
            bandera2 = 1;
        }
    }

    @Override
    public void onMapReady (GoogleMap googleMap) {

        Mapa = googleMap;

        LatLng Tacna = new LatLng(-18.012580, -70.246520);
        Mapa.moveCamera(CameraUpdateFactory.newLatLng(Tacna));

        Mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Mapa.clear();
                String latitud = String.valueOf(latLng.latitude);
                String longitud = String.valueOf(latLng.longitude);

                MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.valueOf(latitud), Double.valueOf(longitud))).title("Marcador");
                Mapa.addMarker(marker);


                Punto_Geografico = latitud + "/" + longitud;
            }
        });

    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
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

    public class RegistrarEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        RegistrarEstablecimiento (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                Statement stm = ConectarDB().createStatement();
                stm.execute("INSERT INTO Establecimiento(ID_Usuario_Propietario,Nombre,Distrito,Categoria,Direccion,Telefono,Descripcion,Capacidad,TotalReseñas," +
                        "Puntuacion,Url_Imagen_Logo,PuntoGeografico,Estado) values('" + Id_Usuario + "','" + Txtnombre.getText().toString()
                        + "','"+Spinner_Distrito.getSelectedItem().toString() + "','" + Spinner_Categoria.getSelectedItem().toString()+"','"
                        + Txtdireccion.getText().toString() + "','"+Txttelefono.getText().toString() + "','" + Txtdescripcion.getText().toString()
                        + "',"+Txtcapacidad.getText().toString() + "," + "0" + "," + "0.0" + ",'" + Foto + "','" + Punto_Geografico + "','Activo')");

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




            new BuscarEstablecimiento (getActivity()).execute(new String[]{"ObtenerID"});
            //mDialog.dismiss();

        }


    }


    public class BuscarEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext2 = null;

        BuscarEstablecimiento (Context context2){
            mContext2 = context2;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


            try {

                Statement stm2 = ConectarDB().createStatement();
                Result_Set = stm2.executeQuery("Select * from Establecimiento " +
                        "where Nombre='" + Txtnombre.getText().toString() + "' and PuntoGeografico='" + Punto_Geografico + "'");

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

                if (Result_Set.next())
                {
                    Id_Establecimiento = Result_Set.getString(1);
                }
                else
                {

                }

            }catch (Exception e){
                Log.e("Error", e.toString());


            }

            //mDialog.dismiss();
            new ActualizarEstablecimiento (getActivity()).execute(new String[]{"SubirYActualizar"});


        }


    }

    public class ActualizarEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext3 = null;

        ActualizarEstablecimiento (Context context3){
            mContext3 = context3;
        }

        @Override
        protected Boolean doInBackground (String... strings) {


             final StorageReference filePath = Storage_Reference.child(Id_Establecimiento).child("Logo").child(Image_Uri.getLastPathSegment());

            final StorageReference filePathDocument = Storage_Reference.child(Id_Establecimiento).child("Documento").child(Image_Document_Uri.getLastPathSegment());


            filePath.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Foto = uri.toString();


                            filePathDocument.putFile(Image_Document_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    filePathDocument.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Foto_Documento = uri.toString();

                                            try {

                                                Statement stm = ConectarDB().createStatement();
                                                stm.execute("Update Establecimiento set Url_Imagen_Logo='" + Foto + "', Url_Imagen_Documento='"+Foto_Documento+"' where ID_Establecimiento='" + Id_Establecimiento + "'");

                                            }catch (Exception e){
                                                Log.e("Error", e.toString());
                                            }

                                        }
                                    });


                                }
                            });



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


        }


    }

    private void InicializarFirebase (){

        FirebaseApp.initializeApp(getActivity());
        Storage_Reference = FirebaseStorage.getInstance().getReference();
        Firebase_Storage = FirebaseStorage.getInstance();
    }


}
