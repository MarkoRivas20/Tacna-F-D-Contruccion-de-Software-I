package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tacnafdbusiness.Adaptador.ImagenesEstablecimientoAdapter;
import com.example.tacnafdbusiness.Model.ImagenEstablecimiento;
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
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ImagenesEstablecimiento extends Fragment {


    public ImagenesEstablecimiento() {
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

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnanadir_Foto;
    Button Btnreseñas;
    Button Btncupon;
    Button Btnmenu;

    ImageView Img_Logo;

    TextView Txtnombre;

    ResultSet Result_Set;

    AlertDialog Alert_Dialog;

    private RecyclerView Recycler_View;
    private ImagenesEstablecimientoAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<ImagenEstablecimiento> Items;

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;
    Uri Image_Uri;


    private static final int PICK_IMAGE = 100;

    String Foto = "";

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_imagenes_establecimiento, container, false);

        InicializarFirebase();

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

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaImagenes) ;
        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();


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


        Img_Logo = (ImageView) v.findViewById(R.id.imglogo);

        Picasso.with(getContext()).load(burl_imagen_logo).into(Img_Logo);


        Txtnombre = (TextView) v.findViewById(R.id.lblnombre);
        Txtnombre.setText(bnombre);



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

        Btndescripcion = (Button) v.findViewById(R.id.btndescripcion);
        Btndescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                perfilEstablecimiento.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();
            }
        });

        Btnperfil = (Button) v.findViewById(R.id.btnperfil);
        Btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilUsuario perfilUsuario = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilUsuario);
                transaction.commit();

            }
        });

        Btnreseñas = (Button) v.findViewById(R.id.btnreseñas);
        Btnreseñas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaResenas listaResenas = new ListaResenas();
                listaResenas.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaResenas);
                transaction.commit();
            }
        });

        Btncupon = (Button) v.findViewById(R.id.btncupon);
        Btncupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaCupon listaCupon = new ListaCupon();
                listaCupon.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaCupon);
                transaction.commit();
            }
        });

        Btnmenu = (Button)v.findViewById(R.id.btnmenu);
        Btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaItemsMenu listaItemsMenu = new ListaItemsMenu();
                listaItemsMenu.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaItemsMenu);
                transaction.commit();
            }
        });

        //new myasynctask(getActivity()).execute(new String[]{"BuscarImagenes"});
        BuscarImagenes();

        Adaptador.setOnItemClickListener(new ImagenesEstablecimientoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick (int position) {
                String text = "ID: " + Items.get(position).getID_Imagen_Establecimiento();
                Toast.makeText(getActivity(),"Manten presionado para ver las opciones", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onEliminar (int position) {
                String Id_Imagen = String.valueOf(Items.get(position).getID_Imagen_Establecimiento());
                String url = Items.get(position).getUrl_Imagen();
                EliminarImagen(Id_Imagen, url);
            }

            @Override
            public void onNoEliminar (int position) {

            }
        });

        Recycler_View.setAdapter(Adaptador);


        Btnanadir_Foto = (Button) v.findViewById(R.id.btnanadirfoto);
        Btnanadir_Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(gallery, PICK_IMAGE);
            }
        });


        return v;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE)
        {
            Image_Uri = data.getData();

            final StorageReference filePath = Storage_Reference.child(bid_establecimiento).child("Imagenes").child(Image_Uri.getLastPathSegment());

            filePath.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Foto = uri.toString();

                            try {

                                Statement stm = ConectarDB().createStatement();
                                stm.execute("insert into Imagen_Establecimiento(ID_Establecimiento,Url_Imagen) values(" + bid_establecimiento + ",'" + Foto + "')");

                            }catch (Exception e){
                                Log.e("Error", e.toString());
                            }

                            //new myasynctask(getActivity()).execute(new String[]{"BuscarImagenes"});
                            BuscarImagenes();

                            Recycler_View.setAdapter(Adaptador);
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

    public void BuscarImagenes(){
        try {

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("Select * from Imagen_Establecimiento where ID_Establecimiento=" + bid_establecimiento);

            Items = new ArrayList<>();

            while (Result_Set.next()){

                ImagenEstablecimiento establecimiento = new ImagenEstablecimiento();
                establecimiento.setID_Imagen_Establecimiento(Result_Set.getInt(1));
                establecimiento.setID_Establecimiento(Result_Set.getInt(2));
                establecimiento.setUrl_Imagen(Result_Set.getString(3));

                Items.add(establecimiento);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new ImagenesEstablecimientoAdapter(Items, getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
        }
    }

    public void EliminarImagen (String ID_Imagen, String url){
        try {

            Statement stm = ConectarDB().createStatement();
            stm.execute("Delete from Imagen_Establecimiento where ID_Imagen_Establecimiento=" + ID_Imagen);


        }catch (Exception e){

        }

        StorageReference photoref = Firebase_Storage.getReferenceFromUrl(url);
        photoref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),"Imagen Eliminada", Toast.LENGTH_SHORT).show();
            }
        });

        BuscarImagenes();
        Recycler_View.setAdapter(Adaptador);
    }

/*
    public class myasynctask extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        myasynctask(Context context){
            mContext=context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {




            try {

                Statement stm2=connectionDB().createStatement();
                rs=stm2.executeQuery("Select * from Imagen_Establecimiento where ID_Establecimiento="+bid_establecimiento);



            }catch (Exception e){
                Log.e("Error",e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute(){

            mDialog.show();

        }

        @Override
        protected  void onPostExecute(Boolean result){

            try {

                items=new ArrayList<>();

                while (rs.next()){
                    ImagenEstablecimiento establecimiento=new ImagenEstablecimiento();
                    establecimiento.setID_Imagen_Establecimiento(rs.getInt(1));
                    establecimiento.setID_Establecimiento(rs.getInt(2));
                    establecimiento.setUrl_Imagen(rs.getString(3));

                    items.add(establecimiento);
                }

                recycler.setHasFixedSize(true);
                lManager=new LinearLayoutManager(getActivity());
                recycler.setLayoutManager(lManager);

                adapter=new ImagenesEstablecimientoAdapter(items,getActivity());

                recycler.setAdapter(adapter);


            }catch (Exception e){
                Log.e("Error",e.toString());
            }


            mDialog.dismiss();


        }


    }*/
}
