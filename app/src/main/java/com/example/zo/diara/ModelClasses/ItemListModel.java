package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemListModel {
    @SerializedName("itemID")
    @Expose
    private String itemID;
    @SerializedName("itemTitle")
    @Expose
    private String itemTitle;
    @SerializedName("itemDescription")
    @Expose
    private String itemDescription;
    @SerializedName("itemPrice")
    @Expose
    private Double itemPrice;
    @SerializedName("itemImageURL")
    @Expose
    private String itemImageURL;

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
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

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImageURL() {
        return itemImageURL;
    }

    public void setItemImageURL(String itemImageURL) {
        this.itemImageURL = itemImageURL;
    }
}
