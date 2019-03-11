package com.example.mobilniproekt.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mobilniproekt.model.Recipe;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleMovie (RecipeModel recipe);
    @Insert
    void insertMultipleMovies (List<RecipeModel> recipes);
    @Update
    void updateMovie (RecipeModel recipe);
    @Delete
    void deleteMovie (RecipeModel recipe);

}
