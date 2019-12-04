package com.HNS.pecmbusiness.ui.dashboard;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.order;
import com.HNS.pecmbusiness.parse;

import java.util.concurrent.TimeUnit;

public class dashboardactiveadapter extends RecyclerView.Adapter<dashboardactiveadapter.myViewHolder> {

    @NonNull
    @Override
    public dashboardactiveadapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_active_dashboard, parent, false);

        return new dashboardactiveadapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {

        final order order = DashboardFragment.activelist.get(position);

        holder.name.setText(order.getCustomer().getName());
        holder.desc.setText("₹ " + order.getAmount());
        holder.handler.postDelayed(new timer(holder, order.getDate().getTime()), 0);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.activelist.remove(position);
                parse.deleteOrder(order.getOrderid());
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, DashboardFragment.activelist.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return DashboardFragment.activelist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, desc;
        TextView time;
        ImageButton cancel;
        Handler handler = new Handler();

        public myViewHolder(View v) {
            super(v);

            name = v.findViewById(R.id.activename);
            desc = v.findViewById(R.id.active2);
            time = v.findViewById(R.id.activetime);
            cancel = v.findViewById(R.id.activecancel);
            v.setClickable(true);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), ActiveorderActivity.class);
            i.putExtra("position", getAdapterPosition());
            v.getContext().startActivity(i);
        }
    }

    public class timer implements Runnable {
        myViewHolder holder;
        long time;

        public timer(myViewHolder holder, long time) {
            this.holder = holder;
            this.time = time;
        }

        @Override
        public void run() {
            long difference = (System.currentTimeMillis() - time);
            holder.time.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(difference),
                    TimeUnit.MILLISECONDS.toMinutes(difference) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(difference) % TimeUnit.MINUTES.toSeconds(1)));
            holder.handler.postDelayed(this, 1000);
        }
    }
}
