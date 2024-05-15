package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginOTPActivity extends AppCompatActivity {
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otpactivity);

        phoneNumber = getIntent().getExtras().getString("phone");
        Toast.makeText(this, phoneNumber, Toast.LENGTH_LONG).show();

        Map<String,String> data = new HashMap<>();
        FirebaseFirestore.getInstance().collection("test").add(data);
    }
}