package com.HNS.pecm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopActivity extends AppCompatActivity {

    public TextView name;
    public RecyclerView menu;
    public ImageView image;
    shopMenuAdapter adapter = new shopMenuAdapter();

    private static int getposition(String shopid) {
        int i;
        shop shop;
        for (i = 0; i < com.HNS.pecm.shop.shoplist.size(); i++) {
            shop = com.HNS.pecm.shop.shoplist.get(i);
            if (shop.getID().equals(shopid))
                break;
        }
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        findViewById(R.id.shop_loading).setVisibility(View.GONE);

        name = findViewById(R.id.shop_name);
        menu = findViewById(R.id.menu);
        image = findViewById(R.id.shop_image);
        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        String id = bd.getString("shop");
        name.setText(shop.shoplist.get(getposition(id)).getName());
        image.setImageBitmap(shop.shoplist.get(getposition(id)).getImage());
        menu.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        menu.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        shopMenuAdapter.dishlist = shop.shoplist.get(getposition(id)).getMenulist();
        shopMenuAdapter.shopid = shop.shoplist.get(getposition(id)).getID();
        shopMenuAdapter.shopname = shop.shoplist.get(getposition(id)).getName();
        menu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}