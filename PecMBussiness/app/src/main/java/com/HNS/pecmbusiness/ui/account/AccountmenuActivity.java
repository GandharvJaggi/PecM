package com.HNS.pecmbusiness.ui.account;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.dish;
import com.HNS.pecmbusiness.object.dishlist;
import com.HNS.pecmbusiness.parse;
import com.HNS.pecmbusiness.registration.StartActivity;
import com.HNS.pecmbusiness.sharedpreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountmenuActivity extends AppCompatActivity {

    static final accountmenuadapter adapter = new accountmenuadapter();
    public static RecyclerView accountmenurecycler;
    public static TextView empty;
    public static FloatingActionButton add, update;

    public static void menusetup() {
        empty.setVisibility(View.GONE);
        if (dishlist.dishlist.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            accountmenurecycler.setVisibility(View.INVISIBLE);
        }
        adapter.notifyItemRangeChanged(0, dishlist.dishlist.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_acountmenu);
        empty = findViewById(R.id.menulistempty);
        accountmenurecycler = findViewById(R.id.accountmenurecycler);
        add = findViewById(R.id.menulistadd);
        update = findViewById(R.id.menulistupdate);

        menusetup();
        sharedpreferences.getdishlist(getApplicationContext());

        accountmenurecycler.setLayoutManager(new LinearLayoutManager(accountmenurecycler.getContext()));
        accountmenurecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        accountmenurecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                final View view = inflater.inflate(R.layout.dialog_menuedit, null);
                builder.setView(view);
                final EditText name = view.findViewById(R.id.editdishname);
                final EditText desc = view.findViewById(R.id.editdesc);
                final EditText price = view.findViewById(R.id.editprice);

                builder.setTitle("Add Dish")
                        .setCancelable(true)
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (StartActivity.getConnection(builder.getContext())) {
                                    dish dish1 = new dish();
                                    dish1.setDishname(name.getText().toString());
                                    dish1.setDesc(desc.getText().toString());
                                    dish1.setPrice(price.getText().toString());
                                    if (dishlist.dishlist.isEmpty())
                                        dishlist.dishlist = new ArrayList<>();
                                    dishlist.dishlist.add(dish1);
                                    sharedpreferences.updatedishlist(builder.getContext());
                                    menusetup();
                                    Toast.makeText(builder.getContext(), "Dish added", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(builder.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                                    dialog.cancel();

                                }
                                adapter.notifyItemRangeChanged(0, dishlist.dishlist.size());
                                adapter.notifyItemInserted(dishlist.dishlist.size());
                                adapter.notifyDataSetChanged();
                            }

                        })
                        .create()
                        .show();
                adapter.notifyItemRangeChanged(0, dishlist.dishlist.size());
                adapter.notifyItemInserted(dishlist.dishlist.size());
                adapter.notifyDataSetChanged();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    sharedpreferences.updatemenulist(v.getContext());
                    sharedpreferences.updatedishlist(v.getContext());
                    parse.updateShopmenu();
                    parse.updateShopdishlist();
                    Toast.makeText(v.getContext(), "Menu updated", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedpreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (dishlist.menulist.isEmpty() && sharedpreferences1.getBoolean("status", false))
            Toast.makeText(getApplicationContext(), "menu can't be empty", Toast.LENGTH_LONG).show();
        else {
            super.onBackPressed();
            sharedpreferences.updatemenulist(getApplicationContext());
            sharedpreferences.updatedishlist(getApplicationContext());
            parse.updateShopmenu();
            parse.updateShopdishlist();
        }
    }
}
