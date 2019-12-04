package com.HNS.pecmbusiness.ui.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.HNS.pecmbusiness.BuildConfig;
import com.HNS.pecmbusiness.MainActivity;
import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.dishlist;
import com.HNS.pecmbusiness.parse;
import com.HNS.pecmbusiness.registration.StartActivity;
import com.HNS.pecmbusiness.sharedpreferences;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AccountFragment extends Fragment {

    public static Switch status;
    static ImageView image;
    static Bitmap bitmap;
    TextView username, edit, change, menu, version;
    Button logout;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);
        status = root.findViewById(R.id.pro_statusswitch);
        logout = root.findViewById(R.id.logout);
        username = root.findViewById(R.id.shop_name);
        image = root.findViewById(R.id.shop_image);
        edit = root.findViewById(R.id.pro_editinfo);
        change = root.findViewById(R.id.pro_passchange);
        menu = root.findViewById(R.id.pro_menu);
        version = root.findViewById(R.id.version123);
        sharedpreferences.getstatus(getContext());

        username.setText(ParseUser.getCurrentUser().getString("name"));
        setimage();

        version.setText(BuildConfig.VERSION_NAME);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.getConnection(v.getContext())) {
                    if (dishlist.menulist.isEmpty()) {
                        Toast.makeText(getContext(), "Menu is empty, can't open shop", Toast.LENGTH_LONG).show();
                        status.setChecked(false);
                    }
                    sharedpreferences.updatestatus(getContext());
                } else {
                    Toast.makeText(v.getContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    status.toggle();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AccounteditActivity.class);
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AccountmenuActivity.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse.logoutUser();
            }
        });
        return root;
    }

    public void setimage() {
        File f = new File(MainActivity.Shopimage, "profile.jpg");
        if (MainActivity.Shopimage.equals("empty"))
            image.setImageResource(R.drawable.shop_placeholder);
        else {
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}