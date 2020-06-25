package com.example.tacnafdbusiness.Adaptador;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tacnafdbusiness.Model.Repartidores;
import com.example.tacnafdbusiness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RepartidorAdapter extends RecyclerView.Adapter <RepartidorAdapter.RepartidorViewHolder>{

    private List<Repartidores> Items;

    private Context Contexto;

    private RepartidorAdapter.OnItemClickListener Listener;

    public class RepartidorViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView Imagen;
        public TextView Txtid;
        public TextView Txtnombre;
        public TextView Txtapellido;

        public RepartidorViewHolder (View v) {
            super(v);

            Imagen = (ImageView) v.findViewById(R.id.Imagen_Recycler_repartidor);
            Txtid = (TextView) v.findViewById(R.id.txtid_Recycler_repartidor);
            Txtnombre = (TextView) v.findViewById(R.id.txtnombre_Recycler_repartidor);
            Txtapellido = (TextView) v.findViewById(R.id.txtapellido_recycler_repartidor);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        public void bindData (Repartidores dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Foto()).into(Imagen);
            Txtid.setText("ID: " + dataModel.getID_Usuario_Repartidor());
            Txtnombre.setText("Nombre: " + dataModel.getNombre());
            Txtapellido.setText("Apellido: " + dataModel.getApellido());
        }

        @Override
        public void onClick (View v) {


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

            menu.setHeaderTitle("¿Que Desea Hacer?");
            MenuItem quitar = menu.add(Menu.NONE,1,1,"Quitar");
            MenuItem cancelar = menu.add(Menu.NONE,2,2,"Cancelar");

            quitar.setOnMenuItemClickListener(this);
            cancelar.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {


            if (Listener != null)
            {

                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                {

                    switch (item.getItemId())
                    {
                        case 1:

                            Listener.onQuitar(position);
                            return true;

                        case 2:

                            Listener.onCancelar(position);
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

    public RepartidorAdapter (List<Repartidores> items, Context context) {
        this.Items = items;
        Contexto = context;
    }


    @Override
    public RepartidorViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_repartidores, parent, false);
        return new RepartidorViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull RepartidorViewHolder holder, int position) {
        holder.bindData(Items.get(position), Contexto);
    }


    @Override
    public int getItemCount() {
        return Items.size();
    }


    public interface OnItemClickListener{

        void onItemClick(int position);
        void onQuitar(int position);
        void onCancelar(int position);

    }

    public void setOnItemClickListener (OnItemClickListener listener){

        this.Listener=listener;

    }


}
