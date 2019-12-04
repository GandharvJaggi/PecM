package com.HNS.pecmbusiness.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.HNS.pecmbusiness.MainActivity;
import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.parse;
import com.HNS.pecmbusiness.registration.StartActivity;
import com.HNS.pecmbusiness.sharedpreferences;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccounteditActivity extends AppCompatActivity {

    public static EditText name, phone;
    public static Button update;
    public static Bitmap bitmap;
    public static Activity activity;
    public ImageView image;
    public ImageButton imgbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        setContentView(R.layout.activity_accountedit);

        name = findViewById(R.id.accounteditname);
        phone = findViewById(R.id.accounteditphone);
        image = findViewById(R.id.imageview);
        update = findViewById(R.id.accounteditupdate);
        imgbtn = findViewById(R.id.accounteditimage);

        ParseUser user = ParseUser.getCurrentUser();

        name.setText(user.getString("name"));
        phone.setText(user.getUsername());
        new imagedownloader().execute();

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getgallery();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StartActivity.getConnection(v.getContext())) {
                    update.setClickable(false);
                    if (name.getText().length() > 0 && phone.getText().length() == 10) {
                        parse.updateUserProfile(name, phone);
                        parse.updateShopprofile(name.getText().toString());
                        parse.updateShopimage(bitmap);
                        MainActivity.Shopimage = saveprofileimage(bitmap);
                    } else {
                        update.setClickable(true);
                        if (name.getText().length() == 0)
                            Toast.makeText(v.getContext(), "The name can't be empty", Toast.LENGTH_LONG).show();
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

    private void getgallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    getContentResolver().openInputStream(selectedImage).close();
                    image.setImageBitmap(bitmap);
                    MainActivity.Shopimage = saveprofileimage(bitmap);
                    sharedpreferences.setshopimage(getApplicationContext(), MainActivity.Shopimage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveprofileimage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private class imagedownloader extends AsyncTask<Void, Void, Void> {
        ProgressBar progressBar = findViewById(R.id.editprogressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.editprogress).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            File f = new File(MainActivity.Shopimage, "profile.jpg");
            if (MainActivity.Shopimage.equals("empty"))
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shop_placeholder);
            else if (f.exists()) {
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");
                query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
                    public void done(ParseObject result, ParseException e) {
                        if (e == null) {
                            ParseFile file = result.getParseFile("image");
                            try {
                                bitmap = BitmapFactory.decodeByteArray(file.getData(), 0, file.getData().length);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.editprogress).setVisibility(View.GONE);
            image.setImageBitmap(bitmap);
        }
    }
}
