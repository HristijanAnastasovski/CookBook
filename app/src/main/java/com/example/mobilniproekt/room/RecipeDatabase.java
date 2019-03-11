package com.example.mobilniproekt.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RecipeModel.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();


}
