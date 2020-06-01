package com.example.zo.diara.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("auth_user_id") private long auth_user_id;
    @SerializedName("firebase_id") private String firebase_id;
    @SerializedName("auth_firstName") private String auth_firstName;
    @SerializedName("auth_middleName") private String auth_middleName;
    @SerializedName("auth_lastName") private String auth_lastName;
    @SerializedName("auth_gender") private String auth_gender;
    @SerializedName("auth_email") private String auth_email;
    @SerializedName("auth_userKey") private String auth_userKey;
    @SerializedName("auth_birthDate") private  String auth_birthDate;
    @SerializedName("auth_username") private String auth_username;
    @SerializedName("auth_password") private String auth_password;
    @SerializedName("auth_token") private String auth_token;
    @SerializedName("Status") private String Status;
    @SerializedName("Message") private String Message;

    public String getMessage() {
        return Message;
    }

    public String getAuth_firstName() { return auth_firstName; }

    public String getAuth_middleName() { return auth_middleName; }

    public String getAuth_lastName() { return auth_lastName; }

    public String getAuth_gender() { return auth_gender; }

    public String getAuth_email() { return auth_email; }

    public String getAuth_userKey() { return auth_userKey; }

    public String getAuth_birthDate() { return auth_birthDate; }

    public String getAuth_password() { return auth_password; }

    public String getStatus() {
        return Status;
    }

    public String getAuth_username() {
        return auth_username;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public long getAuth_user_id() {
        return auth_user_id;
    }
}
