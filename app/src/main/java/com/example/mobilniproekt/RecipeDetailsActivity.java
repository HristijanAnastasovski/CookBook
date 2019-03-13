package com.example.mobilniproekt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobilniproekt.adapters.CardViewAdapter;
import com.example.mobilniproekt.adapters.IngredientsAdapter;
import com.example.mobilniproekt.model.RecipeDetails;
import com.example.mobilniproekt.model.RecipeDetailsWrapper;
import com.example.mobilniproekt.model.Recipes;
import com.example.mobilniproekt.retrofit.GetDataService;
import com.example.mobilniproekt.retrofit.RetrofitClientInstance;
import com.example.mobilniproekt.room.RecipeDatabase;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Activity that shows all the details about the recipe (activates on recipe click)
public class RecipeDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        Intent intent = getIntent();
        String id = intent.getStringExtra(getString(R.string.id_details));

        listInit();
        getDetailsData(id);

    }

    public void getDetailsData(String id)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RecipeDetailsWrapper> call = service.getRecipeDetail(id);
        call.enqueue(new Callback<RecipeDetailsWrapper>() {
            @Override
            public void onResponse(Call<RecipeDetailsWrapper> call, Response<RecipeDetailsWrapper> response) {

                ImageView imageView = findViewById(R.id.detailsImage);
                Glide.with(RecipeDetailsActivity.this)
                        .load(response.body().getRecipeDetails().getImage_url())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .thumbnail(Glide.with(RecipeDetailsActivity.this)
                                .load(R.drawable.ic_launcher_background))
                        .apply(new RequestOptions().override(600, 400))
                        .into(imageView);

                setTitle(response.body().getRecipeDetails().getTitle());
                ingredientsAdapter.updateData(response.body().getRecipeDetails().getIngredients());
                TextView authorNameTextView =(TextView) findViewById(R.id.authorNameTextView);
                authorNameTextView.setText(response.body().getRecipeDetails().getPublisher());

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<RecipeDetailsWrapper> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void listInit()
    {
        recyclerView=findViewById(R.id.recyclerViewIngredients);
        ingredientsAdapter=new IngredientsAdapter(RecipeDetailsActivity.this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ingredientsAdapter);
    }
}
