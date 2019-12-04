package com.HNS.pecmbusiness;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.HNS.pecmbusiness.object.dish;
import com.HNS.pecmbusiness.object.dishlist;
import com.HNS.pecmbusiness.object.order;
import com.HNS.pecmbusiness.ui.account.AccountFragment;
import com.HNS.pecmbusiness.ui.dashboard.DashboardFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.util.List;

public class sharedpreferences {

    private static SharedPreferences sharedpreferences;

    public static void updatestatus(Context context) {

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String status;
        if (AccountFragment.status.isChecked())
            status = "Open";
        else
            status = "Closed";
        String key = "status";
        editor.putBoolean(key, AccountFragment.status.isChecked()).apply();
        parse.updateShopstatus(status);
        AccountFragment.status.setText(status);
    }

    public static void getstatus(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        AccountFragment.status.setChecked(sharedpreferences.getBoolean("status", false));
        if (sharedpreferences.getBoolean("status", false))
            AccountFragment.status.setText("Open");
        else
            AccountFragment.status.setText("Closed");
    }

    public static void updatedishlist(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spedit = sharedpreferences.edit();
        String key = ParseUser.getCurrentUser().getUsername() + "dishlist";
        Gson gson = new Gson();
        String json = gson.toJson(dishlist.dishlist);
        spedit.remove(key).apply();
        spedit.putString(key, json);
        spedit.apply();
    }

    public static void getdishlist(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = ParseUser.getCurrentUser().getUsername() + "dishlist";
        Gson gson = new Gson();
        String response = sharedpreferences.getString(key, "");
        if (!response.isEmpty()) {
            dishlist.dishlist.clear();
            dishlist.dishlist = gson.fromJson(response,
                    new TypeToken<List<dish>>() {
                    }.getType());
        }
    }

    public static void updatemenulist(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spedit = sharedpreferences.edit();
        String key = ParseUser.getCurrentUser().getUsername() + "menulist";
        Gson gson = new Gson();
        String json = gson.toJson(dishlist.menulist);
        spedit.remove(key).apply();
        spedit.putString(key, json);
        spedit.apply();
    }

    public static void getmenulist(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = ParseUser.getCurrentUser().getUsername() + "menulist";
        Gson gson = new Gson();
        String response = sharedpreferences.getString(key, "");
        if (!response.isEmpty()) {
            dishlist.menulist.clear();
            dishlist.menulist = gson.fromJson(response,
                    new TypeToken<List<dish>>() {
                    }.getType());
        }
    }

    public static void getshopid(Context context) {
        sharedpreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        MainActivity.ShopID = sharedpreferences.getString("shopid", "");
    }

    public static void getshopimage(Context context) {
        sharedpreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        MainActivity.Shopimage = sharedpreferences.getString("shopimage", "");
    }

    public static void setshopimage(Context context, String image) {
        sharedpreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        sharedpreferences.edit().putString("shopimage", image).apply();
    }

    public static void updateunpaidlist(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spedit = sharedpreferences.edit();
        String key = ParseUser.getCurrentUser().getUsername() + "unpaidlist";
        Gson gson = new Gson();
        String json = gson.toJson(DashboardFragment.unpaidlist);
        spedit.remove(key).apply();
        spedit.putString(key, json).apply();
    }

    public static void getunpaidlist(Context context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = ParseUser.getCurrentUser().getUsername() + "unpaidlist";
        Gson gson = new Gson();
        String response = sharedpreferences.getString(key, "");
        if (!response.isEmpty()) {
            DashboardFragment.unpaidlist.clear();
            DashboardFragment.unpaidlist = gson.fromJson(response,
                    new TypeToken<List<order>>() {
                    }.getType());
        }
    }
}
