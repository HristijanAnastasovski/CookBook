package com.example.mobilniproekt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilniproekt.adapters.IngredientsAdapter;
import com.example.mobilniproekt.adapters.IngredientsSelectionAdapter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.mobilniproekt.adapters.IngredientsSelectionAdapter.LIST_EMPTY;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IngredientsSelectionAdapter ingredientsSelectionAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EditText ingredientName;
    private Button addIngredient;
    private List<String> ingredientsList;
    private TextView currentlyEmptyText;
    private Button searchForRecipesButton;
    private BottomNavigationView navigation;
    private EmptyListBroadcastReceiver receiver=new EmptyListBroadcastReceiver();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_ingredients_menu);
        listInit();
        initBottomNavigation();
        ingredientsList= new ArrayList<>();
        ingredientName = findViewById(R.id.ingredientNameEditText);
        addIngredient=findViewById(R.id.ingredientAddButton);
        currentlyEmptyText = findViewById(R.id.currentlyEmptyTextView);
        searchForRecipesButton = findViewById(R.id.searchForRecipesButton);
        navigation = findViewById(R.id.bottomNav);

        IntentFilter filter = new IntentFilter(LIST_EMPTY);
        this.registerReceiver(receiver,filter);


        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){

                            navigation.setVisibility(View.INVISIBLE);

                        }else{

                            navigation.setVisibility(View.VISIBLE);

                        }
                    }
                });


        ingredientName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    addIngredientToList();
                    return true;
                }
                return false;
            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientToList();
            }
        });

        searchForRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ingredientsSelectionAdapter.getIngredients().size()>0)
                {
                    List <String> ingredients = new ArrayList<>(ingredientsSelectionAdapter.getIngredients());
                    StringBuilder sb= new StringBuilder();
                    for(int i=0;i<ingredients.size();i++)
                    {
                        sb.append(ingredients.get(i).replaceAll(" ","%20"));
                        if(i+1<ingredients.size())
                        sb.append(",");
                    }
                    Intent intent=new Intent(SearchActivity.this, ListActivity.class);
                    intent.putExtra(SearchActivity.this.getString(R.string.queryString),sb.toString());
                    startActivity(intent);


                }
                else
                    Toast.makeText(SearchActivity.this,"Please add at least one ingredient before searching!",Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    public void addIngredientToList()
    {
        if(!ingredientName.getText().toString().isEmpty())
        {
            ingredientsSelectionAdapter.updateData(ingredientName.getText().toString());
            currentlyEmptyText.setVisibility(View.INVISIBLE);
            ingredientName.setText("");
            hideKeyboard();


        }
    }

    public void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void listInit()
    {
        recyclerView=findViewById(R.id.recyclerViewSelectedIngredients);
        ingredientsSelectionAdapter=new IngredientsSelectionAdapter(SearchActivity.this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ingredientsSelectionAdapter);

    }

    public void initBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent (SearchActivity.this,MainMenuActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;
                    case R.id.ic_favorites:
                        Intent intent2 = new Intent (SearchActivity.this,FavoritesActivity.class);
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
        Intent intent=new Intent(SearchActivity.this,MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public class EmptyListBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            currentlyEmptyText.setVisibility(View.VISIBLE);
        }
    }
}
