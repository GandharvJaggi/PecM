package com.HNS.pecmbusiness.ui.dashboard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.order;

public class dashboardunpaidadapter extends RecyclerView.Adapter<dashboardunpaidadapter.myViewHolder> {

    @NonNull
    @Override
    public dashboardunpaidadapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_unpaid_dashboard, parent, false);

        return new dashboardunpaidadapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final dashboardunpaidadapter.myViewHolder holder, final int position) {

        order order = DashboardFragment.unpaidlist.get(position);

        holder.name.setText(order.getCustomer().getName());
        holder.sid.setText(order.getCustomer().getSID());
        holder.price.setText("₹ " + order.getAmount());

    }

    @Override
    public int getItemCount() {
        if (DashboardFragment.unpaidlist.isEmpty())
            return 0;
        else
            return DashboardFragment.unpaidlist.size();

    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, sid, price;

        public myViewHolder(View v) {
            super(v);

            name = v.findViewById(R.id.unpaidname);
            sid = v.findViewById(R.id.unpaidsid);
            price = v.findViewById(R.id.unpaidtotal);
            v.setClickable(true);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v.setClickable(false);
            Intent i = new Intent(v.getContext(), UnpaidorderActivity.class);
            i.putExtra("position", getAdapterPosition());
            v.getContext().startActivity(i);
        }
    }
}
