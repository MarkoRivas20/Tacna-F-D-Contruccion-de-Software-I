package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Calendar;

import dmax.dialog.SpotsDialog;


public class ModificarCupon extends Fragment {

    public ModificarCupon() {
        // Required empty public constructor
    }


    String bid_establecimiento = "";

    Spinner Spinner_Estado;


    EditText Txttitulo;
    EditText Txtdescripcion;
    EditText Txtfecha_Inicio;
    EditText Txtfecha_Final;
    EditText TxtPorcentaje_Descuento;

    public final Calendar Calendario = Calendar.getInstance();
    final int Mes = Calendario.get(Calendar.MONTH);
    final int Dia = Calendario.get(Calendar.DAY_OF_MONTH);
    final int Anio = Calendario.get(Calendar.YEAR);

    private static final String CERO = "0";
    private static final String BARRA = "/";

    Button Btnseleccionar;
    Button Btnmodificar;

    private static final int PICK_IMAGE = 100;

    Uri Image_Uri;
    ImageView Foto_Gallery;
    Button Btnatras;
    String Foto="";

    AlertDialog Alert_Dialog;

    String Id_Cupon = "";
    String Url_Actual_Cupon = "";

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_modificar_cupon, container, false);

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

        InicializarFirebase();

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Txttitulo = (EditText) v.findViewById(R.id.txttitulo);
        TxtPorcentaje_Descuento = (EditText) v.findViewById(R.id.txtporcentajedescuento);
        Txtdescripcion = (EditText) v.findViewById(R.id.txtdescripcion);
        Txtfecha_Final = (EditText) v.findViewById(R.id.txtfechafinal);
        Txtfecha_Final.setFocusable(false);
        Txtfecha_Inicio = (EditText) v.findViewById(R.id.txtfechainicio);
        Txtfecha_Inicio.setFocusable(false);
        Foto_Gallery = (ImageView) v.findViewById(R.id.imagenlogo);

        bid_establecimiento = GetInfoFromSharedPreferences("ID");

        Id_Cupon = GetInfoCuponFromSharedPreferences("ID_Cupon");
        Url_Actual_Cupon = GetInfoCuponFromSharedPreferences("Url_Imagen_Cupon");
        Txttitulo.setText(GetInfoCuponFromSharedPreferences("Titulo"));
        Txtdescripcion.setText(GetInfoCuponFromSharedPreferences("Descripcion_Cupon"));
        TxtPorcentaje_Descuento.setText(GetInfoCuponFromSharedPreferences("Descuento_Cupon"));

        String []FechaInicio = GetInfoCuponFromSharedPreferences("Fecha_Inicio").split("-");
        Txtfecha_Inicio.setText(FechaInicio[2]+"/"+FechaInicio[1]+"/"+FechaInicio[0]);
        String []FechaFinal = GetInfoCuponFromSharedPreferences("Fecha_Final").split("-");
        Txtfecha_Final.setText(FechaFinal[2]+"/"+FechaFinal[1]+"/"+FechaFinal[0]);


        String[] estados = {"Seleccione un estado", "Activo", "Inactivo"};
        Spinner_Estado = (Spinner) v.findViewById(R.id.spinnerestado);

        Spinner_Estado.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, estados));

        for (int i=0; i<estados.length; i++)
        {
            if ((GetInfoCuponFromSharedPreferences("Estado_Cupon")).equals(estados[i]))
            {
                Spinner_Estado.setSelection(i);
                break;
            }
            else
            {

            }

        }

        Picasso.with(getActivity()).load(GetInfoCuponFromSharedPreferences("Url_Imagen_Cupon")).into(Foto_Gallery);

        Btnmodificar = (Button) v.findViewById(R.id.btnmodificarcupon);
        Btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Txttitulo.length() == 0 || Txtdescripcion.length() == 0 || Txtfecha_Final.length() == 0
                        || Txtfecha_Inicio.length() == 0 || Spinner_Estado.getSelectedItemPosition() == 0 || TxtPorcentaje_Descuento.length() == 0)
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
                    if (TxtPorcentaje_Descuento.length() == 0)
                    {
                        TxtPorcentaje_Descuento.setError("Campo requerido");
                    }
                    else
                    {

                    }

                }
                else
                {
                    new ActualizarCupon (getActivity()).execute(new String[]{"ActualizarCupon"});
                }

            }
        });

        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaCupon listaCupon = new ListaCupon();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaCupon);
                transaction.commit();
            }
        });

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
                }, Anio, Mes, Dia);
                recogerFecha.show();
            }
        });


        Txtfecha_Final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog recogerFecha=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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

        Btnseleccionar = (Button) v.findViewById(R.id.btnseleccionar);
        Btnseleccionar.setOnClickListener(new View.OnClickListener() {
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
            Foto_Gallery.setImageURI(Image_Uri);

            final StorageReference filePath2 = Storage_Reference.child(bid_establecimiento).child("Cupones").child(Image_Uri.getLastPathSegment());

            filePath2.putFile(Image_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Foto = uri.toString();
                            try {

                                Statement stm = ConectarDB().createStatement();
                                stm.execute("Update Cupon set Url_Imagen='" + Foto + "' where ID_Cupon=" + Id_Cupon);

                            }catch (Exception e){
                                Log.e("Error", e.toString());
                            }

                            StorageReference photoeliminar = Firebase_Storage.getReferenceFromUrl(Url_Actual_Cupon);
                            photoeliminar.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Url_Actual_Cupon = Foto;
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
            //cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.2;databaseName=dbtacnafyd;user=sa;password=upt;");
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://tacnafyd.database.windows.net:1433;databaseName=TacnaFyD;user=MarkoRivas;password=Tacna2018.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=ContruccionI.database.windows.net;loginTimeout=30;");


        }catch (Exception e){

        }

        return cnn;
    }

    public class ActualizarCupon extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarCupon (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                Statement stm = ConectarDB().createStatement();

                stm.execute("Update Cupon set Titulo='" + Txttitulo.getText().toString() + "',Descripcion='" + Txtdescripcion.getText().toString() +
                        "',Fecha_Inicio=Convert(date,'" + Txtfecha_Inicio.getText().toString()
                        + "',103),Fecha_Final=Convert(date,'" + Txtfecha_Final.getText().toString() + "',103),Estado='"
                        + Spinner_Estado.getSelectedItem().toString() + "', Porcentaje_Descuento="+TxtPorcentaje_Descuento.getText().toString()
                        +" where ID_Cupon=" + Id_Cupon);


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

            ListaCupon listaCupon = new ListaCupon();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaCupon);
            transaction.commit();



        }


    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

    private String GetInfoCuponFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_cupon", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
