package com.example.tacnafdbusiness;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


public class VisualizarDocumento extends Fragment {



    public VisualizarDocumento() {
        // Required empty public constructor
    }

    PhotoView Imgdocumento;

    String Url_Imagen_Documento = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_visualizar_documento, container, false);

        Imgdocumento = (PhotoView) v.findViewById(R.id.imgdocumento);


        Url_Imagen_Documento = GetInfoFromSharedPreferences("Url_Imagen_Documento");

        Picasso.with(getActivity()).load(Url_Imagen_Documento).into(Imgdocumento);

        Imgdocumento.setScaleType(ImageView.ScaleType.FIT_CENTER);

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


        return v;
    }

    private String GetInfoFromSharedPreferences(String Key){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("info_establecimiento", Context.MODE_PRIVATE);
        return sharedPref.getString(Key,"");
    }

}
