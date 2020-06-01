package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsNearbyModel {
    @SerializedName("itemID")
    @Expose
    private long itemID;
    @SerializedName("userID")
    @Expose
    private long userID;
    @SerializedName("firebaseID")
    @Expose
    private String firebaseID;
    @SerializedName("itemDistanceInKM")
    @Expose
    private double itemDistanceInKM;
    @SerializedName("itemTitle")
    @Expose
    private String itemTitle;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("itemDescription")
    @Expose
    private String itemDescription;
    @SerializedName("itemPrice")
    @Expose
    private double itemPrice;
    @SerializedName("locLatitude")
    @Expose
    private double locLatitude;
    @SerializedName("locLongitude")
    @Expose
    private double locLongitude;
    @SerializedName("itemLikes")
    @Expose
    private long itemLikes;
    @SerializedName("itemImageURL")
    @Expose
    private String itemImageURL;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("archiveCount")
    @Expose
    private long archiveCount;

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public double getItemDistanceInKM() {
        return itemDistanceInKM;
    }

    public void setItemDistanceInKM(double itemDistanceInKM) {
        this.itemDistanceInKM = itemDistanceInKM;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(double locLatitude) {
        this.locLatitude = locLatitude;
    }

    public double getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(double locLongitude) {
        this.locLongitude = locLongitude;
    }

    public long getItemLikes() {
        return itemLikes;
    }

    public void setItemLikes(long itemLikes) {
        this.itemLikes = itemLikes;
    }

    public String getItemImageURL() {
        return itemImageURL;
    }

    public void setItemImageURL(String itemImageURL) {
        this.itemImageURL = itemImageURL;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public long getArchiveCount() {
        return archiveCount;
    }

    public void setArchiveCount(long archiveCount) {
        this.archiveCount = archiveCount;
    }
}
