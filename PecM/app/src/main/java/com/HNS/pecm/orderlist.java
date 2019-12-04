package com.HNS.pecm;

import java.util.ArrayList;
import java.util.Date;

public class orderlist {
    public static ArrayList<orderlist> currentlist = new ArrayList<>();
    private String menulist;
    private int xcoord;
    private int ycoord;
    private String amount;
    private String message;
    private String shopid;
    private String shopname;
    private String date;
    private Date time;
    private String orderid;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMenulist() {
        return menulist;
    }

    public void setMenulist(String menulist) {
        this.menulist = menulist;
    }

    public int getXcoord() {
        return xcoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
