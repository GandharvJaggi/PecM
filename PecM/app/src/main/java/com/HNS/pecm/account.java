package com.HNS.pecm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

import static android.content.Context.MODE_PRIVATE;


public class account extends Fragment {

    Button logout;
    SharedPreferences sp;
    TextView name, sid, change, edit, current, favourite, version;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.pro_user);
        sid = view.findViewById(R.id.pro_sid);
        edit = view.findViewById(R.id.pro_editeinfo);
        change = view.findViewById(R.id.pro_passchange);
        current = view.findViewById(R.id.pro_current);
        favourite = view.findViewById(R.id.pro_fav);
        version = view.findViewById(R.id.version123);

        version.setText("Version: " + BuildConfig.VERSION_NAME);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            name.setText(currentUser.getString("name"));
            sid.setText(currentUser.getUsername());
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), AccounteditActivity.class);
                startActivity(i);
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), AccountcurrentActivity.class);
                startActivity(i);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AccountresetActivity.class);
                startActivity(i);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AccountfavouriteActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                    sp.edit().putBoolean("logged", false).apply();
                    ParseUser.logOut();
                    getActivity().finish();
                    Intent i = new Intent(getContext(), StartActivity.class);
                    startActivity(i);
                } else
                    Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            name.setText(currentUser.getString("name"));
            sid.setText(currentUser.getUsername());
        }
    }
}
