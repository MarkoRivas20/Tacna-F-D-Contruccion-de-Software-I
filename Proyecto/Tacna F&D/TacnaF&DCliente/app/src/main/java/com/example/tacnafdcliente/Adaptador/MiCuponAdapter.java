package com.example.tacnafdcliente.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tacnafdcliente.Model.MiCupon;
import com.example.tacnafdcliente.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MiCuponAdapter extends RecyclerView.Adapter<MiCuponAdapter.MiCuponViewHolder> implements View.OnClickListener{


    private List<MiCupon> Items;

    private Context Contexto;
    private View.OnClickListener Listener;

    public void setOnClickListener (View.OnClickListener listener){
        this.Listener=listener;
    }
    @Override
    public void onClick (View v) {
        if(Listener!=null)
        {
            Listener.onClick(v);
        }
        else
        {

        }
    }

    public class MiCuponViewHolder extends RecyclerView.ViewHolder{
        // Campos respectivos de un item
        public ImageView Imagen;
        public TextView Txttitulo;
        public TextView Txtnombre;
        public TextView Txtdescripcion;
        public TextView Txtfechainicio;
        public TextView Txtfechafinal;
        public TextView Txtporcentaje_Descuento;

        public MiCuponViewHolder (View v) {
            super(v);
            Imagen = (ImageView) v.findViewById(R.id.Imagen_Recycler_micupon);
            Txttitulo = (TextView) v.findViewById(R.id.txttitulo_Recycler_micupon);
            Txtnombre = (TextView) v.findViewById(R.id.txtnombre_Recycler_micupon);
            Txtdescripcion = (TextView) v.findViewById(R.id.txtdescripcion_recycler_micupon);
            Txtfechainicio = (TextView) v.findViewById(R.id.txtfechainicio_Recycler_micupon);
            Txtfechafinal = (TextView) v.findViewById(R.id.txtfechafinal_Recycler_micupon);
            Txtporcentaje_Descuento = (TextView) v.findViewById(R.id.txtporcentajedescuento_Recycler_micupon);
        }

        public void bindData (MiCupon dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen);
            Txttitulo.setText(dataModel.getTitulo());
            Txtnombre.setText("Establecimiento: "+dataModel.getNombre());
            Txtdescripcion.setText("Descripcion: "+dataModel.getDescripcion());
            Txtfechainicio.setText("Fecha Inicio: "+dataModel.getFecha_Inicio());
            Txtfechafinal.setText("Fecha Final: "+dataModel.getFecha_Final());
            Txtporcentaje_Descuento.setText("Porcentaje de Descuento: "+dataModel.getPorcentaje_Descuento()+"%");
        }

    }

    public MiCuponAdapter (List<MiCupon> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public MiCuponViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_mi_cupon, parent, false);
        v.setOnClickListener(this);
        return new MiCuponViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull MiCuponViewHolder holder, int position) {
        holder.bindData(Items.get(position),Contexto);
    }

    public void  filterlist (ArrayList<MiCupon> filtereslist){
        Items = filtereslist;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return Items.size();
    }


}
