package com.HNS.pecm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.util.ArrayList;

public class order {
    private ArrayList<menu> menulist = new ArrayList<>();
    private int xcoord;
    private int ycoord;
    private ParseUser customer;
    private int amount;
    private String message;
    private String shopid;

    public order() {
    }

    public static void updatecurrent(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spedit = sp.edit();
        String key = ParseUser.getCurrentUser().getUsername() + "current";
        Gson gson = new Gson();
        String json = gson.toJson(orderlist.currentlist);
        spedit.remove(key).apply();
        spedit.putString(key, json);
        spedit.commit();
    }

    public static void getcurrent(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String key = ParseUser.getCurrentUser().getUsername() + "current";
        Gson gson = new Gson();
        String response = sp.getString(key, "");
        if (!response.isEmpty()) {
            orderlist.currentlist = gson.fromJson(response,
                    new TypeToken<ArrayList<orderlist>>() {
                    }.getType());
        }
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public ArrayList<menu> getMenulist() {
        return menulist;
    }

    public void setMenulist(ArrayList<menu> menulist) {
        this.menulist = menulist;
    }

    public int getXcoord() {
        return xcoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public ParseUser getCustomer() {
        return customer;
    }

    public void setCustomer(ParseUser customer) {
        this.customer = customer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
