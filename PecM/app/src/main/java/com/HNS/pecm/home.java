package com.HNS.pecm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class home extends Fragment {

    private static View view;
    private RecyclerView recyclerView;
    private homeRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        mAdapter = new homeRecyclerAdapter();
        view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        new loader().execute(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.refreshbadge();
        new loader().execute(getContext());
    }

    private class loader extends AsyncTask<Context, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Context... contexts) {
            if (StartActivity.getConnection(contexts[0]) && StartActivity.checknetworkaccess(contexts[0])) {
                shop.shoplist.clear();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");
                try {
                    List<ParseObject> results = query.find();
                    for (ParseObject result : results) {

                        shop shop = new shop();
                        ParseFile file = result.getParseFile("image");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(file.getData(), 0, file.getData().length);
                        if (result.get("status").toString().equals("Open")) {
                            shop.setName(result.get("shop_name").toString());
                            shop.setImage(bitmap);
                            shop.setStatus(result.get("status").toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<dish>>() {
                            }.getType();
                            ArrayList<dish> list = gson.fromJson(result.getJSONArray("menu").toString(), listType);
                            shop.setMenulist(list);
                            shop.setID(result.getObjectId());
                            com.HNS.pecm.shop.shoplist.add(shop);
                        } else if (result.get("status").toString().equals("Closed")) {
                            shop.setID("");
                            shop.setName(result.get("shop_name").toString());
                            shop.setImage(bitmap);
                            shop.setStatus(result.get("status").toString());
                            com.HNS.pecm.shop.shoplist.add(shop);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemRangeChanged(0, shop.shoplist.size());
            view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(), "Please check you internet connection", Toast.LENGTH_LONG).show();
        }
    }
}