package com.eightmin4mile.goandroid.bakingapp;

import com.eightmin4mile.goandroid.bakingapp.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitRequest {
    //final String webUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    String WEB_URL = "https://d17h27t6h515a5.cloudfront.net";

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipeResult();
}
