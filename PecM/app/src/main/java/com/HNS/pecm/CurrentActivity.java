package com.HNS.pecm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CurrentActivity extends AppCompatActivity {

    ImageView map, pinplace;
    TextView bill, total, self, message;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        map = findViewById(R.id.map);
        self = findViewById(R.id.self);
        pinplace = findViewById(R.id.pinplace);
        bill = findViewById(R.id.currentbill);
        total = findViewById(R.id.currentactivitytotal);
        message = findViewById(R.id.currentmsg);
        pinplace.setVisibility(View.GONE);
        map.setVisibility(View.GONE);
        self.setVisibility(View.GONE);
        Intent intent = getIntent();
        final Bundle bd = intent.getExtras();
        final int position = bd.getInt("position");
        final orderlist order = orderlist.currentlist.get(position);
        bill.setText(order.getMenulist());
        total.setText(gettotal(order.getAmount()), TextView.BufferType.SPANNABLE);
        message.setText(order.getMessage());

        if (order.getXcoord() == -1 && order.getYcoord() == -1)
            self.setVisibility(View.VISIBLE);
        else {
            int serviceX = order.getXcoord();
            int serviceY = order.getYcoord();
            map.setVisibility(View.VISIBLE);
            map.setImageResource(R.drawable.map);
            pinplace.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) pinplace.getLayoutParams();
            marginParams.setMargins(serviceX, serviceY - pinplace.getHeight(), 1080 - serviceX, 1080 - serviceY);
            pinplace.setLayoutParams(marginParams);
        }
    }

    private SpannableStringBuilder gettotal(String totalamount) {

        SpannableStringBuilder result = new SpannableStringBuilder();

        String total = "Total: ";
        String amount = "₹" + totalamount;
        result.append(total.concat(amount));
        result.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
                , 0
                , result.length()
                , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        result.setSpan(new StyleSpan(Typeface.BOLD), 0, total.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        return result;
    }
}
