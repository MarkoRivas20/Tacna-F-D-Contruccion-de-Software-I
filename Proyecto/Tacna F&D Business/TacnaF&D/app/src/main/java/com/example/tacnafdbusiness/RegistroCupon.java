package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;


public class RegistroCupon extends Fragment {

    public RegistroCupon() {
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

    Spinner Spinner_Estado;

    EditText Txttitulo;
    EditText Txtdescripcion;
    EditText Txtfecha_Inicio;
    EditText Txtfecha_Final;

    public final Calendar Calendario = Calendar.getInstance();
    final int Mes = Calendario.get(Calendar.MONTH);
    final int Dia = Calendario.get(Calendar.DAY_OF_MONTH);
    final int Anio = Calendario.get(Calendar.YEAR);

    private static final String CERO = "0";
    private static final String BARRA = "/";

    Button Btnseleccionar;
    private static final int PICK_IMAGE = 100;
    Uri Image_Uri;
    ImageView Foto_Gallery;
    Button Btnatras;

    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;
    AlertDialog Alert_Dialog;
    Button Btnregistrar;

    String Foto = "";

    boolean Booleano = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registro_cupon, container, false);
        Txttitulo = (EditText) v.findViewById(R.id.txttitulo);
        Txtdescripcion = (EditText) v.findViewById(R.id.txtdescripcion);

        InicializarFirebase();
        Booleano = false;


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



        Btnseleccionar = (Button) v.findViewById(R.id.btnseleccionar);
        Foto_Gallery = (ImageView) v.findViewById(R.id.imagenlogo);

        Btnseleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        String[] estados = {"Seleccione un estado inicial", "Activo", "Inactivo"};
        Spinner_Estado = (Spinner) v.findViewById(R.id.spinnerestado);

        Spinner_Estado.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, estados));


        Txtfecha_Inicio = (EditText) v.findViewById(R.id.txtfechainicio);
        Txtfecha_Inicio.setFocusable(false);
        Txtfecha_Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;

                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);

                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);

                        Txtfecha_Inicio.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                }, Anio, Mes , Dia);
                recogerFecha.show();
            }
        });

        Txtfecha_Final = (EditText) v.findViewById(R.id.txtfechafinal);
        Txtfecha_Final.setFocusable(false);
        Txtfecha_Final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;

                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);

                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);

                        Txtfecha_Final.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                }, Anio, Mes, Dia);
                recogerFecha.show();
            }
        });

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
        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                ListaCupon listaCupon = new ListaCupon();
                listaCupon.setArguments(bundle2);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaCupon);
                transaction.commit();
            }
        });

        Btnregistrar = (Button) v.findViewById(R.id.btnregistrarcupon);
        Btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Txttitulo.length() == 0 || Txtdescripcion.length() == 0 || Txtfecha_Final.length() == 0
                        || Txtfecha_Inicio.length() == 0 || Spinner_Estado.getSelectedItemPosition()==0 || !Booleano)
                {

                    if (Txttitulo.length() == 0)
                    {
                        Txttitulo.setError("Campo requerido");
                    }
                    else
                    {

                    }
                    if (Txtdescripcion.length() == 0)
                    {
                        Txtdescripcion.setError("Campo requerido");
                    }
                    else
                    {

                    }
                    if (Txtfecha_Final.length() == 0)
                    {
                        Txtfecha_Final.setError("Campo requerido");
                    }
                    else
                    {

                    }
                    if (Txtfecha_Inicio.length() == 0)
                    {
                        Txtfecha_Inicio.setError("Campo requerido");
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
                    if (!Booleano){
                        Toast.makeText(getActivity(),"Seleccione una imagen", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }

                }
                else
                {

                    new RegistrarCupon (getActivity()).execute(new String[]{"RegistrarCupon"});
                }
            }
        });



        return v;
    }
    private void InicializarFirebase(){

        FirebaseApp.initializeApp(getActivity());
        Storage_Reference = FirebaseStorage.getInstance().getReference();
        Firebase_Storage = FirebaseStorage.getInstance();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE)
        {
            Image_Uri = data.getData();
            Foto_Gallery.setImageURI(Image_Uri);
            Booleano = true;
        }
        else
        {

        }
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
    public class RegistrarCupon extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        RegistrarCupon (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                final StorageReference filePath = Storage_Reference.child(bid_establecimiento).child("Cupones").child(Image_Uri.getLastPathSegment());


                filePath.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Foto = uri.toString();
                                try {

                                    Statement stm = ConectarDB().createStatement();
                                    stm.execute("insert into Cupon(ID_Establecimiento,Titulo,Url_Imagen,Descripcion,Fecha_Inicio,Fecha_Final,Estado) " +
                                            "values ('" + bid_establecimiento + "','" + Txttitulo.getText().toString() + "','" + Foto + "','" + Txtdescripcion.getText().toString() + "','"
                                            + Txtfecha_Inicio.getText().toString() + "','" + Txtfecha_Final.getText().toString() + "','" + Spinner_Estado.getSelectedItem().toString() + "') ");

                                }catch (Exception e){
                                    Log.e("Error", e.toString());
                                }
                            }
                        });

                    }
                });
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

            Toast.makeText(getActivity(),"Se guardo el Cupon con exito", Toast.LENGTH_SHORT).show();


            Alert_Dialog.dismiss();

        }


    }
}
