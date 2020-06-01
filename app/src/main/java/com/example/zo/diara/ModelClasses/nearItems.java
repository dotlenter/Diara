package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class nearItems {
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_title")
    @Expose
    private String itemTitle;
    @SerializedName("item_description")
    @Expose
    private String itemDescription;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_image_url")
    @Expose
    private String itemImageUrl;
    @SerializedName("item_dis_in_km")
    @Expose
    private String itemDisInKm;
    @SerializedName("item_dis_in_m")
    @Expose
    private Double itemDisInM;
    @SerializedName("likeCount")
    @Expose
    private String likeCount;


    public nearItems(String itemId, String itemTitle, String itemDescription, String itemPrice, String itemImageUrl, String itemDisInKm, double itemDisInM, String likeCount)
    {
        this.itemId=itemId;
        this.itemTitle=itemTitle;
        this.itemDescription=itemDescription;
        this.itemPrice=itemPrice;
        this.itemImageUrl=itemImageUrl;
        this.itemDisInKm=itemDisInKm;
        this.itemDisInM=itemDisInM;
        this.likeCount=likeCount;
    }
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public String getItemDisInKm() {
        return itemDisInKm;
    }

    public void setItemDisInKm(String itemDisInKm) {
        this.itemDisInKm = itemDisInKm;
    }

    public Double getItemDisInM() {
        return itemDisInM;
    }

    public void setItemDisInM(Double itemDisInM) {
        this.itemDisInM = itemDisInM;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }
}
