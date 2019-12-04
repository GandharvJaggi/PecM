package com.HNS.pecmbusiness.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.parse;

public class SignupActivity extends AppCompatActivity {

    public static Button signup, tologin;
    public static EditText name, phone, password, confirm;
    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        activity = this;
        signup = findViewById(R.id.signUp);
        tologin = findViewById(R.id.signuptologin);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.pass);
        confirm = findViewById(R.id.passconfirm);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    if (name.getText().length() > 0 && phone.getText().length() == 10 && password.getText().toString().equals(confirm.getText().toString()) && password.getText().length() >= 8 && password.getText().length() <= 16) {
                        parse.createUser(name, phone, password);
                        parse.createShop(name.getText().toString().trim());
                        signup.setClickable(false);
                    } else {
                        if (name.getText().length() == 0)
                            Toast.makeText(v.getContext(), "The name can't be empty", Toast.LENGTH_LONG).show();
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
}
