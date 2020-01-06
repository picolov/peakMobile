package com.material.components.api;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpektaAPI {

    static final String BASE_URL = "http://spektasolusi.com:1337/";

    public SpektaInterface getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SpektaInterface spektaInterface = retrofit.create(SpektaInterface.class);

        return spektaInterface;
    }
}