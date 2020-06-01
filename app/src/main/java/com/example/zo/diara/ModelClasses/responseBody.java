package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class responseBody {
    private String Message;
    private String Status;
    @SerializedName("item_image_url") private MultipartBody.Part item_image_url;
}
