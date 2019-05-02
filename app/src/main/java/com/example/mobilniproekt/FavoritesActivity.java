package com.example.mobilniproekt;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilniproekt.adapters.FavoritesAdapter;
import com.example.mobilniproekt.model.RecipeDetails;
import com.example.mobilniproekt.room.DatabseController;
import com.example.mobilniproekt.room.RecipeDatabase;
import com.example.mobilniproekt.room.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Semaphore;

public class FavoritesActivity extends AppCompatActivity {

    private DatabseController database;
    private List<RecipeDetails> recipeModelList;
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private ImageView sadChefImage;
    private TextView noFavoritesText;
    private FirebaseAuth firebaseAuth;
    private String user;
    public static Semaphore semaphore=new Semaphore(0);
    public static Semaphore semaphoreList=new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initBottomNavigation();

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null) user="guest";
        else user=firebaseUser.getEmail();

        database=new DatabseController(getApplicationContext());
        semaphore.release();

        Log.d("user", user);

        recipeModelList=database.getAllRecipes(user);

        for(RecipeDetails recipeDetails:recipeModelList){
            Log.d("name", recipeDetails.getTitle());
        }

        initRecyclerView();

        if(recipeModelList.isEmpty())
        {
            sadChefImage = findViewById(R.id.sadChefImageViewFavorites);
            sadChefImage.setVisibility(View.VISIBLE);

            noFavoritesText = findViewById(R.id.noRecipesTextViewFavorites);
            noFavoritesText.setVisibility(View.VISIBLE);
        }

    }

    private void initRecyclerView(){
        if(recipeModelList==null) return;
        recyclerView=findViewById(R.id.recyclerViewFavorites);
        favoritesAdapter=new FavoritesAdapter(recipeModelList, this);
        recyclerView.setAdapter(favoritesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999 && resultCode==RESULT_OK){
            if(data.getStringExtra("remove").equals("yes")){
                favoritesAdapter.delete(Integer.parseInt(data.getStringExtra("position")));
            }
        }
    }

    public void initBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_search:
                        Intent intent1 = new Intent (FavoritesActivity.this,SearchActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent (FavoritesActivity.this,MainMenuActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(FavoritesActivity.this,MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
