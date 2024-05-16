package com.example.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appchat.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity {
    String phoneNumber;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    Long timeOut=60L;
    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOTPtv;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otpactivity);

        phoneNumber = getIntent().getExtras().getString("phone");
        phoneNumber = "+"+phoneNumber;


        initViews();
        sendOTP(phoneNumber,false);


        nextBtn.setOnClickListener(v->{
            String enteredOTP = otpInput.getText().toString();

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode,enteredOTP);
            signIn(phoneAuthCredential);

        });

        resendOTPtv.setOnClickListener((v)->{
            sendOTP(phoneNumber,true);
        });
    }

    void initViews() {
        otpInput = findViewById(R.id.login_otp_et);
        nextBtn = findViewById(R.id.next_otp_btn);
        progressBar = findViewById(R.id.enter_otp_progress_bar);
        resendOTPtv = findViewById(R.id.resend_otp_tv);
    }

    void sendOTP(String phoneNumber,boolean isResend){
        startRecentTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeOut, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(),"OTP Verification Failed");
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(),"OTP Send Successfully");
                        setInProgress(false);
                    }
                });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginOTPActivity.this,LoginUsernameActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                }else{
                    AndroidUtil.showToast(getApplicationContext(),"OTP Verification Failed");
                }
            }
        });
    }

    private void startRecentTimer() {
        resendOTPtv.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOut--;
                resendOTPtv.setText("Resend OTP in "+timeOut+" seconds");
                if(timeOut<=0){
                    timeOut = 60L;
                    timer.cancel();
                    runOnUiThread(()->{
                        resendOTPtv.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }
}