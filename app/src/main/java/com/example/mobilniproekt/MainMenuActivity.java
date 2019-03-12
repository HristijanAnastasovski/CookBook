package com.example.mobilniproekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
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
    }
}
