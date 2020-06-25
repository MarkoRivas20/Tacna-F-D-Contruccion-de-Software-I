package com.example.tacnafdbusiness.Adaptador;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdbusiness.Model.ImagenEstablecimiento;
import com.example.tacnafdbusiness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagenesEstablecimientoAdapter extends RecyclerView.Adapter <ImagenesEstablecimientoAdapter.ImagenesEstablecimientoViewHolder> {

    private List<ImagenEstablecimiento> Items;

    private Context Contexto;

    private OnItemClickListener Listener;

    public class ImagenesEstablecimientoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView Imagen;

        public ImagenesEstablecimientoViewHolder (View v) {
            super(v);
            Imagen = (ImageView) v.findViewById(R.id.imageViewimagenestablecimiento);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        public void bindData (ImagenEstablecimiento dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen);
        }

        @Override
        public void onClick(View v) {


            if (Listener != null)
            {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {
                    Listener.onItemClick(position);
                }
                else
                {

                }
            }
            else
            {

            }


        }

        @Override
        public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("¿Desea Eliminarla?");
            MenuItem eliminar = menu.add(Menu.NONE,1,1,"Si");
            MenuItem noeliminar = menu.add(Menu.NONE,2,2,"No");

            eliminar.setOnMenuItemClickListener(this);
            noeliminar.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick (MenuItem item) {


            if (Listener != null)
            {
                int position=getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {

                    switch (item.getItemId())
                    {
                        case 1:

                            Listener.onEliminar(position);
                            return true;

                        case 2:

                            Listener.onNoEliminar(position);
                            return true;


                    }

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
    }

    public ImagenesEstablecimientoAdapter (List<ImagenEstablecimiento> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public ImagenesEstablecimientoViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_imagenes, parent, false);
        return new ImagenesEstablecimientoViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ImagenesEstablecimientoViewHolder holder, int position) {
        holder.bindData(Items.get(position), Contexto);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    public interface OnItemClickListener{
        void onItemClick(int position);

        void onEliminar(int position);
        void onNoEliminar(int position);

    }

    public void setOnItemClickListener (OnItemClickListener listener){

        this.Listener=listener;

    }
}
