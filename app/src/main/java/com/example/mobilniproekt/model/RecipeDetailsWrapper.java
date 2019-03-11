package com.example.mobilniproekt.model;

import com.google.gson.annotations.SerializedName;

public class RecipeDetailsWrapper {
    @SerializedName("recipe")
    private RecipeDetails recipeDetails;

    public RecipeDetails getRecipeDetails() {
        return recipeDetails;
    }

    public void setRecipeDetails(RecipeDetails recipeDetails) {
        this.recipeDetails = recipeDetails;
    }
}
