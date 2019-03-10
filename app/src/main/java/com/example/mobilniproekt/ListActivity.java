package com.example.mobilniproekt;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilniproekt.model.Recipe;
import com.example.mobilniproekt.model.Recipes;
import com.example.mobilniproekt.recycleview.CustomAdapter;
import com.example.mobilniproekt.retrofit.GetDataService;
import com.example.mobilniproekt.retrofit.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Recipes> call = service.getRecipes();
        call.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                progressDoalog.dismiss();
                TextView textViewTest=(TextView) findViewById(R.id.textViewTest);
                textViewTest.setText(response.body().getRecipeList().get(10).getTitle());
                Log.d(response.body().getRecipeList().get(10).getTitle(), response.body().getRecipeList().get(10).getPublisher());
                //generateDataList(response.body().getRecipeList());
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });
    }
    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Recipe> recipes) {
        recyclerView = findViewById(R.id.RecyclerView1);
        adapter = new CustomAdapter(this,recipes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
