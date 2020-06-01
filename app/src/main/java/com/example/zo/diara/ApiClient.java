package com.example.zo.diara;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //public static final String BASE_URL = "https://diara.000webhostapp.com/Diara/api/";
    //public static final String BASE_URL = "http://10.0.2.2:8080/Diara/api/";
    public static final String BASE_URL = "http://capstonehcdc.com/Diara/api/";
    private static ApiClient apiClientInstance;
    private static Retrofit retrofit = null;

    Gson gson = new GsonBuilder().setLenient().create();

    private ApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    public static  synchronized ApiClient getInstance(){
        if(apiClientInstance == null){
            apiClientInstance = new ApiClient();
        }
        return apiClientInstance;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
