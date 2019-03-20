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
import android.widget.TextView;

import com.example.mobilniproekt.adapters.FavoritesAdapter;
import com.example.mobilniproekt.room.DatabseController;
import com.example.mobilniproekt.room.RecipeDatabase;
import com.example.mobilniproekt.room.RecipeModel;

import java.util.List;
import java.util.concurrent.Semaphore;

public class FavoritesActivity extends AppCompatActivity {

    private DatabseController database;
    private List<RecipeModel> recipeModelList;
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    public static Semaphore semaphore=new Semaphore(0);
    public static Semaphore semaphoreList=new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initBottomNavigation();
        database=new DatabseController(getApplicationContext());
        semaphore.release();

        recipeModelList=database.getAllRecipes();

        initRecyclerView();

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
                Log.d("number", data.getStringExtra("position"));
                favoritesAdapter.delete(Integer.parseInt(data.getStringExtra("position")));
            }
            else if(data.getStringExtra("remove").equals("no")){
                Log.d("message", "not removed");
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
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent (FavoritesActivity.this,MainMenuActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
    }
}
