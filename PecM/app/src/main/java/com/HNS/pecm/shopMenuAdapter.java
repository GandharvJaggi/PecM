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

import java.util.ArrayList;


public class shopMenuAdapter extends RecyclerView.Adapter<shopMenuAdapter.myViewHolder> {

    public static String shopid;
    public static String shopname;
    public static ArrayList<dish> dishlist;
    private AlertDialog.Builder builder;

    @NonNull
    @Override
    public shopMenuAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_menu, parent, false);

        return new shopMenuAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final shopMenuAdapter.myViewHolder holder, int position) {
        final dish dish = dishlist.get(position);
        final menu menu = new menu();
        dish.setShopid(shopid);
        menu.setDish(dish);

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu.setShop(shopname);
                if (holder.favourite.getImageTintList().getDefaultColor() == Color.parseColor("#858585")) {
                    holder.favourite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFE7C30C")));
                    com.HNS.pecm.menu.favouritelist.add(menu);
                } else {
                    holder.favourite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#858585")));
                    com.HNS.pecm.menu.favouritelist.remove(com.HNS.pecm.menu.getintforfav(menu));
                }
                com.HNS.pecm.menu.updatefavourite(v.getContext());
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = Integer.parseInt(holder.quantity.getText().toString());
                a++;
                final int b = a;
                final String quan = String.valueOf(a);
                if (com.HNS.pecm.menu.shopid.equals("empty")) {
                    com.HNS.pecm.menu.menulist.clear();
                    com.HNS.pecm.menu.shopid = shopid;
                    com.HNS.pecm.menu.shopname = shopname;
                    menu.setQuatity(a);
                    holder.quantity.setText(quan);
                    com.HNS.pecm.menu.menulist.add(menu);
                } else if (com.HNS.pecm.menu.shopid.equals(shopid)) {
                    menu.setQuatity(a);
                    holder.quantity.setText(quan);
                    if (com.HNS.pecm.menu.checkdish(dish)) {
                        com.HNS.pecm.menu.menulist.remove(com.HNS.pecm.menu.getint(dish));
                        com.HNS.pecm.menu.menulist.add(menu);
                    } else
                        com.HNS.pecm.menu.menulist.add(menu);
                } else {
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Your cart contains dishes from another outlet.Do you wish to discard the selection and add dishes from this outlet?")
                            .setTitle("Replace cart?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    com.HNS.pecm.menu.menulist.clear();
                                    com.HNS.pecm.menu.shopid = shopid;
                                    com.HNS.pecm.menu.shopname = shopname;
                                    menu.setQuatity(b);
                                    holder.quantity.setText(quan);
                                    com.HNS.pecm.menu.menulist.add(menu);
                                    com.HNS.pecm.menu.updatemenu(builder.getContext());
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
                    com.HNS.pecm.menu.menulist.remove(com.HNS.pecm.menu.getint(dish));
                    menu.setQuatity(a);
                    com.HNS.pecm.menu.menulist.add(menu);
                    holder.quantity.setText(String.valueOf(a));
                } else if (a == 0) {
                    com.HNS.pecm.menu.menulist.remove(com.HNS.pecm.menu.getint(dish));
                    holder.quantity.setText(String.valueOf(a));
                }
                MainActivity.refreshbadge();
                com.HNS.pecm.menu.updatemenu(v.getContext());
            }


        });

        holder.name.setText(dish.getDishname());
        holder.desc.setText(dish.getDesc());
        holder.price.setText("₹" + dish.getPrice());
        holder.quantity.setText(setholderquantity(dish));
        holder.favourite.setImageTintList(ColorStateList.valueOf(setholderfavourite(menu)));

    }

    @Override
    public int getItemCount() {
        if (null == dishlist)
            return 0;
        else
            return dishlist.size();
    }

    private String setholderquantity(dish dish) {

        if (menu.checkdish(dish))
            return menu.menulist.get(com.HNS.pecm.menu.getint(dish)).getQuatity() + "";
        else return "0";
    }

    private int setholderfavourite(menu menu) {

        if (com.HNS.pecm.menu.checkfav(menu))
            return Color.parseColor("#FFE7C30C");
        else return Color.parseColor("#858585");
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;
        TextView price;
        ImageButton plus, minus, favourite;
        TextView quantity;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dish_name);
            desc = itemView.findViewById(R.id.dish_desc);
            price = itemView.findViewById(R.id.dish_price);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            quantity = itemView.findViewById(R.id.quantity);
            favourite = itemView.findViewById(R.id.addfavorite);
        }
    }
}


