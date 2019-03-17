package com.example.mobilniproekt;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobilniproekt.adapters.IngredientsAdapter;
import com.example.mobilniproekt.model.RecipeDetails;
import com.example.mobilniproekt.room.DatabseController;
import com.example.mobilniproekt.room.MappingModel;
import com.example.mobilniproekt.room.RecipeDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RecipeDetailsOfflineActivity extends AppCompatActivity {

    private DatabseController database;
    private String recipeID;
    private RecipeDetails recipe;

    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ImageView imageView;
    private TextView authorNameTextView;
    private TextView tagTextView;
    private TextView authorTextView;
    private Button fullGuideButton;

    private List<String> ingreedients;

    public static Semaphore semaphore=new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_offline);

        Intent intent=getIntent();
        recipeID=intent.getStringExtra("Recipe ID");

        Log.d("tag", "testing1");

        database=new DatabseController(getApplicationContext());
        database.initSemaphoreDetailedRecipe();
        database.initSemaphoreForMapping();

        Log.d("tag", "testing2");

        listInit();
        try {
            recipe=database.getOneDetailedRecipe(recipeID);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ingreedients=database.getIngreedients(recipeID);
        semaphore.release();

        try {
            semaphore.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ingredientsAdapter.updateData(ingreedients);
        initData(recipe);
    }

    private void initData(RecipeDetails recipe){
        imageView=(ImageView) findViewById(R.id.detailsImageOffline);
        Glide.with(RecipeDetailsOfflineActivity.this)
                .load(recipe.getImage_url())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(RecipeDetailsOfflineActivity.this)
                        .load(R.drawable.ic_launcher_background))
                .apply(new RequestOptions().override(600, 400))
                .into(imageView);
        authorTextView=(TextView) findViewById(R.id.authorTextViewOffline);
        authorNameTextView=(TextView) findViewById(R.id.authorNameTextViewOffline);
        tagTextView=(TextView) findViewById(R.id.ingredientsTextViewTagOffline);
        authorTextView.setText(recipe.getPublisher());
    }

    private void listInit()
    {
        recyclerView=findViewById(R.id.recyclerViewIngredientsOffline);
        ingredientsAdapter=new IngredientsAdapter(RecipeDetailsOfflineActivity.this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ingredientsAdapter);
    }
}
