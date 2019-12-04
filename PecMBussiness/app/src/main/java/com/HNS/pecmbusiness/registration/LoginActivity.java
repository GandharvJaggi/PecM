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

public class LoginActivity extends AppCompatActivity {

    public static Button login, tosignup, forgot;
    public static EditText name, pass;
    public static Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
        login = findViewById(R.id.logIn);
        tosignup = findViewById(R.id.logintosignup);
        name = findViewById(R.id.loginPhone);
        pass = findViewById(R.id.loginpass);
        forgot = findViewById(R.id.forgotpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    login.setClickable(false);
                    parse.loginUser(name, pass);
                } else
                    Toast.makeText(v.getContext(), "Please check your internet conenction", Toast.LENGTH_LONG).show();
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
                if (StartActivity.getConnection(v.getContext()))
                    parse.passwordReset();
                else
                    Toast.makeText(v.getContext(), "Please check your internet conenction", Toast.LENGTH_LONG).show();
            }
        });
    }
}
