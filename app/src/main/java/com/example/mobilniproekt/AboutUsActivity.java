package com.example.mobilniproekt;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
}
