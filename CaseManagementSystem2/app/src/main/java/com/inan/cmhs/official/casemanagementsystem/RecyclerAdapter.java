package com.inan.cmhs.official.casemanagementsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    ArrayList<RecyclerItems> recyclerItems;

    Context context;
    private ItemClickListener itemClickListener;
    public interface ItemClickListener{
        void onClick(int position);
    }


    public RecyclerAdapter(Context context, ArrayList<RecyclerItems> recyclerItems,ItemClickListener itemClickListener){
        this.context=context;
        this.recyclerItems=recyclerItems;
        this.itemClickListener = itemClickListener;

    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView name,CaseNo,SNo;
        public RecyclerViewHolder(@NonNull View itemView,ItemClickListener listener,ArrayList<RecyclerItems> recyclerItems) throws NullPointerException{
            super(itemView);
            name=itemView.findViewById(R.id.NameDB);
            CaseNo=itemView.findViewById(R.id.CaseNoDB);
            SNo=itemView.findViewById(R.id.SerialNoDB);
            itemView.setOnClickListener(v -> listener.onClick((int) recyclerItems.get(getAdapterPosition()).getSerialNo()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcheckdb,parent,false);
        return new RecyclerViewHolder(view,itemClickListener,recyclerItems);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        try {
            holder.CaseNo.setText("Case No: "+recyclerItems.get(position).getCaseNo());
            holder.name.setText("Name: "+recyclerItems.get(position).getName());
            holder.SNo.setText(String.valueOf(recyclerItems.get(position).getSerialNo()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }


}
