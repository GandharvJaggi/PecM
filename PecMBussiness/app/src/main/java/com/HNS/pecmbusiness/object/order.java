package com.HNS.pecmbusiness.object;

import java.util.ArrayList;
import java.util.Date;

public class order {

    private ArrayList<menu> menulist = new ArrayList<>();
    private int xcoord;
    private int ycoord;
    private customer customer;
    private int amount;
    private String message;
    private String orderid;
    private Date date;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public ArrayList<menu> getMenulist() {
        return menulist;
    }

    public void setMenulist(ArrayList<menu> menulist) {
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

    public customer getCustomer() {
        return customer;
    }

    public void setCustomer(com.HNS.pecmbusiness.object.customer customer) {
        this.customer = customer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
