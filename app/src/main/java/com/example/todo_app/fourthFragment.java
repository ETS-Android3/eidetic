package com.example.todo_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.Executor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.todo_app.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class fourthFragment extends Fragment {

    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    Button signoutButton;
    Context thiscontext;
    View view;
    TextView textView3;

    public fourthFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fourth, container, false);
        thiscontext=view.getContext();
        TextView head=(TextView) view.findViewById(R.id.textView3);
//        head.setText("hello tirtha!");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(),gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        updateUI(account);

        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startactivityresult.launch(signInIntent);
            }
        });


//        sign out button setup

        signoutButton = view.findViewById(R.id.sign_out_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return view;
    }

    public void updateUI(GoogleSignInAccount account){
        SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        Button signoutButton=view.findViewById(R.id.sign_out_button);
        if(account!=null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            textView3=view.findViewById(R.id.textView3);
            textView3.setText("Hi,"+personName);
            textView3.setTextSize(20);
            signInButton.setVisibility(View.GONE);
            signoutButton.setVisibility(View.VISIBLE);


        }
        else{
            textView3=view.findViewById(R.id.textView3);
            textView3.setText("Profile");
            textView3.setTextSize(30);
            signInButton.setVisibility(View.VISIBLE);
            signoutButton.setVisibility(View.GONE);

        }
    }


    ActivityResultLauncher<Intent> startactivityresult=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                }
            }
    );

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(thiscontext, "Congratulations ! successfully signed in.", Toast.LENGTH_SHORT).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("GOOGLE ERROR",e.getMessage());
        }
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                        Toast.makeText(thiscontext, "See you later! successfully signed out.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
