package com.example.mobilniproekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SignInOptionsActivity extends AppCompatActivity {
    public LinearLayout signUpLink;
    public LinearLayout signInWithMail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_options_layout);

        signUpLink = findViewById(R.id.signUpText);
        signUpLink.setClickable(true);

        signInWithMail = findViewById(R.id.signInWithMail);
        signInWithMail.setClickable(true);

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUpIntent = new Intent(SignInOptionsActivity.this,SignUpActivity.class);
                startActivity(toSignUpIntent);
            }
        });

        signInWithMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignInWithMail = new Intent(SignInOptionsActivity.this,SignInWithMailActivity.class);
                startActivity(toSignInWithMail);
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInOptionsActivity.this);

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
}
