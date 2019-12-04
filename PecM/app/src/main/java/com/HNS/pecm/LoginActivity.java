package com.HNS.pecm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button login;
    Button tosignup, forgot;
    View view;
    EditText name, pass;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        login = findViewById(R.id.logIn);
        tosignup = findViewById(R.id.logintosignup);
        name = findViewById(R.id.loginsid);
        pass = findViewById(R.id.loginpass);
        forgot = findViewById(R.id.forgotpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                login.setClickable(false);
                loginUser(name, pass);
            }
        });

        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SignupActivity.class);
                finish();
                startActivity(i);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    Intent i = new Intent(v.getContext(), ForgotpassActivity.class);
                    startActivity(i);
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginUser(final EditText name, final EditText pass) {
        ParseUser.logInInBackground(name.getText().toString(), pass.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent i = new Intent(view.getContext(), MainActivity.class);
                    startActivity(i);
                    sp = getSharedPreferences("login", MODE_PRIVATE);
                    sp.edit().putBoolean("logged", true).apply();
                    sp.edit().putString("session", ParseUser.getCurrentUser().getObjectId()).apply();
                    finish();
                    StartActivity.Start.finish();
                } else {
                    login.setClickable(true);
                    if (e.getCode() == 101)
                        Toast.makeText(getApplicationContext(), "SID or Password incorrect", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 200)
                        Toast.makeText(getApplicationContext(), "SID can't be empty", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 201)
                        Toast.makeText(getApplicationContext(), "Password can't be empty", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "There is some error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
