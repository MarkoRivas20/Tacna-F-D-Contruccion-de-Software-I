package com.example.tacnafddelivery.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafddelivery.R;
import com.example.tacnafddelivery.model.Establecimiento;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EstablecimientoAdapter extends RecyclerView.Adapter <EstablecimientoAdapter.EstablecimientoViewHolder>
        implements View.OnClickListener{

    private List<Establecimiento> Items;
    private Context Contexto;
    private View.OnClickListener Listener;

    public void setOnClickListener (View.OnClickListener listener){
        this.Listener = listener;
    }
    @Override
    public void onClick (View v) {
        if (Listener != null)
        {
            Listener.onClick(v);
        }
        else
        {

        }
    }
    public static class EstablecimientoViewHolder extends RecyclerView.ViewHolder {

        public ImageView Imagen_Recycler_Lista;
        public TextView Txtnombre_Recycler_Lista;
        public TextView Txtdireccion_recycler_lista;
        public TextView Txtdistrito_recycler_lista;

        public EstablecimientoViewHolder (View v) {
            super(v);

            Imagen_Recycler_Lista = (ImageView) v.findViewById(R.id.Imagen_Recycler_Lista);
            Txtnombre_Recycler_Lista = (TextView) v.findViewById(R.id.txtnombre_Recycler_Lista);
            Txtdireccion_recycler_lista = (TextView) v.findViewById(R.id.txtdireccion_Recycler_Lista);
            Txtdistrito_recycler_lista = (TextView) v.findViewById(R.id.txtdistrito_recycler_lista);
        }

        public void bindData (Establecimiento dataModel, Context context) {

            Picasso.with(context).load(dataModel.getUrl_Imagen_Logo()).into(Imagen_Recycler_Lista);
            Txtnombre_Recycler_Lista.setText(dataModel.getNombre());
            Txtdireccion_recycler_lista.setText("Direccion: " + dataModel.getDireccion());
            Txtdistrito_recycler_lista.setText("Distrito: " + dataModel.getDistrito());
        }
    }

    public EstablecimientoAdapter (List<Establecimiento> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public EstablecimientoViewHolder onCreateViewHolder (ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_establecimiento, parent, false);
        v.setOnClickListener(this);
        return new EstablecimientoViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull EstablecimientoViewHolder holder, int position) {

        holder.bindData(Items.get(position), Contexto);

    }
    @Override
    public int getItemCount() {
        return Items.size();
    }
}
