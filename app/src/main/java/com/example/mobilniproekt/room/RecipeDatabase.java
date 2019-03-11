package com.example.mobilniproekt.room;

import android.arch.persistence.room.Database;

@Database(entities = {RecipeModel.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase {

    public abstract DaoAccess daoAccess();

}
