package com.HNS.pecmbusiness.ui.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.parse;
import com.HNS.pecmbusiness.registration.StartActivity;

public class AccountresetActivity extends AppCompatActivity {

    public static EditText password, confirm;
    public static Button update;
    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.activity_accountreset);

        password = findViewById(R.id.accountresetpassword);
        confirm = findViewById(R.id.accountresetconfirm);
        update = findViewById(R.id.accountresetupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StartActivity.getConnection(v.getContext())) {
                    update.setClickable(false);
                    if (password.getText().toString().equals(confirm.getText().toString()) && password.getText().length() >= 8 && password.getText().length() <= 16) {
                        parse.updateUserPassword(password);

                    } else {
                        AccountresetActivity.update.setClickable(true);
                        if (!password.getText().toString().equals(confirm.getText().toString()))
                            Toast.makeText(getApplicationContext(), "The passwords entered don't match", Toast.LENGTH_LONG).show();
                        else if (password.getText().length() < 8 || password.getText().length() > 16)
                            Toast.makeText(getApplicationContext(), "The password should be 8-16 characters long", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "There is some error!", Toast.LENGTH_LONG).show();

                    }
                } else
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }
}
