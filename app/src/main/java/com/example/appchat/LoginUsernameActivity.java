package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appchat.models.User;
import com.example.appchat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsernameActivity extends AppCompatActivity {

    EditText usernameEt;
    Button letMeInBtn;
    ProgressBar progressBar;
    String phoneNumber;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        initViews();

        phoneNumber = getIntent().getExtras().getString("phone");

        getUsername();

        letMeInBtn.setOnClickListener((v)->{
            setUsername();
        });
    }

    void setUsername(){

        String username = usernameEt.getText().toString();

        if(username.isEmpty() || username.length()<=3){
            usernameEt.setError("Username should be at least 3 characters");
            return;
        }

        setInProgress(true);
        if(user != null){
            user.setUserName(username);
        }else {
            user = new User(phoneNumber,username, Timestamp.now());
        }

        FirebaseUtil.currentUserDetails().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginUsernameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });


    }

    private void getUsername() {
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                 user =   task.getResult().toObject(User.class);
                 if(user != null){
                     usernameEt.setText(user.getUserName());
                 }
                }
            }
        });
    }

    void initViews() {
        usernameEt = findViewById(R.id.username_et);
        letMeInBtn = findViewById(R.id.let_me_in_btn);
        progressBar = findViewById(R.id.let_me_in_progress_bar);
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }
    }
}