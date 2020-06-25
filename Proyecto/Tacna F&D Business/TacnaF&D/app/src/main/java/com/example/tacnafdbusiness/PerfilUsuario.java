package com.example.tacnafdbusiness;

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

    Button Btnestablcimiento;
    Button Btnactualizar;
    Button Btncerrar;

    EditText Txtemail;
    EditText Txtclave;
    EditText Txtnombre;
    EditText Txtapellido;
    EditText Txtcelular;
    EditText Txtruc;
    EditText Txtcodigo_Paypal;

    AlertDialog Alert_Dialog;

    String Id = "";
    String Email = "";
    String Clave = "";
    String Nombre = "";
    String Apellido = "";
    String Celular = "";
    String Ruc = "";
    String Codigo_Paypal = "";


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        Celular = GetFromSharedPreferences("celular");
        Ruc = GetFromSharedPreferences("ruc");
        Codigo_Paypal = GetFromSharedPreferences("codigopaypal");


        Lblbienvenida = (TextView) v.findViewById(R.id.lblbienvenida);


        Lblbienvenida.setText("¡Hola " + Nombre + "!");


        Txtemail = (EditText) v.findViewById(R.id.txtemail);
        Txtclave = (EditText) v.findViewById(R.id.txtclave);
        Txtnombre = (EditText) v.findViewById(R.id.txtnombre);
        Txtapellido = (EditText) v.findViewById(R.id.txtapellido);
        Txtcelular = (EditText) v.findViewById(R.id.txtcelular);
        Txtruc = (EditText) v.findViewById(R.id.txtruc);
        Txtcodigo_Paypal = (EditText) v.findViewById(R.id.txtcodigopaypal);

        Txtemail.setText(Email);
        Txtclave.setText(Clave);
        Txtnombre.setText(Nombre);
        Txtapellido.setText(Apellido);
        Txtcelular.setText(Celular);
        Txtruc.setText(Ruc);
        Txtcodigo_Paypal.setText(Codigo_Paypal);

        Txtemail.setEnabled(false);


        Btnestablcimiento = (Button) v.findViewById(R.id.btnestablecimientos);

        Btnestablcimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaEstablecimientos fragmentEstablecimiento = new ListaEstablecimientos();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, fragmentEstablecimiento);
                transaction.commit();
            }
        });

        Btnactualizar = (Button) v.findViewById(R.id.btnmodificar);

        Btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Txtclave.length() == 0 || Txtnombre.length() == 0 || Txtapellido.length() == 0
                        || Txtcelular.length() == 0 || Txtruc.length() == 0 || Txtcodigo_Paypal.length() == 0)
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
                    if (Txtcelular.length() == 0)
                    {
                        Txtcelular.setError("Espacio Requerido");
                    }
                    else
                    {

                    }
                    if (Txtruc.length() == 0)
                    {
                        Txtruc.setError("Espacio Requerido");
                    }
                    else
                    {

                    }
                    if (Txtcodigo_Paypal.length() == 0)
                    {
                        Txtcodigo_Paypal.setError("Espacio Requerido");
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
                final String celulara = "";
                final String ruca = "";
                final String codigopaypala = "";
                SaveLoginSharedPreferences(IDa, emaila, clavea, nombrea, apellidoa, celulara, ruca, codigopaypala);
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

    public class ActualizarUsuario extends AsyncTask <String, Integer, Boolean> {


        private Context mContext = null;

        ActualizarUsuario (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {



            try {

                Statement stm2 = ConectarDB().createStatement();
                stm2.execute("Update Usuario_Propietario set Contrasena='" + Txtclave.getText().toString() + "',Nombre='" + Txtnombre.getText().toString() + "'" +
                        ",Apellido='" + Txtapellido.getText().toString() + "',Celular='" + Txtcelular.getText().toString() + "',RUC='" + Txtruc.getText().toString() +
                        "',Codigo_PayPal='" + Txtcodigo_Paypal.getText().toString() + "' where ID_Usuario_Propietario='" + Id + "'");

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
                    Txtcelular.getText().toString(), Txtruc.getText().toString(), Txtcodigo_Paypal.getText().toString());

            Alert_Dialog.dismiss();

        }


    }


    private void SaveLoginSharedPreferences(String ID, String email, String clave, String nombre, String apellido, String celular, String RUC, String CodigoPaypal){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID", ID);
        editor.putString("email", email);
        editor.putString("clave", clave);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("celular", celular);
        editor.putString("ruc", RUC);
        editor.putString("codigopaypal", CodigoPaypal);

        editor.apply();
    }

    private String GetFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("login_usuario", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}