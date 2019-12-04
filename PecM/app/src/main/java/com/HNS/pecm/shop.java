package com.HNS.pecm;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class shop {
    public static ArrayList<shop> shoplist = new ArrayList<>();
    private String name;
    private Bitmap image;
    private String ID;
    private String status;
    private ArrayList<dish> menulist;

    public shop() {
    }

    public ArrayList<dish> getMenulist() {
        return menulist;
    }

    public void setMenulist(ArrayList<dish> menulist) {
        this.menulist = menulist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
