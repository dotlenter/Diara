package com.example.zo.diara;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigPref {
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesLocation;

    private Context context;

    public ConfigPref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file),Context.MODE_PRIVATE);
        sharedPreferencesLocation = context.getSharedPreferences("com.example.zo.diara.location_preference",Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status, long id, String token, String username, String url){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_loginStatus), status);
        editor.putLong(context.getString(R.string.pref_user_id), id);
        editor.putString(context.getString(R.string.pref_username), username);
        editor.putString(context.getString(R.string.pref_token), token);
        editor.putString(context.getString(R.string.pref_user_image_url), url);
        editor.putInt(context.getString(R.string.pref_filter), 1111);
        editor.putFloat(context.getString(R.string.pref_price_range_min), 0);
        editor.putFloat(context.getString(R.string.pref_price_range_max), 30000);
        editor.apply();
    }
    public Integer getDiscoveryFilter(){
        return sharedPreferences.getInt(context.getString(R.string.pref_filter), 1111);
    }
    public void setDiscoveryFilter(int filterCode){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.pref_filter), filterCode);
        editor.apply();
    }
    public Float getPriceRangeMin(){
        return sharedPreferences.getFloat(context.getString(R.string.pref_price_range_min), 0);
    }
    public void setPriceRangeMin(Float initial){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(context.getString(R.string.pref_price_range_min), initial);
        editor.apply();
    }
    public Float getPriceRangeMax(){
        return sharedPreferences.getFloat(context.getString(R.string.pref_price_range_max), 30000);
    }
    public void setPriceRangeMax(Float initial){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(context.getString(R.string.pref_price_range_max), initial);
        editor.apply();
    }

    public String getUserImageURL(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_image_url), "");
    }
    public void setUserImageURL(String url){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_image_url), url);
        editor.apply();
    }

    public void setDistance(int d){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.pref_distance), d);
        editor.apply();
    }
    public int getDistance(){
        return sharedPreferences.getInt(context.getString(R.string.pref_distance), 25);
    }
    public void logoutUser(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_loginStatus), false);
        editor.putLong(context.getString(R.string.pref_user_id), 0);
        editor.putString(context.getString(R.string.pref_username), "");
        setDistance(25);
        editor.apply();
    }
    public String getUsername(){
        return sharedPreferences.getString(context.getString(R.string.pref_username), "");
    }
    public long getUserID(){
        return sharedPreferences.getLong(context.getString(R.string.pref_user_id), 0);
    }
    public boolean readLoginStatus(){
        return sharedPreferences.getBoolean(context.getString(R.string.pref_loginStatus), false);
    }

    //LOCATION PREFERENCES
    public void setLoc(float lat, float lon){
        SharedPreferences.Editor editor = sharedPreferencesLocation.edit();
        editor.putFloat("lat", lat);
        editor.putFloat("long", lon);
        editor.apply();
    }
    public double getLat(){
        return sharedPreferencesLocation.getFloat("lat", 0);
    }
    public double getLong(){
        return sharedPreferencesLocation.getFloat("long", 0);
    }
}
