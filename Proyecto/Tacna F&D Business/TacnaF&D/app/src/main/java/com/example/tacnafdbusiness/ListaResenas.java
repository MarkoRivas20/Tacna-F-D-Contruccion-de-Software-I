package com.example.tacnafdbusiness;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tacnafdbusiness.Adaptador.ReseñasAdapter;
import com.example.tacnafdbusiness.Model.ImagenEstablecimiento;
import com.example.tacnafdbusiness.Model.Reseñas;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListaResenas extends Fragment {

    public ListaResenas() {
        // Required empty public constructor
    }


    private RecyclerView Recycler_View;
    private ReseñasAdapter Adaptador;
    private RecyclerView.LayoutManager Layout_Manager;

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

    ImageView Img_Logo;
    TextView Txtnombre;
    TextView Lbltotal;
    TextView Lblpuntuacion;

    RatingBar Ratingbar_Calificacion;

    ResultSet Result_Set;
    AlertDialog Alert_Dialog;

    Button Btndescripcion;
    Button Btnperfil;
    Button Btnfoto;
    Button Btncupon;
    Button Btnmenu;


    List<Reseñas> Items;

    ProgressBar Progress_Bar_1;
    ProgressBar Progress_Bar_2;
    ProgressBar Progress_Bar_3;
    ProgressBar Progress_Bar_4;
    ProgressBar Progress_Bar_5;

    int Contador_1 = 0;
    int Contador_2 = 0;
    int Contador_3 = 0;
    int Contador_4 = 0;
    int Contador_5 = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lista_resenas, container, false);

        Recycler_View = (RecyclerView) v.findViewById(R.id.Recycler_resenas) ;
        Progress_Bar_1 = (ProgressBar) v.findViewById(R.id.progress);
        Progress_Bar_2 = (ProgressBar) v.findViewById(R.id.progress2);
        Progress_Bar_3 = (ProgressBar) v.findViewById(R.id.progress3);
        Progress_Bar_4 = (ProgressBar) v.findViewById(R.id.progress4);
        Progress_Bar_5 = (ProgressBar) v.findViewById(R.id.progress5);

        Ratingbar_Calificacion = (RatingBar) v.findViewById(R.id.ratingbarlistaresenas);
        Lblpuntuacion = (TextView) v.findViewById(R.id.lblpuntuacion);
        Lbltotal = (TextView) v.findViewById(R.id.lbltotal);

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

        Lbltotal.setText(btotalresenas);
        Lblpuntuacion.setText(bpuntuacion);
        Ratingbar_Calificacion.setRating(Float.parseFloat(bpuntuacion));

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

        Btnfoto = (Button) v.findViewById(R.id.btnimagenes);
        Btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagenesEstablecimiento imagenesEstablecimiento = new ImagenesEstablecimiento();
                imagenesEstablecimiento.setArguments(bundle2);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedorfragment, imagenesEstablecimiento);
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

        Btnmenu = (Button) v.findViewById(R.id.btnmenu);
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

        new BuscarResenaPorEstablecimiento (getActivity()).execute(new String[]{"Listar"});




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

    public class BuscarResenaPorEstablecimiento extends AsyncTask<String, Integer, Boolean> {


        private Context mContext = null;

        BuscarResenaPorEstablecimiento (Context context){
            mContext = context;
        }

        @Override
        protected Boolean doInBackground (String... strings) {




            try {

                Statement stm2 = ConectarDB().createStatement();
                Result_Set = stm2.executeQuery("select r.ID_Resena,u.Nombre+' '+u.Apellido as Nombre ,r.ID_Establecimiento,r.Fecha,r.Descripcion,r.Calificacion " +
                        "from Resena r inner join Usuario_Cliente u on r.ID_Usuario_Cliente=u.ID_Usuario_Cliente where ID_Establecimiento=" + bid_establecimiento);

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

            RellenarLista();

            Alert_Dialog.dismiss();


        }


    }

    public void RellenarLista(){
        try {


            Items = new ArrayList<>();

            while (Result_Set.next()){
                Reseñas reseñas = new Reseñas();
                reseñas.setID_Resena(Result_Set.getInt(1));
                reseñas.setID_Usuario_Cliente(Result_Set.getString(2));
                reseñas.setID_Establecimiento(Result_Set.getInt(3));
                reseñas.setFecha(Result_Set.getDate(4));
                reseñas.setDescripcion(Result_Set.getString(5));
                reseñas.setCalificacion(Result_Set.getDouble(6));

                Items.add(reseñas);

                if (Result_Set.getDouble(6) <= 1.0)
                {
                    Contador_1++;
                }
                else if (Result_Set.getDouble(6) <= 2.0)
                {
                    Contador_2++;
                }
                else if (Result_Set.getDouble(6) <= 3.0)
                {
                    Contador_3++;
                }
                else if (Result_Set.getDouble(6) <= 4.0)
                {
                    Contador_4++;
                }
                else if (Result_Set.getDouble(6) <= 5.0)
                {
                    Contador_5++;
                }


            }


            Recycler_View.setHasFixedSize(true);
            Layout_Manager = new LinearLayoutManager(getActivity());
            Recycler_View.setLayoutManager(Layout_Manager);

            Adaptador = new ReseñasAdapter(Items, getActivity());


            Recycler_View.setAdapter(Adaptador);


            Progress_Bar_1.setMax(Items.size());
            Progress_Bar_2.setMax(Items.size());
            Progress_Bar_3.setMax(Items.size());
            Progress_Bar_4.setMax(Items.size());
            Progress_Bar_5.setMax(Items.size());

            Progress_Bar_1.setProgress(Contador_1);
            Progress_Bar_2.setProgress(Contador_2);
            Progress_Bar_3.setProgress(Contador_3);
            Progress_Bar_4.setProgress(Contador_4);
            Progress_Bar_5.setProgress(Contador_5);




        }catch (Exception e){
            Log.e("Error", e.toString());
        }
    }


}
