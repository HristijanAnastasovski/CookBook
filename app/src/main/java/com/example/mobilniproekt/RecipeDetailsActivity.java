package com.example.mobilniproekt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Activity that shows all the details about the recipe (activates on recipe click)
public class RecipeDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    private ImageView imageView;
    private TextView authorNameTextView ;
    private Button fullGuideButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        Intent intent = getIntent();
        String id = intent.getStringExtra(getString(R.string.id_details));

        imageView = findViewById(R.id.detailsImage);
        authorNameTextView =findViewById(R.id.authorNameTextView);
        fullGuideButton = findViewById(R.id.buttonToFullRecipe);

        listInit();
        getDetailsData(id);





    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;

    }*/

    public void getDetailsData(String id)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

       /* List<String> lista = new ArrayList<>();
        String author="Author";
        for(int i=1;i<=15;i++)
        {
            lista.add(Integer.toString(i));
        }
        lista.add("asdasasdadsasdadsadsadsdasasdasasdadsa sdadsads adsdas");
        ingredientsAdapter.updateData(lista);
        TextView authorNameTextView =(TextView) findViewById(R.id.authorNameTextView);
        authorNameTextView.setText(author);
        progressDialog.dismiss();*/

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RecipeDetailsWrapper> call = service.getRecipeDetail(id);
        call.enqueue(new Callback<RecipeDetailsWrapper>() {
            @Override
            public void onResponse(Call<RecipeDetailsWrapper> call, Response<RecipeDetailsWrapper> response) {


                Glide.with(RecipeDetailsActivity.this)
                        .load(response.body().getRecipeDetails().getImage_url())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .thumbnail(Glide.with(RecipeDetailsActivity.this)
                                .load(R.drawable.ic_launcher_background))
                        .apply(new RequestOptions().override(600, 400))
                        .into(imageView);

                setTitle(response.body().getRecipeDetails().getTitle());
                ingredientsAdapter.updateData(response.body().getRecipeDetails().getIngredients());
                authorNameTextView.setText(response.body().getRecipeDetails().getPublisher());
                fullGuideButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = response.body().getRecipeDetails().getSource_url();
                        Intent fullGuideIntent = new Intent(Intent.ACTION_VIEW);
                        fullGuideIntent.setData(Uri.parse(url));
                        startActivity(fullGuideIntent);
                    }
                });






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
