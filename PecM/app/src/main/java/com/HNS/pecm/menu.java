package com.HNS.pecm;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.util.ArrayList;

public class menu {
    public static ArrayList<menu> menulist = new ArrayList<>();
    public static ArrayList<menu> favouritelist = new ArrayList<>();
    public static String shopid;
    public static String shopname;
    private dish dish;
    private int quatity;
    private String shop;

    public static boolean checkdish(dish dish) {
        menu menu;
        for (int i = 0; i < menulist.size(); i++) {
            menu = menulist.get(i);
            if (menu.getDish().getDishname().equals(dish.getDishname()) && menu.getDish().getShopid().equals(dish.getShopid()))
                return true;
        }
        return false;
    }

    public static boolean checkfav(menu a) {
        menu b;
        for (int i = 0; i < favouritelist.size(); i++) {
            b = favouritelist.get(i);
            if (b.getDish().getDishname().equals(a.getDish().getDishname()) && b.getDish().getShopid().equals(a.getDish().getShopid()))
                return true;
        }
        return false;
    }

    public static int getint(dish dish) {
        menu menu;
        int i;
        for (i = 0; i < menulist.size(); i++) {
            menu = menulist.get(i);
            if (menu.getDish().getDishname().equals(dish.getDishname()) && menu.getDish().getShopid().equals(dish.getShopid()))
                return i;
        }
        return -1;
    }

    public static int getintforfav(menu a) {
        menu b;
        int i;
        for (i = 0; i < favouritelist.size(); i++) {
            b = favouritelist.get(i);
            if (b.getDish().getDishname().equals(a.getDish().getDishname()) && b.getDish().getShopid().equals(a.getDish().getShopid()))
                return i;
        }
        return -1;
    }

    public static void updatemenu(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spedit = sp.edit();
        String key = ParseUser.getCurrentUser().getUsername() + "menu";
        String shopid = ParseUser.getCurrentUser().getUsername() + "shopid";
        String shopname = ParseUser.getCurrentUser().getUsername() + "shopname";
        Gson gson = new Gson();
        String json = gson.toJson(menu.menulist);
        spedit.remove(key).apply();
        spedit.putString(key, json);
        spedit.putString(shopid, menu.shopid);
        spedit.putString(shopname, menu.shopname);
        spedit.commit();
    }

    public static void getmenu(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String key = ParseUser.getCurrentUser().getUsername() + "menu";
        String shopid = ParseUser.getCurrentUser().getUsername() + "shopid";
        String shopname = ParseUser.getCurrentUser().getUsername() + "shopname";
        Gson gson = new Gson();
        String response = sp.getString(key, "");
        if (!response.isEmpty()) {
            menu.menulist.clear();
            menu.menulist = gson.fromJson(response,
                    new TypeToken<ArrayList<menu>>() {
                    }.getType());
        }
        String responseid = sp.getString(shopid, "");
        if (!responseid.isEmpty())
            menu.shopid = responseid;
        String responsename = sp.getString(shopname, "");
        if (!responsename.isEmpty())
            menu.shopname = responsename;
    }

    public static void updatefavourite(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spedit = sp.edit();
        String key = ParseUser.getCurrentUser().getUsername() + "favourite";
        Gson gson = new Gson();
        String json = gson.toJson(menu.favouritelist);
        spedit.remove(key).apply();
        spedit.putString(key, json);
        spedit.commit();
    }

    public static void getfavourite(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String key = ParseUser.getCurrentUser().getUsername() + "favourite";
        Gson gson = new Gson();
        String response = sp.getString(key, "");
        if (!response.isEmpty()) {
            menu.favouritelist = gson.fromJson(response,
                    new TypeToken<ArrayList<menu>>() {
                    }.getType());
        }
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public com.HNS.pecm.dish getDish() {
        return dish;
    }

    public void setDish(com.HNS.pecm.dish dish) {
        this.dish = dish;
    }

    public int getQuatity() {
        return quatity;
    }

    public void setQuatity(int quatity) {
        this.quatity = quatity;
    }
}
