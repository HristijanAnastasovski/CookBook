package com.finki.mobilniproekt.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.finki.mobilniproekt.model.RecipeDetails;

@Database(entities = {RecipeModel.class, RecipeDetails.class, MappingModel.class}, version = 2, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();


}
