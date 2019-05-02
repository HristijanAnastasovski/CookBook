package com.example.mobilniproekt;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.example.mobilniproekt.room.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RecipeDetailsOfflineActivity extends AppCompatActivity {

    private DatabseController database;
    private String recipeID;
    private RecipeDetails recipe;
    private Button removeFromFavorites;

    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ImageView imageView;
    private TextView authorNameTextView;
    private TextView tagTextView;
    private TextView authorTextView;
    private Button fullGuideButton;
    private String user;

    private FirebaseAuth firebaseAuth;

    private List<String> ingreedients;

    private Intent mainIntent;

    public static Semaphore semaphore=new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details_offline);
        initBottomNavigation();
        mainIntent=getIntent();
        recipeID=mainIntent.getStringExtra("Recipe ID");
        database=new DatabseController(getApplicationContext());
        database.initSemaphoreDetailedRecipe();
        database.initSemaphoreForMapping();
        database.initSemaphoreForSingleRecipe();

        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser==null) user="guest";
        else user=firebaseUser.getEmail();

        removeFromFavorites=(Button) findViewById(R.id.buttonToRemoveOffline);

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

        recipe.setIngredients(ingreedients);
        ingredientsAdapter.updateData(ingreedients);
        initData(recipe);

        removeFromFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeDetailsOfflineActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure that you want to remove the item from the Favorites List?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        database.removeDetailedRecipe(recipe);
                        Intent intent=new Intent();
                        intent.putExtra("remove", "yes");
                        intent.putExtra("position", mainIntent.getStringExtra("position"));
                        setResult(RESULT_OK, intent);
                        dialog.dismiss();
                        finish();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent();
        intent.putExtra("remove", "no");
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initData(RecipeDetails recipe){
        imageView=(ImageView) findViewById(R.id.detailsImageOffline);
        Glide.with(RecipeDetailsOfflineActivity.this)
                .load(recipe.getImage_url())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(RecipeDetailsOfflineActivity.this)
                        .load(R.drawable.ic_launcher_background))
                .apply(new RequestOptions().override(600, 600))
                .into(imageView);
        authorTextView=(TextView) findViewById(R.id.authorTextViewOffline);
        authorNameTextView=(TextView) findViewById(R.id.authorNameTextViewOffline);
        tagTextView=(TextView) findViewById(R.id.ingredientsTextViewTagOffline);
        authorNameTextView.setText(recipe.getPublisher());
        setTitle(recipe.getTitle().replaceAll("&nbsp;"," ").replaceAll("&amp;","&"));
    }

    private void listInit()
    {
        recyclerView=findViewById(R.id.recyclerViewIngredientsOffline);
        ingredientsAdapter=new IngredientsAdapter(RecipeDetailsOfflineActivity.this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ingredientsAdapter);
    }

    public void initBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent (RecipeDetailsOfflineActivity.this,MainMenuActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent (RecipeDetailsOfflineActivity.this,SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_favorites:
                        Intent intent3 = new Intent (RecipeDetailsOfflineActivity.this,FavoritesActivity.class);
                        startActivity(intent3);
                        break;

                }
                return false;
            }
        });
    }
}
