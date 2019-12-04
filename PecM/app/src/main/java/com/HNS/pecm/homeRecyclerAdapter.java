package com.HNS.pecm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class homeRecyclerAdapter extends RecyclerView.Adapter<homeRecyclerAdapter.MainViewHolder> {


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itemview_home_closed, parent, false);
            return new myClosedViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itemview_home_open, parent, false);

            return new myOpenViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        shop shop = com.HNS.pecm.shop.shoplist.get(position);

        if (shop.getStatus().equals("Open"))
            return 1;
        else
            return 0;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        shop shop = com.HNS.pecm.shop.shoplist.get(position);
        holder.name.setText(shop.getName());
        holder.img.setImageBitmap(shop.getImage());

    }

    @Override
    public int getItemCount() {
        return shop.shoplist.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView img;

        public MainViewHolder(View v) {
            super(v);
        }
    }

    public class myOpenViewHolder extends MainViewHolder implements View.OnClickListener {

        public myOpenViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_title);
            img = itemView.findViewById(R.id.imageshop);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (StartActivity.getConnection(v.getContext())) {
                Intent intent = new Intent(v.getContext(), ShopActivity.class);
                intent.putExtra("shop", shop.shoplist.get(getAdapterPosition()).getID());
                v.getContext().startActivity(intent);
            } else
                Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public class myClosedViewHolder extends MainViewHolder {

        public myClosedViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_title);
            img = itemView.findViewById(R.id.imageshop);
        }
    }

}


