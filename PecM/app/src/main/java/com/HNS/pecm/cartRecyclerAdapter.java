package com.HNS.pecm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cartRecyclerAdapter extends RecyclerView.Adapter<cartRecyclerAdapter.myViewHolder> {

    @NonNull
    @Override
    public cartRecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.itemview_cart, viewGroup, false);

        return new cartRecyclerAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final cartRecyclerAdapter.myViewHolder holder, final int i) {

        final dish dish = menu.menulist.get(i).getDish();
        holder.dish.setText(dish.getDishname());
        holder.quantity.setText(String.valueOf(menu.menulist.get(i).getQuatity()));
        holder.price.setText("₹" + dish.getPrice());

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(holder.quantity.getText().toString());
                a++;
                holder.quantity.setText(String.valueOf(a));
                menu.menulist.get(i).setQuatity(a);
                cart.cartsetup();
                menu.updatemenu(v.getContext());
            }


        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(holder.quantity.getText().toString());
                if (a > 1) {
                    a--;
                    holder.quantity.setText(String.valueOf(a));
                    menu.menulist.get(i).setQuatity(a);

                } else if (a == 1) {
                    menu.menulist.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, menu.menulist.size());
                    holder.minus.setClickable(false);

                }
                cart.cartsetup();
                MainActivity.refreshbadge();
                menu.updatemenu(v.getContext());
            }


        });

    }

    @Override
    public int getItemCount() {
        if (null == menu.menulist)
            return 0;
        else
            return menu.menulist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView dish, quantity, price;
        ImageButton plus;
        ImageButton minus;

        private myViewHolder(View itemView) {
            super(itemView);
            dish = itemView.findViewById(R.id.cart_dish);
            quantity = itemView.findViewById(R.id.cart_quantity);
            price = itemView.findViewById(R.id.cart_price);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }
    }


}
