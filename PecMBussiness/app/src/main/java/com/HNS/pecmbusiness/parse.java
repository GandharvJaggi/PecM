package com.HNS.pecmbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

import com.HNS.pecmbusiness.object.dish;
import com.HNS.pecmbusiness.object.dishlist;
import com.HNS.pecmbusiness.registration.LoginActivity;
import com.HNS.pecmbusiness.registration.SignupActivity;
import com.HNS.pecmbusiness.registration.StartActivity;
import com.HNS.pecmbusiness.ui.account.AccounteditActivity;
import com.HNS.pecmbusiness.ui.account.AccountresetActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.parse.Parse.getApplicationContext;

public class parse {

    private static SharedPreferences sp;

    public static void createUser(EditText name, EditText phone, EditText password) {
        final ParseUser user = new ParseUser();
        user.setUsername(phone.getText().toString());
        user.setPassword(password.getText().toString());
        user.put("name", name.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    sp = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                    sp.edit().putBoolean("logged", true).apply();
                    sp.edit().putString("ID", ParseUser.getCurrentUser().getObjectId()).apply();
                    SignupActivity.activity.finish();
                    StartActivity.activity.finish();

                } else {
                    SignupActivity.signup.setClickable(true);
                    if (e.getCode() == 202)
                        Toast.makeText(getApplicationContext(), "The Phone number is already registered", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 206 || e.getCode() == 209)
                        Toast.makeText(getApplicationContext(), "The User is already logged in", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "There is some error! lol", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void createShop(String name) {
        final ParseObject entity = new ParseObject("Shop");

        entity.put("shop_name", name);
        entity.put("status", "Hide");
        entity.put("image", new ParseFile("resume.txt", "My string content".getBytes()));

        entity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                createOrderclass(entity.getObjectId());
                sp = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                sp.edit().putString("shopid", entity.getObjectId()).apply();
                MainActivity.ShopID = entity.getObjectId();
                updateUserShopid(entity.getObjectId());
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.shop_placeholder);
                updateShopimage(bitmap);
                sharedpreferences.setshopimage(getApplicationContext(), "empty");
                sharedpreferences.updatedishlist(getApplicationContext());
                sharedpreferences.updatemenulist(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Shop created", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void createOrderclass(final String shopid) {

        final ParseObject orderclass = new ParseObject(shopid + "orders");
        orderclass.put("message", "");
        orderclass.put("shop", "");
        orderclass.put("complete", "");
        orderclass.put("ycoord", 0);
        orderclass.put("xcoord", 0);
        orderclass.put("menulist", "");
        orderclass.put("customer", "");
        orderclass.put("amount", 0);
        orderclass.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getApplicationContext(), "Shop order class created", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void loginUser(final EditText name, final EditText pass) {
        ParseUser.logInInBackground(name.getText().toString(), pass.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    sp = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                    sp.edit().putBoolean("logged", true).apply();
                    sp.edit().putString("ID", ParseUser.getCurrentUser().getObjectId()).apply();
                    sp.edit().putString("shopid", user.getString("shopid")).apply();
                    MainActivity.ShopID = user.getString("shopid");
                    getShopdishlist();
                    getShopmenu();
                    LoginActivity.activity.finish();
                    StartActivity.activity.finish();
                } else {
                    LoginActivity.login.setClickable(true);
                    if (e.getCode() == 101)
                        Toast.makeText(getApplicationContext(), "Phone number or Password incorrect", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 200)
                        Toast.makeText(getApplicationContext(), "Phone number can't be empty", Toast.LENGTH_LONG).show();
                    else if (e.getCode() == 201)
                        Toast.makeText(getApplicationContext(), "Password can't be empty", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "There is some error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void logoutUser() {
        ParseUser.logOut();
        sp = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putBoolean("logged", false).apply();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp.edit().putBoolean("status", false).apply();
        updateShopstatus("Closed");
        MainActivity.activity.finish();
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public static void passwordReset() {
        ParseUser.requestPasswordResetInBackground("gandharv.jaggi@gmail.com", new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Contact the app developer.", Toast.LENGTH_LONG).show();
                    LoginActivity.activity.finish();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void updateUserPassword(EditText password) {

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            user.setPassword(password.getText().toString());
            user.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Password updated", Toast.LENGTH_LONG).show();
                        AccountresetActivity.activity.finish();

                    } else {
                        AccountresetActivity.update.setClickable(true);
                        if (e.getCode() == 202)
                            Toast.makeText(getApplicationContext(), "Phone number is already registered", Toast.LENGTH_LONG).show();
                        else if (e.getCode() == 206 || e.getCode() == 209)
                            Toast.makeText(getApplicationContext(), "The User is already logged in", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "There is some error! lol", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public static void updateUserProfile(EditText name, EditText phone) {

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            user.setUsername(phone.getText().toString());
            user.put("name", name.getText().toString());

            user.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_LONG).show();
                        AccounteditActivity.activity.finish();

                    } else {
                        AccounteditActivity.update.setClickable(true);
                        if (e.getCode() == 202)
                            Toast.makeText(getApplicationContext(), "The Phone number is already registered", Toast.LENGTH_LONG).show();
                        else if (e.getCode() == 206 || e.getCode() == 209)
                            Toast.makeText(getApplicationContext(), "The User is already logged in", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "There is some error! lol", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public static void updateUserShopid(String shopid) {

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            user.put("shopid", shopid);
            user.saveInBackground();
        }
    }

    public static void updateShopimage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        final ParseFile file = new ParseFile(ParseUser.getCurrentUser().getObjectId() + ".png", image);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");
        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("image", file);
                    entity.saveInBackground();
                }
            }
        });
    }

    public static void updateShopmenu() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");

        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    Gson gson = new Gson();
                    String menulist = gson.toJson(dishlist.menulist, new TypeToken<ArrayList<dish>>() {
                    }.getType());
                    JSONArray menuJSON = null;
                    try {
                        menuJSON = new JSONArray(menulist);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    entity.put("menu", menuJSON);

                    entity.saveInBackground();
                }

            }
        });
    }

    public static void getShopmenu() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");
        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<dish>>() {
                    }.getType();
                    ArrayList<dish> list = gson.fromJson(result.getJSONArray("menu").toString(), listType);
                    dishlist.menulist.clear();
                    dishlist.menulist.addAll(list);
                    sharedpreferences.updatemenulist(getApplicationContext());
                }
            }
        });
    }

    public static void updateShopdishlist() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");

        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    Gson gson = new Gson();
                    String dishlist1 = gson.toJson(dishlist.dishlist, new TypeToken<ArrayList<dish>>() {
                    }.getType());
                    JSONArray dishJSON = null;
                    try {
                        dishJSON = new JSONArray(dishlist1);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    entity.put("dishlist", dishJSON);

                    entity.saveInBackground();
                }
            }
        });
    }

    public static void getShopdishlist() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");
        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<dish>>() {
                    }.getType();
                    ArrayList<dish> list = gson.fromJson(result.getJSONArray("menu").toString(), listType);
                    dishlist.dishlist.clear();
                    dishlist.dishlist.addAll(list);
                    sharedpreferences.updatedishlist(getApplicationContext());
                }
            }
        });
    }

    public static void updateShopprofile(final String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");

        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {

                    entity.put("shop_name", name);

                    entity.saveInBackground();
                }
            }
        });
    }

    public static void updateShopstatus(final String status) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");

        query.getInBackground(MainActivity.ShopID, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {

                    entity.put("status", status);

                    entity.saveInBackground();
                }
            }
        });
    }

    public static void deleteOrder(String orderid) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(MainActivity.ShopID + "orders");
        query.getInBackground(orderid, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.deleteInBackground();
                }
            }
        });
    }

    public static void completeOrder(String orderid) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(MainActivity.ShopID + "orders");
        query.getInBackground(orderid, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("complete", "1");
                    entity.saveInBackground();
                }
            }
        });
    }

    public static void paidOrder(String orderid) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(MainActivity.ShopID + "orders");
        query.getInBackground(orderid, new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    entity.put("complete", "2");
                    entity.saveInBackground();
                }
            }
        });
    }
}
