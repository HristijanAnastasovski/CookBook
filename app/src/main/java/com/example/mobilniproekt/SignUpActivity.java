package com.example.mobilniproekt;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private Button signUpButton;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout rePasswordLayout;
    private FirebaseAuth mAuth;
    boolean accountExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        email = findViewById(R.id.emailSignUpInfo);
        password = findViewById(R.id.passwordSignUpInfo);
        rePassword = findViewById(R.id.rePasswordSignUpInfo);
        signUpButton = findViewById(R.id.signUpProceedBtn);

        emailLayout = findViewById(R.id.emailSignUpInfoErrorLayout);
        passwordLayout = findViewById(R.id.passwordSignUpInfoErrorLayout);
        rePasswordLayout = findViewById(R.id.rePasswordSignUpInfoErrorLayout);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput())
                {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intentToMainMenu = new Intent(SignUpActivity.this,MainMenuActivity.class);
                                        startActivity(intentToMainMenu);
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                       // updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
            }
        });
    }



    public boolean validateInput()
    {

        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || rePassword.getText().toString().isEmpty())
        {
            if(email.getText().toString().isEmpty())
                emailLayout.setError("Required field.");
            else
                emailLayout.setError(null);
            if(password.getText().toString().isEmpty())
                passwordLayout.setError("Required field.");
            else
                passwordLayout.setError(null);
            if(rePassword.getText().toString().isEmpty())
                rePasswordLayout.setError("Required field.");
            else
                rePasswordLayout.setError(null);
            Toast.makeText(SignUpActivity.this,"Please fill all required fields.",Toast.LENGTH_LONG).show();
            return false;
        }

        if(!email.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
        {     emailLayout.setError("Not a valid e-mail address");
            return false;
        }
        else
            emailLayout.setError(null);




        mAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()){

                    try {

                        List<String> list = task.getResult().getSignInMethods();
                        accountExist=!list.isEmpty();
                        if(accountExist)
                        {
                            emailLayout.setError("Account already exists.");

                        }

                    } catch (Exception e)
                    {
                        Toast.makeText(SignUpActivity.this,"Internal error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




        if(password.getText().toString().length()<6)
        {
            passwordLayout.setError("Your password must be at least 6 characters.");
            rePasswordLayout.setError("Your password must be at least 6 characters.");
            return false;
        }
        else
        {
            passwordLayout.setError(null);
            rePasswordLayout.setError(null);
        }
        if(!password.getText().toString().equals(rePassword.getText().toString()))
        {
            passwordLayout.setError("Passwords do not match.");
            rePasswordLayout.setError("Passwords do not match.");
            return false;
        }
        else{
            passwordLayout.setError(null);
            rePasswordLayout.setError(null);
        }



        emailLayout.setError(null);
        passwordLayout.setError(null);
        rePasswordLayout.setError(null);
        //Toast.makeText(SignUpActivity.this,"Success",Toast.LENGTH_LONG).show();
        return true;
    }
}
