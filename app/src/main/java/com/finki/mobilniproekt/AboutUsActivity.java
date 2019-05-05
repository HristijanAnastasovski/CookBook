package com.finki.mobilniproekt;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class AboutUsActivity extends AppCompatActivity  {
    TextView textViewContact1;
    TextView textViewContact2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_layout);
        textViewContact1 = findViewById(R.id.textViewContact1);
        textViewContact2 = findViewById(R.id.textViewContact2);

        textViewContact1.setPaintFlags(textViewContact1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        textViewContact2.setPaintFlags(textViewContact2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        initBottomNavigation();

        textViewContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "anastasovski.h@gmail.com"));
                startActivity(emailIntent);
            }
        });

        textViewContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "ljuben_angelkoski@live.com"));
                startActivity(emailIntent);
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
                        Intent intent1 = new Intent (AboutUsActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        if(taskList.get(0).numActivities!=1) finish();
                        break;
                    case R.id.ic_favorites:
                        Intent intent2 = new Intent (AboutUsActivity.this,FavoritesActivity.class);
                        startActivity(intent2);
                        if(taskList.get(0).numActivities!=1) finish();
                        break;

                    case R.id.ic_home:
                        Intent intent3 = new Intent (AboutUsActivity.this,MainMenuActivity.class);
                        startActivity(intent3);
                        if(taskList.get(0).numActivities!=1) finish();

                }
                return false;
            }
        });
    }
}
