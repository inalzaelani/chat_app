package com.example.appchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        initViews();

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    handleSendOtp();
                    return true;
                }
                return false;
            }
        });

        sendOtpBtn.setOnClickListener((v) -> handleSendOtp());
    }

    private void initViews(){
        countryCodePicker = findViewById(R.id.country_code);
        phoneInput = findViewById(R.id.login_mobile_number_et);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);
    }

    private void handleSendOtp() {
        if (!countryCodePicker.isValidFullNumber()) {
            phoneInput.setError("Phone number not valid");
            return;
        }
        Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginOTPActivity.class);
        intent.putExtra("phone", countryCodePicker.getFullNumber());
        startActivity(intent);
    }
}
