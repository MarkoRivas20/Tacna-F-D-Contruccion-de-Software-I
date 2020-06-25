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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class ModificarItemMenu extends Fragment {


    public ModificarItemMenu() {
        // Required empty public constructor
    }

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

    AlertDialog Alert_Dialog;

    Button Btnmodificar;

    EditText Txtnombre;
    EditText Txtdescripcion;
    EditText Txtprecio;

    Button Btnseleccionar;

    private static final int PICK_IMAGE = 100;

    Uri Image_Uri;
    ImageView Foto_Gallery;

    Button Btnatras;
    String Foto = "";

    String Id_Item_Menu = "";
    String Url_Actual_Item_Menu = "";

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_modificar_item_menu, container, false);


        InicializarFirebase();


        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtdescripcion = (EditText) v.findViewById(R.id.txtdescripcion);
        Txtprecio = (EditText) v.findViewById(R.id.txtprecio);

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Bundle bundle = getArguments();

        Id_Item_Menu = bundle.getString("id_item_menu");
        Url_Actual_Item_Menu = bundle.getString("url_imagen");

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


                        ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                        listaItemsMenu.setArguments(bundle2);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contenedorfragment, listaItemsMenu);
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

        Btnseleccionar = (Button) v.findViewById(R.id.btnseleccionar);
        Foto_Gallery = (ImageView) v.findViewById(R.id.imagenlogo);


        Picasso.with(getActivity()).load(Url_Actual_Item_Menu).into(Foto_Gallery);
        Txtnombre.setText(bundle.getString("nombremenu"));
        Txtdescripcion.setText(bundle.getString("descripcionmenu"));
        Txtprecio.setText(bundle.getString("precio"));

        Btnseleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }
        });

        Btnmodificar = (Button) v.findViewById(R.id.btnmodificaritemmenu);
        Btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txtnombre.length() == 0 || Txtdescripcion.length() == 0 || Txtprecio.length() == 0)
                {

                    if (Txtnombre.length() == 0)
                    {
                        Txtnombre.setError("Campo Requerido");
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
                    if (Txtprecio.length() == 0)
                    {
                        Txtprecio.setError("Campo Requerido");
                    }
                    else
                    {

                    }

                }
                else
                {

                    new ActualizarItemMenu (getActivity()).execute(new String[]{"ActualizarMenu"});
                }

            }
        });

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                listaItemsMenu.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaItemsMenu);
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

            final StorageReference filePath2 = Storage_Reference.child(bid_establecimiento).child("ItemMenu").child(Image_Uri.getLastPathSegment());

            filePath2.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Foto = uri.toString();

                            try{

                                Statement stm = ConectarDB().createStatement();
                                stm.execute("Update Item_Menu set Url_Imagen='" + Foto + "' where ID_Item_Menu=" + Id_Item_Menu);

                            }catch (Exception e){
                                Log.e("Error", e.toString());
                            }

                            StorageReference photoeliminar = Firebase_Storage.getReferenceFromUrl(Url_Actual_Item_Menu);
                            photoeliminar.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Url_Actual_Item_Menu = Foto;
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

    public class ActualizarItemMenu extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarItemMenu (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                Statement stm = ConectarDB().createStatement();
                stm.execute("Update Item_Menu set Nombre='" + Txtnombre.getText().toString() + "',Descripcion='" + Txtdescripcion.getText().toString()
                        + "',Precio='" + Txtprecio.getText().toString() + "' where ID_Item_Menu=" + Id_Item_Menu);


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

            Bundle bundle2 = new Bundle();
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

            ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
            listaItemsMenu.setArguments(bundle2);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaItemsMenu);
            transaction.commit();



        }


    }
}