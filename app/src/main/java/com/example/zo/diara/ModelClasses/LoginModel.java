package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("auth_user_id")
    @Expose
    private long authUserId;
    @SerializedName("auth_username")
    @Expose
    private String authUsername;
    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("auth_user_img_url")
    @Expose
    private String authUserImgUrl;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;

    public long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(long authUserId) {
        this.authUserId = authUserId;
    }

    public String getAuthUsername() {
        return authUsername;
    }

    public void setAuthUsername(String authUsername) {
        this.authUsername = authUsername;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthUserImgUrl() {
        return authUserImgUrl;
    }

    public void setAuthUserImgUrl(String authUserImgUrl) {
        this.authUserImgUrl = authUserImgUrl;
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
}
