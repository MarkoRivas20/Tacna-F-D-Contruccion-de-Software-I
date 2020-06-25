package com.example.tacnafdbusiness.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdbusiness.Model.Reseñas;
import com.example.tacnafdbusiness.R;

import java.util.List;

public class ReseñasAdapter extends RecyclerView.Adapter <ReseñasAdapter.ReseñasViewHolder>{

    private List<Reseñas> Items;
    private Context Contexto;


    public static class ReseñasViewHolder extends RecyclerView.ViewHolder {

        public RatingBar Ratingbar_Resenas;
        public TextView Txtnombre_Recycler_Resena;
        public TextView Txtdescripcion_Recycler_Resena;
        public TextView Txtfecha_Recycler_Resena;
        public TextView Txtpuntuacion_Recycler_Resena;

        public ReseñasViewHolder (View v) {
            super(v);

            Ratingbar_Resenas = (RatingBar) v.findViewById(R.id.ratingbar_resenas);
            Txtnombre_Recycler_Resena = (TextView) v.findViewById(R.id.txtnombre_Recycler_resena);
            Txtdescripcion_Recycler_Resena = (TextView) v.findViewById(R.id.txtdescripcion_recycler_resena);
            Txtfecha_Recycler_Resena = (TextView) v.findViewById(R.id.txtfecha_Recycler_resena);
            Txtpuntuacion_Recycler_Resena = (TextView) v.findViewById(R.id.txtpuntuacion_recycler_resena);
        }

        public void bindData (Reseñas dataModel, Context context) {

            Ratingbar_Resenas.setRating(dataModel.getCalificacion().floatValue());
            Txtnombre_Recycler_Resena.setText(dataModel.getID_Usuario_Cliente());
            Txtdescripcion_Recycler_Resena.setText(dataModel.getDescripcion());
            Txtfecha_Recycler_Resena.setText(String.valueOf(dataModel.getFecha()));
            Txtpuntuacion_Recycler_Resena.setText(String.valueOf(dataModel.getCalificacion()));
        }
    }

    public ReseñasAdapter (List<Reseñas> items, Context context) {
        this.Items = items;
        Contexto = context;
    }

    @Override
    public ReseñasViewHolder onCreateViewHolder (ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_resenas, parent, false);

        return new ReseñasViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ReseñasViewHolder holder, int position) {

        holder.bindData(Items.get(position), Contexto);

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
