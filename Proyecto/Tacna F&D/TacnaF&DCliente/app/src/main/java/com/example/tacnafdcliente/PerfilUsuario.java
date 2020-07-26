package com.example.tacnafdcliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

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
    Button Btnprincipal;
    Button Btnactualizar;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

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



        Lblbienvenida = (TextView) v.findViewById(R.id.lblbienvenida);


        Lblbienvenida.setText("¡Hola " + Nombre + "!");


        Txtemail = (EditText) v.findViewById(R.id.txtemail);
        Txtclave = (EditText) v.findViewById(R.id.txtclave);
        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtapellido = (EditText) v.findViewById(R.id.txtapellido);

        Txtemail.setText(Email);
        Txtclave.setText(Clave);
        Txtnombre.setText(Nombre);
        Txtapellido.setText(Apellido);

        Txtemail.setEnabled(false);

        Btnactualizar = (Button) v.findViewById(R.id.btnmodificar);

        Btnactualizar.setOnClickListener(new View.OnClickListener() {
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

        Btncerrar = (Button) v.findViewById(R.id.btncerrar);

        Btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String IDa = "";
                final String nombrea = "";
                final String apellidoa = "";
                final String emaila = "";
                final String clavea = "";
                SaveLoginSharedPreferences(IDa, emaila, clavea, nombrea, apellidoa);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        Btnprincipal = (Button) v.findViewById(R.id.btnprincipal);
        Btnprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, pantallaPrincipal);
                transaction.commit();
            }
        });

        Btnestablecimiento = (Button) v.findViewById(R.id.btnestablecimientos);
        Btnestablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarEstablecimiento buscarEstablecimiento = new BuscarEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, buscarEstablecimiento);
                transaction.commit();
            }
        });


        return v;
    }

    public Connection ConnectionDB(){

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

    public class ActualizarUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConnectionDB().createStatement();
                stm2.execute("Update Usuario_Cliente set Contrasena='" + Txtclave.getText().toString() + "',Nombre='" + Txtnombre.getText().toString() + "'" +
                        ",Apellido='" + Txtapellido.getText().toString() + "' where ID_Usuario_Cliente='" + Id + "'");

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
            SaveLoginSharedPreferences(Id, Email, Txtclave.getText().toString(), Txtnombre.getText().toString(), Txtapellido.getText().toString());

            Alert_Dialog.dismiss();

        }


    }


    private void SaveLoginSharedPreferences (String ID, String email, String clave, String nombre, String apellido){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("email", email);
        editor.putString("clave", clave);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);

        editor.apply();
    }

    private String GetFromSharedPreferences (String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
