package com.example.tacnafdcliente.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdcliente.Model.Establecimiento;
import com.example.tacnafdcliente.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListaEstablecimientoAdapter extends RecyclerView.Adapter<ListaEstablecimientoAdapter.ListaEstablecimientoViewHolder>
        implements View.OnClickListener{

    private List<Establecimiento> Items;
    private Context Contexto;
    private View.OnClickListener Listener;

    public void setOnClickListener (View.OnClickListener listener){
        this.Listener=listener;
    }
    @Override
    public void onClick (View v) {
        if(Listener!=null){
            Listener.onClick(v);
        }
    }


    public static class ListaEstablecimientoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView Imagen_Recycler_Lista;
        public ImageView Imagen_Stars;
        public ImageView Imagen_Total;
        public TextView Txtnombre_Recycler_Lista;
        public TextView Txtdireccion_recycler_Lista;
        public TextView Txttotalreseña_recycler_Lista;
        public TextView Txtpuntuacion_recycler_Lista;
        public TextView Txtcategoria_recycler_Lista;

        public ListaEstablecimientoViewHolder (View v) {
            super(v);
            Imagen_Recycler_Lista = (ImageView) v.findViewById(R.id.Imagen_Recycler_Lista);
            Imagen_Stars = (ImageView) v.findViewById(R.id.imageView3rutarecycler);
            Imagen_Total = (ImageView) v.findViewById(R.id.imageView3rutarecycler2);
            Txtnombre_Recycler_Lista = (TextView) v.findViewById(R.id.txtnombre_Recycler_Lista);
            Txtdireccion_recycler_Lista = (TextView) v.findViewById(R.id.txtdireccion_recycler_lista);
            Txttotalreseña_recycler_Lista = (TextView) v.findViewById(R.id.txttotalreseña_recycler_lista);
            Txtpuntuacion_recycler_Lista = (TextView) v.findViewById(R.id.txtpuntuacion_recycler_lista);
            Txtcategoria_recycler_Lista = (TextView) v.findViewById(R.id.txtcategoria_Recycler_Lista);
        }

        public void bindData (Establecimiento dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Imagen_Logo()).into(Imagen_Recycler_Lista);
            Imagen_Stars.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_));
            Imagen_Total.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person));
            Txtnombre_Recycler_Lista.setText(dataModel.getNombre());
            Txtdireccion_recycler_Lista.setText("Direccion: "+dataModel.getDireccion());
            Txttotalreseña_recycler_Lista.setText(String.valueOf(dataModel.getTotalResenas()));
            Txtpuntuacion_recycler_Lista.setText(String.valueOf(dataModel.getPuntuacion()));
            Txtcategoria_recycler_Lista.setText("Categoria: "+dataModel.getCategoria());
        }
    }

    public ListaEstablecimientoAdapter (List<Establecimiento> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public ListaEstablecimientoViewHolder onCreateViewHolder (ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_establecimiento, parent, false);
        v.setOnClickListener(this);
        return new ListaEstablecimientoViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ListaEstablecimientoViewHolder holder, int position) {

        holder.bindData(Items.get(position),Contexto);

    }

    public void  filterlist (ArrayList<Establecimiento> filtereslist){
        Items = filtereslist;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return Items.size();
    }

}
