package com.HNS.pecm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView navView;

    public static void refreshbadge() {

        if (menu.menulist.isEmpty()) {
            MainActivity.navView.getBadge(R.id.navigation_cart).setVisible(false);
            menu.shopid = "empty";
            menu.shopname = "empty";
        } else {
            MainActivity.navView.getBadge(R.id.navigation_cart).setVisible(true);
            MainActivity.navView.getOrCreateBadge(R.id.navigation_cart).setNumber(menu.menulist.size());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        createnotificationchannel();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        MainActivity.navView.getOrCreateBadge(R.id.navigation_cart);
        refreshbadge();
    }

    private void createnotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifchannel";
            String description = "channel for notifications";
            int inportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("1", name, inportance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshbadge();
    }
}
