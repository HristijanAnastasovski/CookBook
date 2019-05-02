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
    private Semaphore semaphoreForSingleRecipe;
    private static RecipeDatabase recipeDatabase;
    private boolean isAdded;
    private List<RecipeDetails> recipes;
    private RecipeDetails recipeDetails;
    private RecipeModel recipeModel;
    private List<String> ingreedients;

    public DatabseController(Context context){
        recipeDatabase= Room.databaseBuilder(context, RecipeDatabase.class, "recipes_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void initSemaphoreDetailedRecipe(){
        semaphoreSingleRecipeDetails=new Semaphore(0);
    }

    public void initSemaphoreForMapping(){
        semaphoreForMappings=new Semaphore(0);
    }

    public void initSemaphoreForSingleRecipe(){
        semaphoreForSingleRecipe=new Semaphore(0);
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
                recipeDatabase.daoAccess().deleteRecipe(model);
                return null;
            }
        }.execute();
    }

    public static void removeDetailedRecipe(final RecipeDetails model) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeDatabase.daoAccess().deleteRecipeDetails(model);
                List<MappingModel> mappingModels=new ArrayList<>();
                for(String ingreedient:model.getIngredients()){
                    MappingModel mapping=new MappingModel(model.getRecipe_id(), ingreedient);
                    mappingModels.add(mapping);
                }
                recipeDatabase.daoAccess().deleteMultipleMappings(mappingModels);
                return null;
            }
        }.execute();
    }

    public static void nuke() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeDatabase.daoAccess().nukeDetails();
                recipeDatabase.daoAccess().nukeRecipes();
                recipeDatabase.daoAccess().nukeMappings();
                return null;
            }
        }.execute();
    }

    public List<RecipeDetails> getAllRecipes(String user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recipes=recipeDatabase.daoAccess().getMultipleDetailedRecipes(user);
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

    public RecipeModel getOneRecipe(String recipeID){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   recipeModel=recipeDatabase.daoAccess().getOneRecipe(recipeID);
                   semaphoreForSingleRecipe.release();
               }
           }).start();
        try {
            semaphoreForSingleRecipe.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return recipeModel;
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
        Log.d("RecipeID", recipe.getRecipe_id());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RecipeDetails recipeModel=recipeDatabase.daoAccess().getOneDetailedRecipe(recipe.getRecipe_id());
                if (recipeModel==null) isAdded=false;
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
