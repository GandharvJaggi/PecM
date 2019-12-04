package com.HNS.pecmbusiness.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.menu;
import com.HNS.pecmbusiness.object.order;
import com.HNS.pecmbusiness.parse;
import com.HNS.pecmbusiness.sharedpreferences;

public class UnpaidorderActivity extends AppCompatActivity {

    TextView bill, total, name, sid, phone;
    Button paid;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaidorder);
        paid = findViewById(R.id.orderpaid);
        bill = findViewById(R.id.orderbill);
        total = findViewById(R.id.ordertotal);
        name = findViewById(R.id.ordername);
        sid = findViewById(R.id.ordersid);
        phone = findViewById(R.id.orderphone);

        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        final int position = bd.getInt("position");
        final order order = DashboardFragment.unpaidlist.get(position);

        bill.setText(getbill(), TextView.BufferType.SPANNABLE);
        total.setText(gettotal(), TextView.BufferType.SPANNABLE);
        name.setText(order.getCustomer().getName());
        sid.setText(order.getCustomer().getSID());
        phone.setText(order.getCustomer().getPhone());

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paid.setClickable(false);
                parse.paidOrder(order.getOrderid());
                DashboardFragment.unpaidlist.remove(position);
                sharedpreferences.updateunpaidlist(v.getContext());
                DashboardFragment.unpaidadapter.notifyItemRangeChanged(0, DashboardFragment.activelist.size());
                DashboardFragment.unpaidadapter.notifyDataSetChanged();
                finish();
            }
        });

    }

    private SpannableStringBuilder getbill() {

        SpannableStringBuilder bill = new SpannableStringBuilder();

        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        int position = bd.getInt("position");
        order order = DashboardFragment.unpaidlist.get(position);
        int start;
        for (int i = 0; i < order.getMenulist().size(); i++) {
            String dish;
            String amount;
            menu menu = order.getMenulist().get(i);
            dish = menu.getDish().getDishname() + " x " + menu.getQuatity();
            amount = "₹ " + (menu.getQuatity() * Integer.parseInt(menu.getDish().getPrice()));
            bill.append(dish);
            bill.append(System.lineSeparator());
            start = bill.length();
            bill.append(amount);
            bill.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
                    , start
                    , bill.length()
                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            bill.setSpan(new SuperscriptSpan()
                    , start
                    , bill.length()
                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i != order.getMenulist().size() - 1)
                bill.append(System.lineSeparator());
        }

        return bill;
    }

    private SpannableStringBuilder gettotal() {

        SpannableStringBuilder result = new SpannableStringBuilder();
        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        int position = bd.getInt("position");
        order order = DashboardFragment.unpaidlist.get(position);

        String total = "Total: ";
        String amount = "₹ " + order.getAmount();
        result.append(total.concat(amount));
        result.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
                , 0
                , result.length()
                , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        result.setSpan(new StyleSpan(Typeface.BOLD), 0, total.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        return result;
    }
}
