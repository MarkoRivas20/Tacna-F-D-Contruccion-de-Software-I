package com.example.tacnafdbusiness.Adaptador;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdbusiness.Model.ItemMenu;
import com.example.tacnafdbusiness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemMenuAdapter extends RecyclerView.Adapter <ItemMenuAdapter.ItemMenuViewHolder>{

    private List<ItemMenu> Items;

    private Context Contexto;

    private OnItemClickListener Listener;

    public class ItemMenuViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView Imagen;
        public TextView Txtnombre;
        public TextView Txtdescripcion;
        public TextView Txtprecio;

        public ItemMenuViewHolder(View v) {
            super(v);
            Imagen = (ImageView) v.findViewById(R.id.Imagen_Recycler_Menu);
            Txtnombre = (TextView) v.findViewById(R.id.txtnombre_Recycler_Menu);
            Txtdescripcion = (TextView) v.findViewById(R.id.txtdescripcion_recycler_Menu);
            Txtprecio = (TextView) v.findViewById(R.id.txtprecio_Recycler_Menu);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        public void bindData (ItemMenu dataModel, Context context) {

            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen);
            Txtnombre.setText(dataModel.getNombre());
            Txtdescripcion.setText(dataModel.getDescripcion());
            Txtprecio.setText("S/. " + dataModel.getPrecio());
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

            menu.setHeaderTitle("¿Que Desea Hacer?");
            MenuItem Modificar = menu.add(Menu.NONE,1,1,"Modificar");
            MenuItem Eliminar = menu.add(Menu.NONE,2,2,"Eliminar");
            MenuItem Cancelar = menu.add(Menu.NONE,3,3,"Cancelar");

            Modificar.setOnMenuItemClickListener(this);
            Eliminar.setOnMenuItemClickListener(this);
            Cancelar.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick (MenuItem item) {


            if (Listener != null)
            {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION)
                {

                    switch (item.getItemId())
                    {
                        case 1:

                            Listener.onModificar(position);
                            return true;

                        case 2:

                            Listener.onEliminar(position);
                            return true;

                        case 3:

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
        holder.bindData(Items.get(position), Contexto);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    public interface OnItemClickListener{
        void onItemClick(int position);

        void onModificar(int position);
        void onEliminar(int position);
        void onCancelar(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.Listener = listener;

    }

}
