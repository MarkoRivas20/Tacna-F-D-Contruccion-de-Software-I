package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;


public class ModificarEstablecimiento extends Fragment implements OnMapReadyCallback {

    public ModificarEstablecimiento() {
        // Required empty public constructor
    }
    Button Btnseleccionar;

    ImageView Foto_Gallery;

    Spinner Spinner_Categoria;
    Spinner Spinner_Distrito;
    Spinner Spinner_Estado;

    private GoogleMap Mapa;
    private CustomMapView Map_View;

    EditText Txtnombre;
    EditText Txtdireccion;
    EditText Txttelefono;
    EditText Txtdescripcion;
    EditText Txtcapacidad;

    String Punto_Geografico="";
    String Foto="";

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
    String burl_imagen_documento = "";
    String bpuntogeografico = "";
    String bestado = "";

    Button Btnatras;

    AlertDialog Alert_Dialog;

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;
    Uri Image_Uri;

    private static final int PICK_IMAGE = 100;

    String Id_Usuario = "";
    String Id_Establecimiento = "";


    Button Btnmodificar;
    String Url_Imagen_Logo = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_modificar_establecimiento, container, false);


        InicializarFirebase();

        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtdireccion = (EditText) v.findViewById(R.id.txtdireccion);
        Txttelefono = (EditText) v.findViewById(R.id.txttelefono);
        Txtdescripcion = (EditText) v.findViewById(R.id.txtdescripcion);
        Txtcapacidad = (EditText) v.findViewById(R.id.txtcapacidad);
        Foto_Gallery = (ImageView) v.findViewById(R.id.imagenlogo);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        String[] categorias = {"Seleccione una Categoria", "Restaurante", "Cafeteria", "Bar"};
        Spinner_Categoria = (Spinner) v.findViewById(R.id.spinnercategoria);
        Spinner_Categoria.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorias));

        String[] distritos = {"Seleccione un Distrito", "Tacna", "Alto del Alianza", "Calana", "Pachia", "Palca", "Pocollay", "Ciudad Nueva"};
        Spinner_Distrito = (Spinner) v.findViewById(R.id.spinnerdistrito);
        Spinner_Distrito.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, distritos));

        String[] estados = {"Seleccione un estado", "Activo", "Inactivo"};
        Spinner_Estado = (Spinner) v.findViewById(R.id.spinnerestado);
        Spinner_Estado.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, estados));

        Map_View = (CustomMapView) v.findViewById(R.id.map);
        Map_View.onCreate(savedInstanceState);
        Map_View.onResume();
        Map_View.getMapAsync(this);

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
        bpuntuacion = bundle.getString("puntuacion");
        burl_imagen_logo = bundle.getString("url_imagen_logo");
        burl_imagen_documento = bundle.getString("url_imagen_documento");
        bpuntogeografico = bundle.getString("puntogeografico");
        bestado = bundle.getString("estado");





        Txtnombre.setText(bundle.getString("nombre"));
        Txtdireccion.setText(bundle.getString("direccion"));
        Txttelefono.setText(bundle.getString("telefono"));
        Txtdescripcion.setText(bundle.getString("descripcion"));
        Txtcapacidad.setText(bundle.getString("capacidad"));
        Punto_Geografico = bundle.getString("puntogeografico");

        for (int i=0; i<categorias.length; i++)
        {
            if ((bundle.getString("categoria")).equals(categorias[i]))
            {
                Spinner_Categoria.setSelection(i);
                break;
            }
            else
            {

            }
        }
        for (int i=0; i<distritos.length; i++)
        {
            String b = bundle.getString("distrito");
            if (b.equals(distritos[i]))
            {
                Spinner_Distrito.setSelection(i);
                break;
            }
            else
            {

            }
        }

        for (int i=0; i<estados.length; i++)
        {
            if ((bundle.getString("estado")).equals(estados[i]))
            {
                Spinner_Estado.setSelection(i);
                break;
            }
            else
            {

            }
        }

        Url_Imagen_Logo = bundle.getString("url_imagen_logo");

        Picasso.with(getActivity()).load(Url_Imagen_Logo).into(Foto_Gallery);

        final Bundle bundle2 = new Bundle();
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
        bundle2.putString("url_imagen_documento", burl_imagen_documento);
        bundle2.putString("puntogeografico", bpuntogeografico);
        bundle2.putString("estado", bestado);


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {


                        PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                        perfilEstablecimiento.setArguments(bundle2);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
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

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                perfilEstablecimiento.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();
            }
        });


        Btnseleccionar = (Button) v.findViewById(R.id.btnseleccionar);
        Btnseleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(gallery, PICK_IMAGE);


            }
        });

        Btnmodificar = (Button) v.findViewById(R.id.btnmodificardatosestablecimiento);
        Btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txtnombre.length() == 0 || Txtdireccion.length() == 0 || Txttelefono.length() == 0 || Txtdescripcion.length() == 0 || Txtcapacidad.length() == 0
                        || Spinner_Categoria.getSelectedItemPosition() == 0 || Spinner_Distrito.getSelectedItemPosition() == 0 || Spinner_Estado.getSelectedItemPosition() == 0)
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
                    if (Spinner_Estado.getSelectedItemPosition() == 0)
                    {
                        Toast.makeText(getActivity(),"Seleccione un estado", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }

                }
                else
                {
                    new ActualizarEstablecimiento (getActivity()).execute(new String[]{"Actualizar"});
                }


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

            final StorageReference filePath2 = Storage_Reference.child(bid_establecimiento).child("Logo").child(Image_Uri.getLastPathSegment());

            filePath2.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Foto = uri.toString();
                            burl_imagen_logo = Foto;
                            try {

                                Statement stm = ConectarDB().createStatement();
                                stm.execute("Update Establecimiento set Url_Imagen_Logo='" + Foto + "' where ID_Establecimiento='" + bid_establecimiento + "'");

                            }catch (Exception e){
                                Log.e("Error", e.toString());
                            }

                            StorageReference photoeliminar = Firebase_Storage.getReferenceFromUrl(Url_Imagen_Logo);
                            photoeliminar.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

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

    @Override
    public void onMapReady (GoogleMap googleMap) {
        Mapa = googleMap;


        String[] ltdlng = Punto_Geografico.split("/");

        LatLng lugar = new LatLng(Double.parseDouble(ltdlng[0]), Double.parseDouble(ltdlng[1]));
        Mapa.moveCamera(CameraUpdateFactory.newLatLng(lugar));

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

    public class ActualizarEstablecimiento extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarEstablecimiento (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

                 try {

                    Statement stm = ConectarDB().createStatement();
                    stm.execute("Update Establecimiento set Nombre='" + Txtnombre.getText().toString() + "',Distrito='" + Spinner_Distrito.getSelectedItem().toString()
                            + "',Categoria='" + Spinner_Categoria.getSelectedItem().toString() + "',Direccion='" + Txtdireccion.getText().toString() + "',Telefono='"
                            + Txttelefono.getText().toString() + "',Descripcion='" + Txtdescripcion.getText().toString() + "',Capacidad='" + Txtcapacidad.getText().toString()
                            + "',Estado='" + Spinner_Estado.getSelectedItem().toString() + "',PuntoGeografico='" + Punto_Geografico
                            + "'where ID_Establecimiento='" + bid_establecimiento + "'");


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

            Alert_Dialog.dismiss();

            Bundle bundle3 = new Bundle();
            bundle3.putString("id_establecimiento", bid_establecimiento);
            bundle3.putString("nombre", Txtnombre.getText().toString());
            bundle3.putString("distrito", Spinner_Distrito.getSelectedItem().toString());
            bundle3.putString("categoria", Spinner_Categoria.getSelectedItem().toString());
            bundle3.putString("direccion", Txtdireccion.getText().toString());
            bundle3.putString("telefono", Txttelefono.getText().toString());
            bundle3.putString("descripcion", Txtdescripcion.getText().toString());
            bundle3.putString("capacidad", Txtcapacidad.getText().toString());
            bundle3.putString("totalresenas", btotalresenas);
            bundle3.putString("puntuacion", bpuntuacion);
            bundle3.putString("url_imagen_logo", burl_imagen_logo);
            bundle3.putString("url_imagen_documento", burl_imagen_documento);
            bundle3.putString("puntogeografico", Punto_Geografico);
            bundle3.putString("estado", Spinner_Estado.getSelectedItem().toString());

            PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
            perfilEstablecimiento.setArguments(bundle3);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
            transaction.commit();



        }


    }
}
