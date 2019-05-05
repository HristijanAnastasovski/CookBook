package com.finki.mobilniproekt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInWithMailActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button signInButton;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_with_mail_layout);


        email = findViewById(R.id.emailSignInInfo);
        password = findViewById(R.id.passwordSignInInfo);

        signInButton = findViewById(R.id.signInProceedBtn);

        emailLayout = findViewById(R.id.emailSignInInfoErrorLayout);
        passwordLayout = findViewById(R.id.passwordSignInInfoErrorLayout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");


        mAuth = FirebaseAuth.getInstance();

        mAdView = findViewById(R.id.adViewMail);

        //test : ca-app-pub-3940256099942544/6300978111
        //my : ca-app-pub-2112536401499963/1376229189
        //mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);





        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput())
                {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SignInWithMailActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        progressDialog.hide();
                                        Intent intentToMainMenu = new Intent(SignInWithMailActivity.this,MainMenuActivity.class);
                                        startActivity(intentToMainMenu);

                                        Toast.makeText(SignInWithMailActivity.this, "Welcome "+user.getEmail().split("@")[0],
                                                Toast.LENGTH_LONG).show();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e("exception", task.getException().toString());
                                        progressDialog.hide();
                                        if(task.getException() instanceof FirebaseNetworkException){
                                            Toast.makeText(SignInWithMailActivity.this, "Internet connection problem.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            emailLayout.setError("Invalid username or password");
                                            passwordLayout.setError("Invalid username or password");
                                            Toast.makeText(SignInWithMailActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    // ...
                                }
                            });
                }
            }
        });

    }

    public boolean validateInput(){
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() )
        {
        if(email.getText().toString().isEmpty())
            emailLayout.setError("Required field.");
        else
            emailLayout.setError(null);
        if(password.getText().toString().isEmpty())
            passwordLayout.setError("Required field.");
        else
            passwordLayout.setError(null);

        Toast.makeText(SignInWithMailActivity.this,"Please fill all required fields.",Toast.LENGTH_LONG).show();
        return false;
        }



        emailLayout.setError(null);
        passwordLayout.setError(null);
        progressDialog.show();
        return true;
    }


}
