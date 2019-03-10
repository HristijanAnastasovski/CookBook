package com.example.mobilniproekt.retrofit;

import com.example.mobilniproekt.model.Recipes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("/api/search?key=a7e55d14ff46b7a9b3f39aa49679bddf&q=milk,eggs")
    Call<Recipes> getRecipes();

}
