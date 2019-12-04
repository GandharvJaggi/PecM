package com.HNS.pecm;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class favouriteRecyclerAdapter extends RecyclerView.Adapter<favouriteRecyclerAdapter.myViewHolder> {

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_favourite, parent, false);

        return new favouriteRecyclerAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        final menu menu = com.HNS.pecm.menu.favouritelist.get(position);

        holder.shop.setText(menu.getShop());
        holder.name.setText(menu.getDish().getDishname());
        holder.price.setText("₹" + menu.getDish().getPrice());
        holder.quantity.setText(setholderquantity(menu.getDish()));
        holder.star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFE7C30C")));

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#858585")));
                com.HNS.pecm.menu.favouritelist.remove(com.HNS.pecm.menu.getintforfav(menu));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, com.HNS.pecm.menu.favouritelist.size());
                AccountfavouriteActivity.favsetup();
                holder.star.setClickable(false);
                com.HNS.pecm.menu.updatefavourite(v.getContext());
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                int a = Integer.parseInt(holder.quantity.getText().toString());
                a++;
                final String quan = String.valueOf(a);
                if (com.HNS.pecm.menu.shopid.equals("empty")) {
                    com.HNS.pecm.menu.shopid = menu.getDish().getShopid();
                    com.HNS.pecm.menu.shopname = menu.getShop();
                    menu.setQuatity(a);
                    holder.quantity.setText(quan);
                    com.HNS.pecm.menu.menulist.add(menu);
                } else if (com.HNS.pecm.menu.shopid.equals(menu.getDish().getShopid())) {

                    menu.setQuatity(a);
                    holder.quantity.setText(quan);
                    if (com.HNS.pecm.menu.checkdish(menu.getDish())) {
                        com.HNS.pecm.menu.menulist.remove(com.HNS.pecm.menu.getint(menu.getDish()));
                        com.HNS.pecm.menu.menulist.add(menu);
                    } else
                        com.HNS.pecm.menu.menulist.add(menu);
                } else {
                    builder.setMessage("Your cart contains dishes from another outlet.Do you wish to discard the selection and add dishes from this outlet?")
                            .setTitle("Replace cart?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    com.HNS.pecm.menu.menulist.clear();
                                    com.HNS.pecm.menu.shopid = menu.getDish().getShopid();
                                    com.HNS.pecm.menu.shopname = menu.getShop();
                                    menu.setQuatity(1);
                                    holder.quantity.setText(quan);
                                    com.HNS.pecm.menu.menulist.add(menu);
                                    com.HNS.pecm.menu.updatemenu(builder.getContext());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                MainActivity.refreshbadge();
                com.HNS.pecm.menu.updatemenu(v.getContext());
            }


        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(holder.quantity.getText().toString());
                a--;
                if (a > 0) {
                    com.HNS.pecm.menu.menulist.remove(com.HNS.pecm.menu.getint(menu.getDish()));
                    menu.setQuatity(a);
                    com.HNS.pecm.menu.menulist.add(menu);
                    holder.quantity.setText(String.valueOf(a));
                } else if (a == 0) {
                    com.HNS.pecm.menu.menulist.remove(com.HNS.pecm.menu.getint(menu.getDish()));
                    holder.quantity.setText(String.valueOf(a));
                }
                MainActivity.refreshbadge();
                com.HNS.pecm.menu.updatemenu(v.getContext());
            }


        });


    }

    @Override
    public int getItemCount() {
        if (null == menu.favouritelist)
            return 0;
        else
            return menu.favouritelist.size();
    }

    private String setholderquantity(dish dish) {

        if (menu.checkdish(dish))
            return menu.menulist.get(com.HNS.pecm.menu.getint(dish)).getQuatity() + "";
        else return "0";
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView shop, name, price, quantity;
        public ImageButton plus, minus, star;

        public myViewHolder(View v) {
            super(v);

            shop = v.findViewById(R.id.accountfavouriteshop);
            name = v.findViewById(R.id.accountfavouritename);
            price = v.findViewById(R.id.accountfavouriteprice);
            quantity = v.findViewById(R.id.accountfavouritequantity);
            plus = v.findViewById(R.id.accountfavouriteplus);
            minus = v.findViewById(R.id.accountfavouriteminus);
            star = v.findViewById(R.id.accountfavouritestar);
        }
    }
}
