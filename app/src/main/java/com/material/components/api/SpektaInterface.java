package com.material.components.api;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpektaInterface {
    @GET("distributors/")
    Call<List<JsonObject>> getDistributors();

    @GET("articles/")
    Call<List<JsonObject>> getArticles();

    @GET("products/")
    Call<List<JsonObject>> getProducts();
}