package com.example.tacnafdcliente.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdcliente.Model.Cupon;
import com.example.tacnafdcliente.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CuponAdapter extends RecyclerView.Adapter<CuponAdapter.CuponViewHolder>{
    private List<Cupon> Items;

    private Context Contexto;

    public class CuponViewHolder extends RecyclerView.ViewHolder{
        // Campos respectivos de un item
        public ImageView Imagen;
        public TextView Txttitulo;
        public TextView Txtdescripcion;
        public TextView Txtfechainicio;
        public TextView Txtfechafinal;

        public CuponViewHolder (View v) {
            super(v);
            Imagen = (ImageView) v.findViewById(R.id.Imagen_Recycler_cupon);
            Txttitulo = (TextView)v.findViewById(R.id.txttitulo_Recycler_cupon);
            Txtdescripcion = (TextView)v.findViewById(R.id.txtdescripcion_recycler_cupon);
            Txtfechainicio = (TextView)v.findViewById(R.id.txtfechainicio_Recycler_cupon);
            Txtfechafinal = (TextView)v.findViewById(R.id.txtfechafinal_Recycler_cupon);
        }

        public void bindData (Cupon dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen);
            Txttitulo.setText(dataModel.getTitulo());
            Txtdescripcion.setText(dataModel.getDescripcion());
            Txtfechainicio.setText("Fecha Inicio: "+dataModel.getFecha_Inicio());
            Txtfechafinal.setText("Fecha Final: "+dataModel.getFecha_Final());
        }



    }


    public CuponAdapter (List<Cupon> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public CuponViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_cupones, parent, false);
        return new CuponViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull CuponViewHolder holder, int position) {
        holder.bindData(Items.get(position),Contexto);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
