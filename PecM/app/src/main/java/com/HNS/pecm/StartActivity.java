package com.HNS.pecm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class StartActivity extends AppCompatActivity {

    public static Activity Start;
    Button login;
    Button signup;
    SharedPreferences sp;

    public static boolean getConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return null != activeNetwork;
    }

    public static boolean checknetworkaccess(Context context) {
        final String TAG = "";
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<Boolean> task = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                Looper.prepare();
                InetAddress address;
                try {
                    address = InetAddress.getByName("www.google.com");
                } catch (IOException e) {
                    Log.e(TAG, "call: ioexception", e);
                    return false;
                }
                return !address.getHostAddress().equals("");
            }
        };
        Future<Boolean> future = executorService.submit(task);
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "checknetworkaccess: timeot", e);
            Toast.makeText(context, "Please check you internet connection", Toast.LENGTH_LONG).show();
            return false;
        } catch (InterruptedException e) {
            Log.e(TAG, "checknetworkaccess: interrupt", e);
            Toast.makeText(context, "Please check you internet connection", Toast.LENGTH_LONG).show();
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "checknetworkaccess: execution", e);
            Toast.makeText(context, "Please check you internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Start = this;
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logged", false) && sp.getString("session", "").equals(ParseUser.getCurrentUser().getObjectId())) {
            menu.getmenu(getApplicationContext());
            menu.getfavourite(getApplicationContext());
            order.getcurrent(getApplicationContext());
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_start);

        login = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.SignupBtn);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SignupActivity.class);
                startActivity(i);
            }
        });
    }
}

