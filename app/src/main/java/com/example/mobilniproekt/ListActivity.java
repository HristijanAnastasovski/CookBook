package com.example.mobilniproekt;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.example.mobilniproekt.adapters.CardViewAdapter;
import com.example.mobilniproekt.model.Recipe;
import com.example.mobilniproekt.model.Recipes;

import com.example.mobilniproekt.retrofit.GetDataService;
import com.example.mobilniproekt.retrofit.RetrofitClientInstance;
import com.example.mobilniproekt.room.RecipeDatabase;

import org.w3c.dom.Text;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {



    //private CustomAdapter adapter;


    private RecyclerView recyclerView;
    private CardViewAdapter cardViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private RecipeDatabase recipeDatabase;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        listInit();
        initBottomNavigation();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString(ListActivity.this.getString(R.string.queryString));
        getRecipes(message);


    }
    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Recipe> recipes) {
       /* recyclerView = findViewById(R.id.RecyclerView1);
        adapter = new CustomAdapter(this,recipes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);*/



    }

    public void getRecipes(String s)
    {
        if(!haveNetworkConnection())
        {
            TextView noRecipesMatching = findViewById(R.id.noRecipesTextView);
            noRecipesMatching.setText("Please check your internet connection.");
            noRecipesMatching.setVisibility(View.VISIBLE);
            ImageView sadChefImageView = findViewById(R.id.sadChefImageView);
            sadChefImageView.setVisibility(View.VISIBLE);
        }
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Recipes> call = service.getRecipes(s);
        call.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                progressDialog.dismiss();

               // if(isNetworkConnected() && ) {
                    if (response.body().getCount() > 0) {
                        cardViewAdapter.updateData(response.body().getRecipeList());
                    } else {
                        TextView noRecipesMatching = findViewById(R.id.noRecipesTextView);
                        noRecipesMatching.setVisibility(View.VISIBLE);
                        ImageView sadChefImageView = findViewById(R.id.sadChefImageView);
                        sadChefImageView.setVisibility(View.VISIBLE);
                    }
               /* }else
                {
                    TextView noRecipesMatching = findViewById(R.id.noRecipesTextView);
                    noRecipesMatching.setText(ListActivity.this.getString(R.string.noInternet));
                    noRecipesMatching.setVisibility(View.VISIBLE);
                    ImageView sadChefImageView = findViewById(R.id.sadChefImageView);
                    sadChefImageView.setVisibility(View.VISIBLE);
                }*/

            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    /*private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }/*

    /*Method that initializes the RecyclerView*/
    private void listInit()
    {
        recyclerView=findViewById(R.id.recyclerView);
        cardViewAdapter=new CardViewAdapter(ListActivity.this, recipeDatabase);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cardViewAdapter);

    }

    public void initBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent (ListActivity.this,MainMenuActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent (ListActivity.this,SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_favorites:
                        Intent intent3 = new Intent (ListActivity.this,FavoritesActivity.class);
                        startActivity(intent3);
                        break;

                }
                return false;
            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
