package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
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

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;


public class DetalleCupon extends Fragment {

    public DetalleCupon() {
        // Required empty public constructor
    }

    Button Btnperfil;
    Button Btnestablecimiento;
    Button Btnatras;
    Button Btnusar;

    TextView Txtnombre;
    TextView Txtfecha;
    TextView Txttitulo;
    TextView Txtdescripcion;
    TextView Txtfechainicio;
    TextView Txtfechafinal;

    String Url_Imagen = "";

    ImageView Foto_Gallery;

    AlertDialog Alert_Dialog;

    String Id_Cupon_Usuario = "";


    AlertDialog.Builder Mensaje;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_detalle_cupon, container, false);


        Mensaje = new AlertDialog.Builder(getActivity());
        Mensaje.setTitle("Usar Cupon");
        Mensaje.setMessage("¿Esta seguro que desea usar su cupon? Ya no podra revertirlo");
        Mensaje.setCancelable(false);
        Mensaje.setPositiveButton("Si, estoy seguro", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new ActualizarEstadoCupon(getActivity()).execute(new String[]{"ActualizarCuponUsuario"});

            }
        });
        Mensaje.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        Alert_Dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere")
                .setCancelable(false)
                .build();

        Txttitulo = (TextView) v.findViewById(R.id.txttitulo);
        Txtfecha = (TextView) v.findViewById(R.id.txtfecha);
        Txtnombre = (TextView) v.findViewById(R.id.txtnombre);
        Txtdescripcion = (TextView) v.findViewById(R.id.txtdescripcion);
        Txtfechainicio = (TextView) v.findViewById(R.id.txtfechainicio);
        Txtfechafinal = (TextView) v.findViewById(R.id.txtfechafinal);
        Foto_Gallery = (ImageView) v.findViewById(R.id.imagenlogo);



        Bundle bundle = getArguments();

        Txttitulo.setText(bundle.getString("Titulo"));
        Txtnombre.setText(bundle.getString("Nombre"));
        Txtfecha.setText(bundle.getString("Fecha"));
        Txtdescripcion.setText(bundle.getString("Descripcion"));
        Txtfechainicio.setText(bundle.getString("Fecha_Inicio"));
        Txtfechafinal.setText(bundle.getString("Fecha_Final"));
        Id_Cupon_Usuario=bundle.getString("ID_Cupon_Usuario");

        Picasso.with(getActivity()).load(bundle.getString("Url_Imagen")).into(Foto_Gallery);



        Btnperfil = (Button)v.findViewById(R.id.btnperfil);
        Btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilUsuario perfilUsuario = new PerfilUsuario();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilUsuario);
                transaction.commit();
            }
        });

        Btnestablecimiento = (Button)v.findViewById(R.id.btnestablecimientos);
        Btnestablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarEstablecimiento buscarEstablecimiento = new BuscarEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, buscarEstablecimiento);
                transaction.commit();
            }
        });

        Btnatras = (Button)v.findViewById(R.id.btnatras);
        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaMiCupon listaMiCupon = new ListaMiCupon();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, listaMiCupon);
                transaction.commit();
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

        Btnusar = (Button)v.findViewById(R.id.btnusar);
        Btnusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mensaje.show();
            }
        });

        return v;
    }

    public Connection ConnectionDB () {

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

    public class ActualizarEstadoCupon extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarEstadoCupon(Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... strings) {


            try{


                Statement stm = ConnectionDB().createStatement();
                stm.execute("Update Cupon_Usuario set Estado='Inactivo' where ID_Cupon_Usuario=" + Id_Cupon_Usuario);



            }catch (Exception e){
                Log.e("Error", e.toString());
            }

            return true;
        }

        @Override
        protected  void onPreExecute() {

            Alert_Dialog.show();

        }

        @Override
        protected  void onPostExecute(Boolean result){

            Toast.makeText(getActivity(),"Cupon usado con exito", Toast.LENGTH_SHORT).show();

            Alert_Dialog.dismiss();

            ListaMiCupon listaMiCupon = new ListaMiCupon();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedorfragment, listaMiCupon);
            transaction.commit();


        }


    }
}
