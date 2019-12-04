package com.HNS.pecm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.math.BigInteger;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    Button tologin;
    EditText name, sid, phone, email, password, confirm;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.signUp);
        tologin = findViewById(R.id.signuptologin);
        name = findViewById(R.id.name);
        sid = findViewById(R.id.sid);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirm = findViewById(R.id.passconfirm);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    if (name.getText().length() > 0 && sid.getText().length() == 8 && email.getText().length() > 0 && phone.getText().length() == 10 && password.getText().toString().equals(confirm.getText().toString()) && password.getText().length() >= 8 && password.getText().length() <= 16) {
                        createUser(name, sid, phone, email, password);
                        signup.setClickable(false);
                    } else {
                        if (name.getText().length() == 0)
                            Toast.makeText(v.getContext(), "The name can't be empty", Toast.LENGTH_LONG).show();
                        else if (sid.getText().length() != 8)
                            Toast.makeText(v.getContext(), "The SID is invalid", Toast.LENGTH_LONG).show();
                        else if (email.getText().length() == 0)
                            Toast.makeText(v.getContext(), "The Email ID can't be empty", Toast.LENGTH_LONG).show();
                        else if (phone.getText().length() != 10)
                            Toast.makeText(v.getContext(), "The phone number is invalid", Toast.LENGTH_LONG).show();
                        else if (!password.getText().toString().equals(confirm.getText().toString()))
                            Toast.makeText(v.getContext(), "The passwords entered don't match", Toast.LENGTH_LONG).show();
                        else if (password.getText().length() < 8 || password.getText().length() > 16)
                            Toast.makeText(v.getContext(), "The password should be 8-16 characters long", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(v.getContext(), "There is some error!", Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LoginActivity.class);
                finish();
                startActivity(i);
            }
        });
    }

    public void createUser(EditText name, EditText sid, EditText phone, EditText email, EditText password) {
        final ParseUser user = new ParseUser();
        user.setUsername(sid.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());
        user.put("name", name.getText().toString());
        user.put("phone", BigInteger.valueOf(Long.valueOf(phone.getText().toString())));

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    sp = getSharedPreferences("login", MODE_PRIVATE);
                    sp.edit().putBoolean("logged", true).apply();
                    sp.edit().putString("session", ParseUser.getCurrentUser().getObjectId()).apply();
                    menu.updatemenu(getApplicationContext());
                    menu.updatefavourite(getApplicationContext());
                    order.updatecurrent(getApplicationContext());
                    finish();
                    StartActivity.Start.finish();

                } else {
                    signup.setClickable(true);
                    Log.e("", "done: " + e.getCode(), e);
                    if (e.getCode() == 202)
                        Toast.makeText(getApplicationContext(), "The SID is already registered", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 203)
                        Toast.makeText(getApplicationContext(), "The Email ID is already registered", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 206 || e.getCode() == 209)
                        Toast.makeText(getApplicationContext(), "The User is already logged in", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 125)
                        Toast.makeText(getApplicationContext(), "Invalid Email ID", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "There is some error! lol", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
