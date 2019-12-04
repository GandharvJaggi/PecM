package com.HNS.pecmbusiness;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.HNS.pecmbusiness.ui.dashboard.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    public static Activity activity;
    public static String ShopID;
    public static String Shopimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        createnotificationchannel();
        sharedpreferences.getshopid(getApplicationContext());
        sharedpreferences.getshopimage(getApplicationContext());
        sharedpreferences.getdishlist(getApplicationContext());
        sharedpreferences.getmenulist(getApplicationContext());
        DashboardFragment.activelivequery(getApplicationContext());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.mobile_navigation);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void createnotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifchannel";
            String description = "channel for notifications";
            int inportance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1", name, inportance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static class app extends Application {

        @Override
        public void onCreate() {
            super.onCreate();

            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId("rkP4DDlbdXqRSp2lvGw7ghNS6kaIwXs4gzFKgap5")
                    .clientKey("W0QzeR2pLG7ohqaBpRqZXA4lJgfKMUxVdHiJ6tMO")
                    .server("https://pecm.back4app.io")
                    .build()
            );
        }
    }
}
