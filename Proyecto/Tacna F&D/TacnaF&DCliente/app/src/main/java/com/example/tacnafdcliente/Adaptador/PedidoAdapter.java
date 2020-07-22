package com.example.tacnafdcliente.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafdcliente.Model.Pedido;
import com.example.tacnafdcliente.R;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter <PedidoAdapter.PedidoViewHolder>{

    private List<Pedido> Items;
    private Context Contexto;

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {


        public TextView Txtid_Recycler_Pedido;
        public TextView Txtestadp_Recycler_Pedido;
        public TextView Txtestablecimiento_Recycler_Pedido;
        public TextView Txtfecha_Recycler_Pedido;
        public TextView Txtdireccion_Recycler_Pedido;
        public TextView Txtprecio_Recycler_Pedido;
        public TextView Txtdescripcion_Recycler_Pedido;

        public PedidoViewHolder (View v) {
            super(v);

            Txtid_Recycler_Pedido = (TextView) v.findViewById(R.id.txtid_Recycler_pedido);
            Txtestadp_Recycler_Pedido = (TextView) v.findViewById(R.id.txtestado_Recycler_pedido);
            Txtestablecimiento_Recycler_Pedido = (TextView) v.findViewById(R.id.txtestablecimiento_Recycler_pedido);
            Txtfecha_Recycler_Pedido = (TextView) v.findViewById(R.id.txtfecha_Recycler_pedido);
            Txtdireccion_Recycler_Pedido = (TextView) v.findViewById(R.id.txtdireccion_Recycler_pedido);
            Txtprecio_Recycler_Pedido = (TextView) v.findViewById(R.id.txtprecio_Recycler_pedido);
            Txtdescripcion_Recycler_Pedido = (TextView) v.findViewById(R.id.txtdescripcion_Recycler_pedido);
        }

        public void bindData (Pedido dataModel, Context context) {

            Txtid_Recycler_Pedido.setText(String.valueOf(dataModel.getID_Pedido()));
            Txtestadp_Recycler_Pedido.setText("Estado: " + dataModel.getEstado());
            Txtestablecimiento_Recycler_Pedido.setText("Establecimiento: " + dataModel.getNombre_Establecimiento());
            Txtfecha_Recycler_Pedido.setText("Fecha: " + dataModel.getFecha());
            Txtdireccion_Recycler_Pedido.setText("Direccion: " + dataModel.getDireccion_Destino());
            Txtprecio_Recycler_Pedido.setText("Precio: S/."+ dataModel.getPrecio_Total());
            Txtdescripcion_Recycler_Pedido.setText("Descripcion: " + dataModel.getDescripcion());
        }
    }

    public PedidoAdapter (List<Pedido> items, Context context) {
        this.Items = items;
        Contexto = context;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder (ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_pedidos, parent, false);

        return new PedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull PedidoViewHolder holder, int position) {

        holder.bindData(Items.get(position), Contexto);

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

}