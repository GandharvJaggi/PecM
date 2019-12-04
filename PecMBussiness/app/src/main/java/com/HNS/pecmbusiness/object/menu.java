package com.HNS.pecmbusiness.object;

public class menu {

    private dish dish;
    private int quatity;
    private String shop;

    public menu.dish getDish() {
        return dish;
    }

    public int getQuatity() {
        return quatity;
    }

    public String getShop() {
        return shop;
    }

    public class dish {

        private String dishname;
        private String price;
        private String desc;
        private String shopid;

        public String getDishname() {
            return dishname;
        }

        public String getPrice() {
            return price;
        }

        public String getDesc() {
            return desc;
        }

        public String getShopid() {
            return shopid;
        }
    }

}
