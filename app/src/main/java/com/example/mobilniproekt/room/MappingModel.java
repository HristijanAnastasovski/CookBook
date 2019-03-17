package com.example.mobilniproekt.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MappingModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ID;

    private String recipeID;
    private String ingredient;

    public MappingModel(String recipeID, String ingredient) {
        this.recipeID = recipeID;
        this.ingredient = ingredient;
    }

    public void setID(int ID){
        this.ID=ID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getIngredient() {
        return ingredient;
    }

    public int getID(){
        return ID;
    }
}
