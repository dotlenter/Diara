package com.example.zo.diara.ModelClasses;

import android.graphics.drawable.Drawable;

public class Item {
    private long item_id;
    private String auth_user_fullName;
    private float auth_user_rating;
    private String auth_verif_stat;
    private String item_title;
    private String item_description;
    private double item_price;
    private String item_image_url;
    private double loc_long;
    private double loc_lat;
    private String item_actual_loc;
    private String item_dis_in_km;
    private String item_dis_in_m;
    private int likeCount;
    private String Status;
    private String Message;

    public Item(){

    }

    public Item(long item_id, String auth_user_fullName, String item_title, String item_description, double item_price, String item_image_url, String item_dis_in_km, String item_dis_in_m, int likeCount) {
        this.item_id=item_id;
        this.item_title = item_title;
        this.item_description = item_description;
        this.item_price = item_price;
        this.item_image_url = item_image_url;
        this.item_dis_in_km = item_dis_in_km;
        this.item_dis_in_m = item_dis_in_m;
        this.likeCount = likeCount;
    }

    public Item(long item_id, String auth_user_fullName, float auth_user_rating, String auth_verif_stat, String item_title, String item_desc, double item_price, String item_image_url, double loc_long, double loc_lat, String item_actual_loc, String item_dis_in_km, String item_dis_in_m) {
        this.item_id = item_id;
        this.auth_user_fullName = auth_user_fullName;
        this.auth_user_rating = auth_user_rating;
        this.auth_verif_stat = auth_verif_stat;
        this.item_title = item_title;
        this.item_description = item_description;
        this.item_price = item_price;
        this.item_image_url = item_image_url;
        this.loc_long = loc_long;
        this.loc_lat = loc_lat;
        this.item_actual_loc = item_actual_loc;
        this.item_dis_in_km = item_dis_in_km;
        this.item_dis_in_m = item_dis_in_m;
    }

    public long getItem_id() {
        return item_id;
    }

    public String getAuth_user_fullName() {
        return auth_user_fullName;
    }

    public float getAuth_user_rating() {
        return auth_user_rating;
    }

    public String getAuth_verif_stat() {
        return auth_verif_stat;
    }

    public String getItem_title() {
        return item_title;
    }

    public String getItem_desc() {
        return item_description;
    }

    public double getItem_price() {
        return item_price;
    }

    public double getLoc_long() {
        return loc_long;
    }

    public double getLoc_lat() {
        return loc_lat;
    }

    public String getItem_actual_loc() {
        return item_actual_loc;
    }

    public String getItem_dis_in_km() {
        return item_dis_in_km;
    }

    public String getItem_dis_in_m() {
        return item_dis_in_m;
    }

    public String getItem_image_url() {
        return item_image_url;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getMessage() {
        return Message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
