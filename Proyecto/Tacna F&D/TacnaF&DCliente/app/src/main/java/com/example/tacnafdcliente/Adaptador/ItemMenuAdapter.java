package com.example.tacnafdcliente.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdcliente.Model.ItemMenu;
import com.example.tacnafdcliente.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemMenuAdapter extends RecyclerView.Adapter<ItemMenuAdapter.ItemMenuViewHolder>{

    private List<ItemMenu> Items;

    private Context Contexto;

    public class ItemMenuViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView Imagen;
        public TextView Txtnombre;
        public TextView Txtdescripcion;
        public TextView Txtprecio;

        public ItemMenuViewHolder (View v) {
            super(v);
            Imagen = (ImageView) v.findViewById(R.id.Imagen_Recycler_Menu);
            Txtnombre = (TextView) v.findViewById(R.id.txtnombre_Recycler_Menu);
            Txtdescripcion = (TextView) v.findViewById(R.id.txtdescripcion_recycler_Menu);
            Txtprecio = (TextView) v.findViewById(R.id.txtprecio_Recycler_Menu);
        }

        public void bindData (ItemMenu dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen);
            Txtnombre.setText(dataModel.getNombre());
            Txtdescripcion.setText(dataModel.getDescripcion());
            Txtprecio.setText("S/. "+dataModel.getPrecio());
        }



    }


    public ItemMenuAdapter (List<ItemMenu> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public ItemMenuViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_menu, parent, false);
        return new ItemMenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ItemMenuViewHolder holder, int position) {
        holder.bindData(Items.get(position),Contexto);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

}
