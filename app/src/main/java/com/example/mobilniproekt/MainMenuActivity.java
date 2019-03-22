package com.example.mobilniproekt;

import android.app.ActivityManager;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mobilniproekt.room.RecipeDatabase;

import java.util.List;
import java.util.concurrent.Semaphore;

public class MainMenuActivity extends AppCompatActivity {

    private RecipeDatabase database;
    private static Semaphore semaphore=new Semaphore(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        initBottomNavigation();



        /*
        database= Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "recipes_db")
                .fallbackToDestructiveMigration()
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.daoAccess().nukeRecipes();
                database.daoAccess().nukeDetails();
                database.daoAccess().nukeMappings();
                semaphore.release();
            }
        }).start();

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


        Button btnToSearch = findViewById(R.id.mainMenuToSearchButton);
        Button btnToFavorites = findViewById(R.id.mainMenuToFavoritesButton);
        Button btnToAbout = findViewById(R.id.mainMenuToAboutButton);
        Button btnToExit= findViewById(R.id.mainMenuToExitButton);

        btnToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSearch = new Intent(MainMenuActivity.this,SearchActivity.class);
                startActivity(intentToSearch);
            }
        });

        btnToExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure that you want to exit?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
            }
        });

        btnToAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                ActivityManager manager=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskList = manager.getRunningTasks(10);
                switch(menuItem.getItemId()){
                    case R.id.ic_search:
                        Intent intent1 = new Intent (MainMenuActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        if(taskList.get(0).numActivities!=1) finish();
                        break;
                    case R.id.ic_favorites:
                        Intent intent2 = new Intent (MainMenuActivity.this,FavoritesActivity.class);
                        startActivity(intent2);
                        if(taskList.get(0).numActivities!=1) finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        System.exit(0);
    }
}
