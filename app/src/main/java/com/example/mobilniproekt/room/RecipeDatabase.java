package com.example.mobilniproekt.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.mobilniproekt.model.RecipeDetails;

@Database(entities = {RecipeModel.class, RecipeDetails.class, MappingModel.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();


}
