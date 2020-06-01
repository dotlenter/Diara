package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemCommentsModel {

    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("userImgUrl")
    @Expose
    private String userImgUrl;
    @SerializedName("commentBody")
    @Expose
    private String commentBody;
    @SerializedName("itemRating")
    @Expose
    private Double itemRating;
    @SerializedName("commentUrl")
    @Expose
    private String commentUrl;
    @SerializedName("commentDate")
    @Expose
    private String commentDate;
    @SerializedName("userEmail")
    @Expose
    private String userEmail;

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Double getItemRating() {
        return itemRating;
    }

    public void setItemRating(Double itemRating) {
        this.itemRating = itemRating;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
