package com.HNS.pecm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountfavouriteActivity extends AppCompatActivity {

    static RecyclerView favouriterecycler;
    static TextView empty;
    favouriteRecyclerAdapter adapter;

    public static void favsetup() {
        if (menu.favouritelist.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            favouriterecycler.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            favouriterecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accountfavourite);

        empty = findViewById(R.id.fav_empty);
        favouriterecycler = findViewById(R.id.favourite_recycler);
        favsetup();
        adapter = new favouriteRecyclerAdapter();
        favouriterecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        favouriterecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        favouriterecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
