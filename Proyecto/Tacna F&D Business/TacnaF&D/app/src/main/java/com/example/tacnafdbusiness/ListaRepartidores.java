package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tacnafdbusiness.Adaptador.RepartidorAdapter;
import com.example.tacnafdbusiness.Model.Repartidores;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaRepartidores extends Fragment {

    public ListaRepartidores() {
        // Required empty public constructor
    }

    String bid_establecimiento = "";

    Button Btnatras;
    Button Btnanadir;

    EditText Txtid_Repartidor;

    ResultSet Result_Set;
    ResultSet Result_Set2;

    private RecyclerView Recycler_View;
    private RepartidorAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

    List<Repartidores> Items;

    FirebaseDatabase Firebase_Database;
    DatabaseReference Database_Reference;
    StorageReference Storage_Reference;
    FirebaseStorage Firebase_Storage;

    AlertDialog Alert_Dialog;

    AlertDialog.Builder Mensaje;

    boolean Booleano = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_repartidores, container, false);

        InicializarFirebase();

        Mensaje = new AlertDialog.Builder(getActivity());
        Mensaje.setTitle("Agregar Repartidor");
        Mensaje.setCancelable(false);
        Mensaje.setPositiveButton("Si, estoy seguro", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                AgregarRepartidor agregarRepartidor= new AgregarRepartidor(getActivity().getApplicationContext());
                agregarRepartidor.execute(Txtid_Repartidor.getText().toString(),bid_establecimiento);

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

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_ListaRepartidor) ;

        Txtid_Repartidor = (EditText) v.findViewById(R.id.txtidrepartidor);
        Btnatras = (Button) v.findViewById(R.id.btnatras);
        Btnanadir= (Button) v.findViewById(R.id.btnagregarrepartidor);



        bid_establecimiento = GetInfoFromSharedPreferences("ID");


        Btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerfilEstablecimiento perfilEstablecimiento = new PerfilEstablecimiento();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, perfilEstablecimiento);
                transaction.commit();

            }
        });

        Btnanadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    for (int i=0; i<Items.size(); i++){

                        if(Txtid_Repartidor.getText().toString().equals(String.valueOf(Items.get(i).getID_Usuario_Repartidor()))){
                            Toast.makeText(getActivity().getApplicationContext(),"Este repartidor ya fue agregado",Toast.LENGTH_SHORT).show();
                            Booleano = true;
                            break;
                        }
                        else
                        {
                            Booleano = false;
                        }
                    }
                    if(!Booleano){
                        BuscarRepartidor buscarRepartidor= new BuscarRepartidor(getActivity().getApplicationContext());
                        buscarRepartidor.execute(Txtid_Repartidor.getText().toString());
                    }


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

        BuscarRepartidores();


        Adaptador.setOnItemClickListener(new RepartidorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick (int position) {
                Toast.makeText(getActivity(),"Manten presionado para ver las opciones", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQuitar(int position) {
                String Id_Repartidor_Establecimiento = String.valueOf(Items.get(position).getID_Repartidor_Establecimiento());
                QuitarRepartidor asy= new QuitarRepartidor(getActivity().getApplicationContext());
                asy.execute(Id_Repartidor_Establecimiento);
            }

            @Override
            public void onCancelar(int position) {

            }


        });

        Recycler_View.setAdapter(Adaptador);


        return v;
    }

    private void BuscarRepartidores() {
        try {

            Statement stm2 = ConectarDB().createStatement();
            Result_Set = stm2.executeQuery("select re.ID_Repartidor_Establecimiento, us.ID_Usuario_Repartidor,us.Nombre, us.Apellido,us.Url_Foto " +
                    "from Repartidor_Establecimiento re inner join Usuario_Repartidor us on re.ID_Usuario_Repartidor=us.ID_Usuario_Repartidor " +
                    "where re.ID_Establecimiento=" + bid_establecimiento);

            Items = new ArrayList<>();

            while (Result_Set.next()){

                Repartidores repartidores = new Repartidores();
                repartidores.setID_Repartidor_Establecimiento(Result_Set.getInt(1));
                repartidores.setID_Usuario_Repartidor(Result_Set.getInt(2));
                repartidores.setNombre(Result_Set.getString(3));
                repartidores.setApellido(Result_Set.getString(4));
                repartidores.setUrl_Foto(Result_Set.getString(5));
                Items.add(repartidores);
            }

            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new RepartidorAdapter(Items,getActivity());




        }catch (Exception e){
            Log.e("Error", e.toString());
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

    public class QuitarRepartidor extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        QuitarRepartidor (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                String Id_Repartidor_Establecimiento=strings[0];
                Statement stm = ConectarDB().createStatement();
                stm.execute("delete from Repartidor_Establecimiento where ID_Repartidor_Establecimiento = " + Id_Repartidor_Establecimiento);

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


            Alert_Dialog.dismiss();

            BuscarRepartidores();
            Recycler_View.setAdapter(Adaptador);

        }


    }

    public class BuscarRepartidor extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        BuscarRepartidor (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                String Id_Repartidor = strings[0];
                Statement stm3 = ConectarDB().createStatement();
                Result_Set2 = stm3.executeQuery("select * from Usuario_Repartidor where ID_Usuario_Repartidor=" + Id_Repartidor);

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


            Alert_Dialog.dismiss();

            try{

                if (Result_Set2.next())
                {

                    Mensaje.setMessage("¿Esta seguro que desea añadir a " + Result_Set2.getString(4) + " " + Result_Set2.getString(5) + "?");
                    Mensaje.show();
                }
                else
                {
                        Toast.makeText(getActivity(),"No existe este ID", Toast.LENGTH_SHORT).show();
                }


            }catch (Exception e){
                Log.e("Error", e.toString());
            }




        }


    }

    public class AgregarRepartidor extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        AgregarRepartidor (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {

            try {

                String Id_Usuario_Repartidor = strings[0];
                String Id_Establecimiento = strings[1];
                Statement stm4 = ConectarDB().createStatement();
                stm4.executeQuery("insert into Repartidor_Establecimiento(ID_Usuario_Repartidor,ID_Establecimiento) values("+Id_Usuario_Repartidor+","+Id_Establecimiento+")");

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


            Alert_Dialog.dismiss();

            Toast.makeText(getActivity(),"Repartidor Agregado con exito", Toast.LENGTH_SHORT).show();

            BuscarRepartidores();
            Recycler_View.setAdapter(Adaptador);



        }


    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }
}
