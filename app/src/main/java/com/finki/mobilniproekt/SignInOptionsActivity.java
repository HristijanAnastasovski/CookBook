package com.finki.mobilniproekt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignInOptionsActivity extends AppCompatActivity {
    public LinearLayout signUpLink;
    public LinearLayout signInWithMail;
    public LinearLayout continueAsGuest;
    public CallbackManager mCallbackManager;
    public LinearLayout signInWithFacebook;
    public FirebaseAuth mAuth;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_options_layout);

        Get_hash_key();
        signUpLink = findViewById(R.id.signUpText);
        signUpLink.setClickable(true);

        signInWithMail = findViewById(R.id.signInWithMail);
        signInWithMail.setClickable(true);

        continueAsGuest = findViewById(R.id.continueAsGuest);
        continueAsGuest.setClickable(true);

        signInWithFacebook = findViewById(R.id.signInWithFacebook);
        signInWithFacebook.setClickable(true);

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();



        mAdView = findViewById(R.id.adView);

        //test : ca-app-pub-3940256099942544/6300978111
        //my : ca-app-pub-2112536401499963/1376229189
        //mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
       AdRequest adRequest = new AdRequest.Builder().build();
       mAdView.loadAd(adRequest);


        //to sign in with mail activity
        signInWithMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignInWithMail = new Intent(SignInOptionsActivity.this,SignInWithMailActivity.class);
                startActivity(toSignInWithMail);
            }
        });

        continueAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent continueAsGuestIntent = new Intent (SignInOptionsActivity.this,MainMenuActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(continueAsGuestIntent);
                Toast.makeText(SignInOptionsActivity.this, "Welcome guest",
                        Toast.LENGTH_LONG).show();
            }
        });

        //to sign up activity
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUpIntent = new Intent(SignInOptionsActivity.this,SignUpActivity.class);
                startActivity(toSignUpIntent);
            }
        });


        signInWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(SignInOptionsActivity.this, Arrays.asList("email"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignInOptionsActivity.this, "Authentication cancelled.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("Error facebook auth:", error.toString());
                        if(error.toString().equals("CONNECTION_FAILURE: CONNECTION_FAILURE"))
                            Toast.makeText(SignInOptionsActivity.this, "Internet connection problem.",
                                    Toast.LENGTH_SHORT).show();
                        else
                        Toast.makeText(SignInOptionsActivity.this, "Authentication error.",
                                Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });



    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



// Prompt the user to re-provide their sign-in credentials





        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("success","successful login");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInOptionsActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent logInWithFacebookIntent = new Intent (SignInOptionsActivity.this,MainMenuActivity.class);
                            startActivity(logInWithFacebookIntent);
                            Toast.makeText(SignInOptionsActivity.this, "Welcome "+user.getEmail().split("@")[0],
                                    Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user./Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Log.e("signInWithCredential", task.getException().toString());
                            Toast.makeText(getApplicationContext(), "Firebase Facebook login failed",
                                    Toast.LENGTH_SHORT).show();

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Account with same Email already exists.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            LoginManager.getInstance().logOut();


                        }


                        // ...
                    }
                });


    }


    public void checkIfEmailExists(String mail)
    {
        mAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = !task.getResult().getSignInMethods().isEmpty();

                if(!check){
                    Toast.makeText(getApplicationContext(),"Email not found",Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Email found",Toast.LENGTH_LONG).show();
                }
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


    public void Get_hash_key() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.com.finki.mobilniproekt", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }


}
