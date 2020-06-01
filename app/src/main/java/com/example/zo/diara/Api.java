package com.example.zo.diara;

import com.example.zo.diara.ModelClasses.Item;
import com.example.zo.diara.ModelClasses.ItemCommentsModel;
import com.example.zo.diara.ModelClasses.ItemListModel;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;
import com.example.zo.diara.ModelClasses.LoginModel;
import com.example.zo.diara.ModelClasses.StatMessageModel;
import com.example.zo.diara.ModelClasses.User;
import com.example.zo.diara.ModelClasses.nearItems;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    /**
     *
     * API VERSION 2
     *
     * */


    @FormUrlEncoded
    @POST("API-v2/getItemComments.php")
    Call<List<ItemCommentsModel>> getItemComments(
            @Field("itemID") long itemID
    );

    @FormUrlEncoded
    @POST("API-v2/createItemComment.php")
    Call<StatMessageModel> createComment(
            @Field("userID") long userID,
            @Field("itemID") long itemID,
            @Field("commentBody") String comment
    );


    @FormUrlEncoded
    @POST("API-v2/getUserLikedItems.php")
    Call<List<ItemsNearbyModel>> getLikedItems(
            @Field("userID") long userID
    );

    @FormUrlEncoded
    @POST("API-v2/getUserArchivedItems.php")
    Call<List<ItemsNearbyModel>> getArchivedItems(
            @Field("userID") long userID
    );

    @FormUrlEncoded
    @POST("API-v2/setItemSwipedLeft.php")
    Call<StatMessageModel> itemSkipped(
            @Field("userID") long userID,
            @Field("itemID") long itemID
    );
    @FormUrlEncoded
    @POST("API-v2/setItemSwipedRight.php")
    Call<StatMessageModel> itemLiked(
            @Field("userID") long userID,
            @Field("itemID") long itemID
    );

    @FormUrlEncoded
    @POST("API-v2/setItemSwipedTop.php")
    Call<StatMessageModel> itemArchived(
            @Field("userID") long userID,
            @Field("itemID") long itemID
    );

    @FormUrlEncoded
    @POST("API-v2/createUserFeedback.php")
    Call<StatMessageModel> createFeedback(
            @Field("userID") long id,
            @Field("feedBody") String body,
            @Field("feedRating") double rating
    );

    @FormUrlEncoded
    @POST("API-v2/createUserWithEmailAndPassword.php")
    Call<StatMessageModel> createUser(
            @Field("userEmail") String userEmail,
            @Field("firebaseID") String firebaseID,
            @Field("birthDate") String birthDate,
            @Field("token") String token,
            @Field("userPassword") String pass,
            @Field("KEY") String key
    );

    @FormUrlEncoded
    @POST("API-v2/getAllItemsNearby.php")
    Call<List<ItemsNearbyModel>> getItemsNearby(
            @Field("auth_user_id") long id,
            @Field("locLongitude") double longitude,
            @Field("locLatitude") double latitude,
            @Field("setMaxDistance") int maxDistance,
            @Field("setDiscoveryFeat") int filter,
            @Field("minPrice") double minPrice,
            @Field("maxPrice") double maxPrice
    );

    @FormUrlEncoded
    @POST("API-v2/setUserProfileImage.php")
    Call<StatMessageModel> updateUserImage(
            @Field("userImage") String image,
            @Field("userID") long userID
    );

    @FormUrlEncoded
    @POST("API-v2/getSingleUserForLogin.php")
    Call<LoginModel> loginUserWithEmailAndPassword(
            @Field("auth_username") String auth_username,
            @Field("auth_password") String auth_password);

    @FormUrlEncoded
    @POST("API-v2/getUserListedItems.php")
    Call<List<ItemsNearbyModel>> getUserListedItems(
            @Field("userID") long id
    );

    /**
     *
     * LEGACY INTERFACE
     *
     * */

    @GET("Users/getSingleUserForLogin.php")
    Call<User> performLogin(
            @Query("auth_username") String auth_username,
            @Query("auth_password") String auth_password);

    @GET("Items/getItems.php")
    Call<List<nearItems>> getItems(
            @Query("auth_user_id") long auth_user_id,
            @Query("loc_lat") double loc_lat,
            @Query("loc_long") double loc_long,
            @Query("userMaxDistance") int dis);

    @FormUrlEncoded
    @POST("Items/addItem.php")
    Call<Item> addItem(
            @Field("auth_user_id") long auth_user_id,
            @Field("item_title") String title,
            @Field("item_description") String itemDesc,
            @Field("item_price") double price,
            @Field("item_image_url") String url,
            @Field("loc_long") String lon,
            @Field("loc_lat") String lat);

    @POST("Items/rewindSwipe.php")
    Call<Item> rewindSwipe(
            @Query("auth_user_id") long auth_user_id
    );

    @POST("Items/swipedItemRight.php")
    Call<Item> swipeItemRight(
            @Query("item_id") long item_id,
            @Query("auth_user_id") long auth_user_id
    );

    @POST("Items/swipedItemLeft.php")
    Call<Item> swipedItemLeft(
            @Query("item_id") long item_id,
            @Query("auth_user_id") long auth_user_id
    );

    @POST("Items/swipedItemTop.php")
    Call<Item> swipedItemTop(
            @Query("item_id") long item_id,
            @Query("auth_user_id") long auth_user_id
    );

    @POST("Items/getAllItemsNearbyWithFilter.php")
    Call<List<Item>> getAllItemsNearbyWithFilter(
            @Query("auth_user_id") long auth_user_id,
            @Query("loc_lat") double loc_lat,
            @Query("loc_long") double loc_long,
            @Query("dis") int dis,
            @Query("order") String order);

    @GET("Users/createUserWithEmailAndPassword.php")
    Call<User> createUserWithEmailAndPassword(
            @Query("auth_email") String auth_email,
            @Query("auth_password") String auth_password,
            @Query("auth_birthDate") String auth_birthDate,
            @Query("auth_token") String auth_token,
            @Query("firebase_id") String firebase_id);

    @POST("Items/updateItemCoord.php")
    Call<Item> updateItemCoord(
            @Query("loc_lat") String loc_lat,
            @Query("loc_long") String loc_long,
            @Query("auth_user_id") long auth_user_id);
}