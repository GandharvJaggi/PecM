package com.HNS.pecmbusiness.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.HNS.pecmbusiness.MainActivity;
import com.HNS.pecmbusiness.R;
import com.parse.ParseUser;

public class StartActivity extends AppCompatActivity {

    public static Activity activity;
    Button login;
    Button signup;
    SharedPreferences sp;

    public static boolean getConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return null != activeNetwork;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logged", false) && !sp.getString("ID", "").isEmpty() && ParseUser.getCurrentUser() != null) {
            if (sp.getString("ID", "").equals(ParseUser.getCurrentUser().getObjectId())) {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }

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

