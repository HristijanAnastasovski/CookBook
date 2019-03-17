package com.example.mobilniproekt.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mobilniproekt.model.Recipe;
import com.example.mobilniproekt.model.RecipeDetails;
import com.example.mobilniproekt.room.RecipeModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleRecipe (RecipeModel recipe);
    @Insert(onConflict = REPLACE)
    void insertMultipleRecipes (List<RecipeModel> recipes);
    @Update
    void updateRecipe (RecipeModel recipe);
    @Delete
    void deleteRecipe (RecipeModel recipe);
    @Query("SELECT * FROM RecipeModel")
    List<RecipeModel> getAllRecipes();
    @Query("SELECT * FROM RecipeModel WHERE recipe_id= :recipeID LIMIT 1")
    RecipeModel getOneRecipe(String recipeID);

    @Insert
    void insertOnlySingleRecipeDetails (RecipeDetails recipe);
    @Insert(onConflict = REPLACE)
    void insertMultipleRecipesDetails (List<RecipeDetails> recipes);
    @Update
    void updateRecipeDetails (RecipeDetails recipe);
    @Delete
    void deleteRecipeDetails (RecipeDetails recipe);
    @Query("SELECT * FROM RecipeDetails WHERE recipe_id= :recipeID LIMIT 1")
    RecipeDetails getOneDetailedRecipe(String recipeID);

    @Insert
    void insertMultipleMappings(List<MappingModel> mappings);
    @Query("SELECT * FROM MappingModel WHERE recipeID= :recipeID")
    List<MappingModel> getMappings(String recipeID);

    @Query("DELETE FROM recipemodel")
    void nukeRecipes();
    @Query("DELETE FROM RecipeDetails")
    void nukeDetails();
    @Query("DELETE FROM MappingModel")
    void nukeMappings();
}
