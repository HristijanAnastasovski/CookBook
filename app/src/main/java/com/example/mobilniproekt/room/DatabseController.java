package com.example.mobilniproekt.room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mobilniproekt.model.RecipeDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.example.mobilniproekt.FavoritesActivity.semaphore;
import static com.example.mobilniproekt.FavoritesActivity.semaphoreList;
import static com.example.mobilniproekt.RecipeDetailsActivity.semaphore1;
import static com.example.mobilniproekt.RecipeDetailsActivity.semaphore2;

public class DatabseController {

    private Semaphore semaphoreSingleRecipeDetails;
    private Semaphore semaphoreForMappings;
    private static RecipeDatabase recipeDatabase;
    private boolean isAdded;
    private List<RecipeModel> recipes;
    private RecipeDetails recipeDetails;
    private List<String> ingreedients;

    public DatabseController(Context context){
        recipeDatabase= Room.databaseBuilder(context, RecipeDatabase.class, "recipe_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void initSemaphoreDetailedRecipe(){
        semaphoreSingleRecipeDetails=new Semaphore(0);
    }

    public void initSemaphoreForMapping(){
        semaphoreForMappings=new Semaphore(0);
    }

    public static void insertSingleRecipe(final RecipeModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeDatabase.daoAccess().insertOnlySingleRecipe(model);
                return null;
            }
        }.execute();
    }

    public static void insertDetailedRecipe(final RecipeDetails model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeDatabase.daoAccess().insertOnlySingleRecipeDetails(model);
                List<MappingModel> mappings=new ArrayList<>();
                for(String ingreedient : model.getIngredients()){
                    MappingModel mapping=new MappingModel(model.getRecipe_id(), ingreedient);
                    mappings.add(mapping);
                }
                recipeDatabase.daoAccess().insertMultipleMappings(mappings);
                return null;
            }
        }.execute();
    }

    public static void removeRecipe(final RecipeModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeDatabase.daoAccess().insertOnlySingleRecipe(model);
                return null;
            }
        }.execute();
    }

    public static void RemoveDetailedRecipe(final RecipeModel model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeDatabase.daoAccess().insertOnlySingleRecipe(model);
                return null;
            }
        }.execute();
    }

    public List<RecipeModel> getAllRecipes(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recipes=recipeDatabase.daoAccess().getAllRecipes();
                semaphoreList.release();
            }
        }).start();

        try {
            semaphoreList.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public RecipeDetails getOneDetailedRecipe(String recipeID) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recipeDetails=recipeDatabase.daoAccess().getOneDetailedRecipe(recipeID);
                semaphoreSingleRecipeDetails.release();
            }
        }).start();
        semaphoreSingleRecipeDetails.acquire();
        return recipeDetails;
    }

    public List<String> getIngreedients(String recipeID){
        ingreedients=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MappingModel> mappings=recipeDatabase.daoAccess().getMappings(recipeID);
                ingreedients=new ArrayList<>();
                for(MappingModel model:mappings){
                    ingreedients.add(model.getIngredient());
                }
                semaphoreForMappings.release();
            }
        }).start();
        try {
            semaphoreForMappings.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ingreedients;
    }

    public boolean checkIfExists(RecipeDetails recipe){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("tag", "Starting Thread");
                try {
                    semaphore1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("tag", "Ending Thread");
                Log.d("tag", "recipeID"+recipe.getRecipe_id());
                RecipeModel recipeModel=recipeDatabase.daoAccess().getOneRecipe(recipe.getRecipe_id());
                if(recipeModel==null) isAdded=false;
                else isAdded=true;
                semaphore2.release();
            }
        }).start();

        try {
            semaphore2.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isAdded;
    }

}
