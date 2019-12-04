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

import java.math.BigInteger;

public class AccounteditActivity extends AppCompatActivity {

    EditText name, sid, phone, email;
    Button update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountedit);

        name = findViewById(R.id.accounteditname);
        sid = findViewById(R.id.accounteditsid);
        phone = findViewById(R.id.accounteditphone);
        email = findViewById(R.id.accounteditemail);
        update = findViewById(R.id.accounteditupdate);

        ParseUser user = ParseUser.getCurrentUser();

        name.setText(user.getString("name"));
        sid.setText(user.getUsername());
        phone.setText(user.getNumber("phone").toString());
        email.setText(user.getEmail());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext()) && StartActivity.checknetworkaccess(v.getContext())) {
                    if (name.getText().length() > 0 && sid.getText().length() == 8 && email.getText().length() > 0 && phone.getText().length() == 10)
                        updateUser(name, sid, phone, email);
                    else {
                        if (name.getText().length() == 0)
                            Toast.makeText(v.getContext(), "The name can't be empty", Toast.LENGTH_LONG).show();
                        else if (sid.getText().length() != 8)
                            Toast.makeText(v.getContext(), "Invalid SID", Toast.LENGTH_LONG).show();
                        else if (name.getText().length() == 0)
                            Toast.makeText(v.getContext(), "Invalid Email ID", Toast.LENGTH_LONG).show();
                        else if (phone.getText().length() != 10)
                            Toast.makeText(v.getContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(v.getContext(), "There is some error!", Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void updateUser(EditText name, EditText sid, EditText phone, EditText email) {

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            user.setUsername(sid.getText().toString());
            user.setEmail(email.getText().toString());
            user.put("name", name.getText().toString());
            user.put("phone", BigInteger.valueOf(Long.valueOf(phone.getText().toString())));

            user.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_LONG).show();
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
