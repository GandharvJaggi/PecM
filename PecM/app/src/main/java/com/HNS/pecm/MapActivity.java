package com.HNS.pecm;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MapActivity extends AppCompatActivity {

    ImageView map, pinplace;
    RadioGroup selection;
    RadioButton self, serve;
    TextView bill, total;
    EditText message;
    int serviceX = -1;
    int serviceY = -1;
    Button order;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        map = findViewById(R.id.map);
        selection = findViewById(R.id.rgroup);
        self = findViewById(R.id.self);
        serve = findViewById(R.id.serve);
        pinplace = findViewById(R.id.pinplace);
        order = findViewById(R.id.finalorder);
        bill = findViewById(R.id.orderbill);
        total = findViewById(R.id.ordertotal);
        message = findViewById(R.id.ordermsg);
        pinplace.setVisibility(View.GONE);
        map.setVisibility(View.GONE);

        bill.setText(getbill(), TextView.BufferType.SPANNABLE);
        total.setText(gettotal(), TextView.BufferType.SPANNABLE);

        selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.self) {
                    map.setVisibility(View.GONE);
                    pinplace.setVisibility(View.GONE);
                    serviceX = -1;
                    serviceY = -1;
                } else {
                    map.setVisibility(View.VISIBLE);
                }
            }
        });

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                serviceX = (int) event.getX();
                serviceY = (int) event.getY();
                pinplace.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) pinplace.getLayoutParams();
                marginParams.setMargins(serviceX, serviceY - pinplace.getHeight(), v.getWidth() - serviceX, v.getHeight() - serviceY);
                pinplace.setLayoutParams(marginParams);
                System.out.println(map.getWidth() + " " + v.getHeight() + " " + Resources.getSystem().getDisplayMetrics().widthPixels + " " + Resources.getSystem().getDisplayMetrics().heightPixels);
                return false;
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext()) && StartActivity.checknetworkaccess(v.getContext())) {
                    com.HNS.pecm.order order = new order();
                    if (message.getText().length() == 0)
                        order.setMessage("");
                    else
                        order.setMessage(message.getText().toString());
                    order.setAmount(cart.amountcalculation());
                    order.setCustomer(ParseUser.getCurrentUser());
                    order.setMenulist(menu.menulist);
                    order.setXcoord(serviceX);
                    order.setYcoord(serviceY);
                    order.setShopid(menu.shopid);
                    sendorder(order);
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void sendorder(final com.HNS.pecm.order order) {

        final ParseObject entity = new ParseObject(order.getShopid() + "orders");
        customer customer = new customer();
        customer.setName(order.getCustomer().get("name").toString());
        customer.setPhone(order.getCustomer().get("phone").toString());
        customer.setSID(order.getCustomer().getUsername());
        Gson gson = new Gson();
        String customer1 = gson.toJson(customer);
        final String menulist = gson.toJson(order.getMenulist());
        entity.put("customer", customer1);
        entity.put("menulist", menulist);
        entity.put("xcoord", order.getXcoord());
        entity.put("ycoord", order.getYcoord());
        entity.put("message", order.getMessage());
        entity.put("amount", order.getAmount());
        entity.put("complete", "0");

        entity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    orderlist orderlistitem = new orderlist();
                    orderlistitem.setShopname(menu.shopname);
                    orderlistitem.setOrderid(entity.getObjectId());
                    orderlistitem.setMenulist(getbill().toString());
                    orderlistitem.setAmount(String.valueOf(cart.amountcalculation()));
                    orderlistitem.setXcoord(serviceX);
                    orderlistitem.setYcoord(serviceY);
                    orderlistitem.setShopid(menu.shopid);
                    orderlistitem.setMessage(message.getText().toString());
                    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    orderlistitem.setDate(date);
                    orderlistitem.setTime(entity.getCreatedAt());
                    orderlist.currentlist.add(orderlistitem);

                    com.HNS.pecm.order.updatecurrent(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Order successful", Toast.LENGTH_LONG).show();
                    finish();
                    menu.menulist.clear();
                    menu.updatemenu(getApplicationContext());
                } else
                    e.printStackTrace();
            }
        });
    }

    private SpannableStringBuilder getbill() {

        SpannableStringBuilder bill = new SpannableStringBuilder();

        String shopname = menu.shopname;
        bill.append(shopname);
        int start = 0;
        bill.setSpan(new StyleSpan(Typeface.BOLD), start, shopname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        bill.setSpan(new ForegroundColorSpan(0xFFCC5500), start, shopname.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        bill.append(System.lineSeparator());
        bill.append(System.lineSeparator());

        for (int i = 0; i < menu.menulist.size(); i++) {
            String dish;
            String amount;
            menu menu = com.HNS.pecm.menu.menulist.get(i);
            dish = menu.getDish().getDishname() + " x " + menu.getQuatity();
            amount = "₹" + (menu.getQuatity() * Integer.parseInt(menu.getDish().getPrice()));
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
            if (i != com.HNS.pecm.menu.menulist.size() - 1)
                bill.append(System.lineSeparator());
        }

        return bill;
    }

    private SpannableStringBuilder gettotal() {

        SpannableStringBuilder result = new SpannableStringBuilder();

        String total = "Total: ";
        String amount = "₹" + cart.amountcalculation();
        result.append(total.concat(amount));
        result.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
                , 0
                , result.length()
                , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        result.setSpan(new StyleSpan(Typeface.BOLD), 0, total.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        return result;
    }

    public class customer {
        private String name;
        private String phone;
        private String SID;

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }
    }
}
