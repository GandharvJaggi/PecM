package com.HNS.pecm;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.Parse;


public class cart extends Fragment {

    private static RecyclerView recyclerView;
    private static TextView emptyView, amount, amounttext;
    private static Button order;
    private cartRecyclerAdapter mAdapter;

    public static void cartsetup() {
        if (menu.menulist.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            order.setVisibility(View.GONE);
            amount.setVisibility(View.GONE);
            amounttext.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            order.setVisibility(View.VISIBLE);
            amount.setVisibility(View.VISIBLE);
            amounttext.setVisibility(View.VISIBLE);
            amount.setText("₹" + amountcalculation());

        }
    }

    public static int amountcalculation() {

        int sum = 0;

        for (int i = 0; i < menu.menulist.size(); i++) {
            sum += Integer.parseInt(menu.menulist.get(i).getDish().getPrice()) * menu.menulist.get(i).getQuatity();
        }
        return sum;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.dish_recycler);
        emptyView = view.findViewById(R.id.empty);
        order = view.findViewById(R.id.proceed);
        amount = view.findViewById(R.id.amount);
        amounttext = view.findViewById(R.id.amounttext);
        cartsetup();
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MapActivity.class);
                startActivity(i);
            }
        });
        mAdapter = new cartRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new cartRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        cartsetup();
        mAdapter.notifyDataSetChanged();
    }

    public static class app extends Application {

        @Override
        public void onCreate() {
            super.onCreate();

            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId("rkP4DDlbdXqRSp2lvGw7ghNS6kaIwXs4gzFKgap5")
                    .clientKey("W0QzeR2pLG7ohqaBpRqZXA4lJgfKMUxVdHiJ6tMO")
                    .server("https://parseapi.back4app.com")
                    .build()
            );
        }
    }
}
