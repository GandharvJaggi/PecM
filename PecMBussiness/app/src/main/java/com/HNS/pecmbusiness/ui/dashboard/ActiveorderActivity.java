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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.menu;
import com.HNS.pecmbusiness.object.order;
import com.HNS.pecmbusiness.parse;
import com.HNS.pecmbusiness.sharedpreferences;

public class ActiveorderActivity extends AppCompatActivity {

    ImageView map, pinplace;
    TextView bill, total, self, message;
    Button complete;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activeorder);
        map = findViewById(R.id.map);
        self = findViewById(R.id.self);
        pinplace = findViewById(R.id.pinplace);
        complete = findViewById(R.id.ordercomplete);
        bill = findViewById(R.id.orderbill);
        total = findViewById(R.id.ordertotal);
        message = findViewById(R.id.ordermsg);
        pinplace.setVisibility(View.GONE);
        map.setVisibility(View.GONE);
        self.setVisibility(View.GONE);
        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        final int position = bd.getInt("position");
        final order order = DashboardFragment.activelist.get(position);

        bill.setText(getbill(), TextView.BufferType.SPANNABLE);
        total.setText(gettotal(), TextView.BufferType.SPANNABLE);
        message.setText(order.getMessage());

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete.setClickable(false);
                parse.completeOrder(order.getOrderid());
                DashboardFragment.activelist.remove(position);
                DashboardFragment.unpaidlist.add(order);
                sharedpreferences.updateunpaidlist(v.getContext());
                DashboardFragment.activeadapter.notifyItemRangeChanged(0, DashboardFragment.activelist.size());
                DashboardFragment.activeadapter.notifyItemRemoved(position);
                DashboardFragment.unpaidadapter.notifyItemRangeChanged(0, DashboardFragment.activelist.size());
                DashboardFragment.unpaidadapter.notifyDataSetChanged();
                finish();
            }
        });

        if (order.getXcoord() == -1 && order.getYcoord() == -1)
            self.setVisibility(View.VISIBLE);
        else {
            int serviceX = order.getXcoord();
            int serviceY = order.getYcoord();
            map.setVisibility(View.VISIBLE);
            pinplace.setVisibility(View.VISIBLE);
            System.out.println(serviceX + " " + serviceY + " " + map.getHeight() + " " + map.getWidth());
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) pinplace.getLayoutParams();
            marginParams.setMargins(serviceX, serviceY - pinplace.getHeight(), 1080 - serviceX, 1080 - serviceY);
            pinplace.setLayoutParams(marginParams);
        }

    }

    private SpannableStringBuilder getbill() {

        SpannableStringBuilder bill = new SpannableStringBuilder();

        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        int position = bd.getInt("position");
        order order = DashboardFragment.activelist.get(position);
        int start = 0;
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
        order order = DashboardFragment.activelist.get(position);

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
