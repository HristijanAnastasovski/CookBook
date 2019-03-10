package com.example.mobilniproekt.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipes {

    @SerializedName("count")
    private Integer count;

    @SerializedName("recipes")
    private List<Recipe> recipeList;

    public Recipes(Integer count, List<Recipe> recipeList) {
        this.count = count;
        this.recipeList = recipeList;
    }

    public Integer getCount() {
        return count;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }
}
