package com.example.mobilniproekt;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.mobilniproekt.room.DatabseController;
import com.example.mobilniproekt.room.MappingModel;
import com.example.mobilniproekt.room.RecipeDatabase;
import com.example.mobilniproekt.room.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

//Activity that shows all the details about the recipe (activates on recipe click)
public class RecipeDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;

    private ImageView imageView;
   // private ImageView heartImage;
    private TextView authorNameTextView ;
    private Button fullGuideButton;
    private FirebaseAuth firebaseAuth;

    private DatabseController database;

    private RecipeDetails recipe;

    private boolean isAdded;

    public static Semaphore semaphore1=new Semaphore(0);
    public static Semaphore semaphore2=new Semaphore(0);
    private Toolbar toolbar;
    private MenuItem btnFavorites=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        initBottomNavigation();
       // Intent intent = getIntent();
       // String id = intent.getStringExtra(getString(R.string.id_details));

        imageView = findViewById(R.id.detailsImage);
        //heartImage=findViewById(R.id.imageViewHeart);
        authorNameTextView =findViewById(R.id.authorNameTextView);
        fullGuideButton = findViewById(R.id.buttonToFullRecipe);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        firebaseAuth=FirebaseAuth.getInstance();


        listInit();
        //getDetailsData(id);

        database=new DatabseController(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String id = intent.getStringExtra(getString(R.string.id_details));
        getDetailsData(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        btnFavorites=menu.findItem(R.id.button_favorite);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.button_favorite) {
            Toast toast=new Toast(getApplicationContext());
            if(!isAdded) {
                database.insertDetailedRecipe(recipe);
                isAdded=!isAdded;

                toast.makeText(getApplicationContext(), "Recipe Successfully Added To My Favorites", Toast.LENGTH_LONG).show();
                item.setIcon(R.drawable.ic_favorite_red_filled);
            }
            else {
                database.removeDetailedRecipe(recipe);
                isAdded=!isAdded;

                toast.makeText(getApplicationContext(), "Recipe Successfully Removed From My Favorites", Toast.LENGTH_LONG).show();
                item.setIcon(R.drawable.ic_favorite_white);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDetailsData(String id)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RecipeDetailsWrapper> call = service.getRecipeDetail(id);
        call.enqueue(new Callback<RecipeDetailsWrapper>() {
            @Override
            public void onResponse(Call<RecipeDetailsWrapper> call, Response<RecipeDetailsWrapper> response) {

                recipe=response.body().getRecipeDetails();

                Glide.with(RecipeDetailsActivity.this)
                        .load(response.body().getRecipeDetails().getImage_url())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .thumbnail(Glide.with(RecipeDetailsActivity.this)
                                .load(R.drawable.ic_launcher_background))
                        .apply(new RequestOptions().override(600, 600))
                        .into(imageView);

                setTitle(response.body().getRecipeDetails().getTitle().replaceAll("&nbsp;"," ").replaceAll("&amp;","&"));
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

                semaphore1.release();

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user==null) recipe.setUser("guest");
                else recipe.setUser(user.getEmail());
                recipe.setRecipe_id(recipe.getRecipe_id()+recipe.getUser());

                isAdded=database.checkIfExists(recipe);
                Log.d("is added", Boolean.toString(isAdded));

                if(isAdded) {
                    if(btnFavorites!=null)
                    btnFavorites.setIcon(R.drawable.ic_favorite_red_filled);
                    //heartImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.redHeart));
                }

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

    private RecipeModel setRecipeModel(RecipeDetails recipe){
        RecipeModel model=new RecipeModel();
        model.setF2f_url(recipe.getF2f_url());
        model.setImage_url(recipe.getImage_url());
        model.setPublisher(recipe.getPublisher());
        model.setPublisher_url(recipe.getPublisher_url());
        model.setRecipe_id(recipe.getRecipe_id());
        model.setTitle(recipe.getTitle());
        model.setSocial_rank(recipe.getSocial_rank());
        model.setSource_url(recipe.getSource_url());
        return model;
    }

    public void initBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent (RecipeDetailsActivity.this,MainMenuActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent (RecipeDetailsActivity.this,SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_favorites:
                        Intent intent3 = new Intent (RecipeDetailsActivity.this,FavoritesActivity.class);
                        startActivity(intent3);
                        break;

                }
                return false;
            }
        });
    }
}
