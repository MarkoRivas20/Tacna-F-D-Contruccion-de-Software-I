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

import com.example.tacnafdbusiness.Model.Cupon;
import com.example.tacnafdbusiness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CuponAdapter extends RecyclerView.Adapter <CuponAdapter.CuponViewHolder>{

    private List<Cupon> Items;

    private Context Contexto;

    private OnItemClickListener Listener;

    public class CuponViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView Imagen;
        public TextView Txttitulo;
        public TextView Txtdescripcion;
        public TextView Txtfechainicio;
        public TextView Txtfechafinal;
        public TextView Txtestado;

        public CuponViewHolder (View v) {
            super(v);

            Imagen = (ImageView) v.findViewById(R.id.Imagen_Recycler_cupon);
            Txttitulo = (TextView) v.findViewById(R.id.txttitulo_Recycler_cupon);
            Txtdescripcion = (TextView) v.findViewById(R.id.txtdescripcion_recycler_cupon);
            Txtfechainicio = (TextView) v.findViewById(R.id.txtfechainicio_Recycler_cupon);
            Txtfechafinal = (TextView) v.findViewById(R.id.txtfechafinal_Recycler_cupon);
            Txtestado = (TextView) v.findViewById(R.id.txtestado_Recycler_cupon);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        public void bindData (Cupon dataModel, Context context) {
            Picasso.with(context).load(dataModel.getUrl_Imagen()).into(Imagen);
            Txttitulo.setText(dataModel.getTitulo());
            Txtdescripcion.setText(dataModel.getDescripcion());
            Txtfechainicio.setText("Fecha Inicio: " + dataModel.getFecha_Inicio());
            Txtfechafinal.setText("Fecha Final: " + dataModel.getFecha_Final());
            Txtestado.setText("Estado: " + dataModel.getEstado());
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
            MenuItem modificar = menu.add(Menu.NONE,1,1,"Modificar");
            MenuItem cancelar = menu.add(Menu.NONE,2,2,"Cancelar");

            modificar.setOnMenuItemClickListener(this);
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

                            Listener.onModificar(position);
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
        holder.bindData(Items.get(position), Contexto);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }


    public interface OnItemClickListener{

        void onItemClick(int position);
        void onModificar(int position);
        void onCancelar(int position);

    }

    public void setOnItemClickListener (OnItemClickListener listener){

        this.Listener=listener;

    }
}