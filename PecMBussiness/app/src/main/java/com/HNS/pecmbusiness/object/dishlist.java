package com.HNS.pecmbusiness.object;

import java.util.ArrayList;

public class dishlist {

    public static ArrayList<dish> dishlist = new ArrayList<>();
    public static ArrayList<dish> menulist = new ArrayList<>();

    public static int getmenuindex(dish dish) {
        dish dish1;
        int i;
        for (i = 0; i < menulist.size(); i++) {
            dish1 = menulist.get(i);
            if (dish1.getDishname().equals(dish.getDishname()))
                return i;
        }
        return i;
    }

    public static int getdishindex(dish dish) {
        dish dish1;
        int i;
        for (i = 0; i < dishlist.size(); i++) {
            dish1 = dishlist.get(i);
            if (dish1.getDishname().equals(dish.getDishname()))
                return i;
        }
        return i;
    }

    public static boolean getmenuboolean(dish dish) {
        dish dish1;
        for (int i = 0; i < menulist.size(); i++) {
            dish1 = menulist.get(i);
            if (dish1.getDishname().equals(dish.getDishname()))
                return true;
        }
        return false;
    }
}
