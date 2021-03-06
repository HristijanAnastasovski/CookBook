package com.finki.mobilniproekt.retrofit;

import com.finki.mobilniproekt.model.RecipeDetailsWrapper;
import com.finki.mobilniproekt.model.Recipes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/api/search?key=a7e55d14ff46b7a9b3f39aa49679bddf")
    Call<Recipes> getRecipes(@Query("q") String q);

    // 1 api: 1df1cc7a6a7945c6b7522d82d148810c
    // 2 api: a7e55d14ff46b7a9b3f39aa49679bddf

    @GET("/api/get?key=a7e55d14ff46b7a9b3f39aa49679bddf")
    Call<RecipeDetailsWrapper> getRecipeDetail(@Query("rId") String rId);



}
