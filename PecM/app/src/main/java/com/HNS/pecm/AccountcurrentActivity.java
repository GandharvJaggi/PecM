package com.HNS.pecm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountcurrentActivity extends AppCompatActivity {

    public static currentrecycleradapter adapter = new currentrecycleradapter();
    public static RecyclerView currentrecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accountcurrent);
        currentrecycler = findViewById(R.id.currentrecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        currentrecycler.setLayoutManager(linearLayoutManager);
        currentrecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        currentrecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
