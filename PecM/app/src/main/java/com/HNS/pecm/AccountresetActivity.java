package com.HNS.pecm;

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
import com.parse.SaveCallback;

public class AccountresetActivity extends AppCompatActivity {

    EditText password, confirm;
    Button update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accountreset);

        password = findViewById(R.id.accountresetpassword);
        confirm = findViewById(R.id.accountresetconfirm);
        update = findViewById(R.id.accountresetupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext()) && StartActivity.checknetworkaccess(v.getContext())) {
                    if (password.getText().toString().equals(confirm.getText().toString()) && password.getText().length() >= 8 && password.getText().length() <= 16)
                        updateUser(password);
                    else {

                        if (!password.getText().toString().equals(confirm.getText().toString()))
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

    }

    public void updateUser(EditText password) {

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {

            user.setPassword(password.getText().toString());

            user.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Password updated", Toast.LENGTH_LONG).show();
                        finish();
                        update.setClickable(false);

                    } else {
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
}
