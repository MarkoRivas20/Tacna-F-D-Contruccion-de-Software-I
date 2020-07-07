package com.example.tacnafdcliente.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdcliente.Model.Carrusel;
import com.example.tacnafdcliente.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarruselAdapter extends RecyclerView.Adapter<CarruselAdapter.CarruselViewHolder>{


    private List<Carrusel> Items;
    private Context Contexto;


    public static class CarruselViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView Imagen_Carrusel;

        public CarruselViewHolder (View v) {
            super(v);
            Imagen_Carrusel = (ImageView) v.findViewById(R.id.imageViewCarrusel);

        }
        public void bindData (Carrusel dataModel, Context context){
            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen_Carrusel);
        }

    }

    public CarruselAdapter (List<Carrusel> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public CarruselViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_carrusel_imagenes, parent, false);

        return new CarruselViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull CarruselViewHolder holder, int position) {
        holder.bindData(Items.get(position),Contexto);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
