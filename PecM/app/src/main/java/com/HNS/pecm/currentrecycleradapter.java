package com.HNS.pecm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class currentrecycleradapter extends RecyclerView.Adapter<currentrecycleradapter.myViewHolder> {

    @NonNull
    @Override
    public currentrecycleradapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_past, parent, false);
        return new currentrecycleradapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull currentrecycleradapter.myViewHolder holder, final int position) {

        orderlist order = orderlist.currentlist.get(position);
        String time = new SimpleDateFormat("HH:mm", Locale.US).format(order.getTime()) + ", " + order.getDate();
        holder.shop.setText(order.getShopname());
        holder.total.setText("₹" + order.getAmount());
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        if (orderlist.currentlist.isEmpty())
            return 0;
        else
            return orderlist.currentlist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shop, time, total;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            shop = itemView.findViewById(R.id.previousshop);
            time = itemView.findViewById(R.id.previoustime);
            total = itemView.findViewById(R.id.previoustotal);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), CurrentActivity.class);
            i.putExtra("position", getAdapterPosition());
            v.getContext().startActivity(i);
        }
    }
}
