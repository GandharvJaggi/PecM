package com.HNS.pecmbusiness.ui.account;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.dish;
import com.HNS.pecmbusiness.object.dishlist;
import com.HNS.pecmbusiness.registration.StartActivity;

public class accountmenuadapter extends RecyclerView.Adapter<accountmenuadapter.myViewHolder> {

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_dishlist, parent, false);

        return new accountmenuadapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        final dish dish = dishlist.dishlist.get(position);

        holder.name.setText(dish.getDishname());
        holder.price.setText("₹ " + dish.getPrice());
        holder.desc.setText(dish.getDesc());
        holder.check.setChecked(dishlist.getmenuboolean(dish));

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.check.isChecked()) {
                    dishlist.menulist.add(dish);
                } else
                    dishlist.menulist.remove(dishlist.getmenuindex(dish));
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View view = inflater.inflate(R.layout.dialog_menuedit, null);
                builder.setView(view);
                final EditText name = view.findViewById(R.id.editdishname);
                final EditText desc = view.findViewById(R.id.editdesc);
                final EditText price = view.findViewById(R.id.editprice);
                name.setText(dish.getDishname());
                desc.setText(dish.getDesc());
                price.setText(dish.getPrice());
                builder.setTitle("Edit Dish")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (StartActivity.getConnection(builder.getContext())) {
                                    int i = dishlist.getdishindex(dish);
                                    dishlist.dishlist.remove(i);
                                    dish dish1 = new dish();
                                    dish1.setDishname(name.getText().toString());
                                    dish1.setDesc(desc.getText().toString());
                                    dish1.setPrice(price.getText().toString());
                                    dishlist.dishlist.add(i, dish1);
                                    notifyDataSetChanged();
                                    notifyItemChanged(position);
                                } else {
                                    Toast.makeText(builder.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }
                            }
                        })
                        .create()
                        .show();

                notifyItemChanged(position);
                notifyItemRangeChanged(0, dishlist.dishlist.size());
                notifyDataSetChanged();
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    if (holder.check.isChecked())
                        dishlist.menulist.remove(dishlist.getmenuindex(dish));
                    int i = dishlist.getdishindex(dish);
                    dishlist.dishlist.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(0, dishlist.dishlist.size());
                    notifyDataSetChanged();
                    AccountmenuActivity.menusetup();
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dishlist.dishlist.isEmpty())
            return 0;
        else
            return dishlist.dishlist.size();

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView desc, name, price;
        public CheckBox check;
        public ImageButton edit, remove;

        public myViewHolder(View v) {
            super(v);

            name = v.findViewById(R.id.dish_name);
            price = v.findViewById(R.id.dish_price);
            desc = v.findViewById(R.id.dish_desc);
            check = v.findViewById(R.id.dish_check);
            edit = v.findViewById(R.id.dish_edit);
            remove = v.findViewById(R.id.dish_remove);

        }
    }
}