package com.example.tacnafddelivery.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tacnafddelivery.ListaEstablecimiento;
import com.example.tacnafddelivery.R;
import com.example.tacnafddelivery.model.Pedido;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter <PedidoAdapter.PedidoViewHolder>
        implements View.OnClickListener{

    private List<Pedido> Items;
    private Context Contexto;
    private View.OnClickListener Listener;

    public void setOnClickListener (View.OnClickListener listener){
        this.Listener=listener;
    }
    @Override
    public void onClick (View v) {
        if(Listener!=null){
            Listener.onClick(v);
        }
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder{


        public TextView Txtid_Recycler_Pedido;
        public TextView Txtestadp_Recycler_Pedido;
        public TextView Txtcliente_Recycler_Pedido;
        public TextView Txtfecha_Recycler_Pedido;
        public TextView Txtdireccion_Recycler_Pedido;
        public TextView Txtdescripcion_Recycler_Pedido;

        public PedidoViewHolder (View v) {
            super(v);

            Txtid_Recycler_Pedido = (TextView) v.findViewById(R.id.txtid_Recycler_pedido);
            Txtestadp_Recycler_Pedido = (TextView) v.findViewById(R.id.txtestado_Recycler_pedido);
            Txtcliente_Recycler_Pedido = (TextView) v.findViewById(R.id.txtcliente_Recycler_pedido);
            Txtfecha_Recycler_Pedido = (TextView) v.findViewById(R.id.txtfecha_Recycler_pedido);
            Txtdireccion_Recycler_Pedido = (TextView) v.findViewById(R.id.txtdireccion_Recycler_pedido);
            Txtdescripcion_Recycler_Pedido = (TextView) v.findViewById(R.id.txtdescripcion_Recycler_pedido);
        }

        public void bindData (Pedido dataModel, Context context) {

            Txtid_Recycler_Pedido.setText(String.valueOf(dataModel.getID_Pedido()));
            Txtestadp_Recycler_Pedido.setText("Estado: " + dataModel.getEstado());
            Txtcliente_Recycler_Pedido.setText("Cliente: " + dataModel.getUsuario_Cliente());
            Txtfecha_Recycler_Pedido.setText("Fecha: " + dataModel.getFecha());
            Txtdireccion_Recycler_Pedido.setText("Direccion: " + dataModel.getDireccion_Destino());
            Txtdescripcion_Recycler_Pedido.setText("Descripcion: " + dataModel.getDescripcion());
        }


    }

    public PedidoAdapter (List<Pedido> items, Context context) {
        this.Items = items;
        Contexto = context;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder (ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lista_pedido, parent, false);
        v.setOnClickListener(this);
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