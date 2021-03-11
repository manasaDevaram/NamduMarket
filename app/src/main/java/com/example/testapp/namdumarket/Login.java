 package com.example.testapp.namdumarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

 public class Login extends AppCompatActivity {
     private EditText MobileNumber;
     private Button LoginButton,verifyLoginButton;
     private LinearLayout retrieveOTP,resendOTP;
     private EditText code1,code2,code3,code4,code5,code6;
     private String verificationId;
     private ProgressBar progressBar;  String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MobileNumber = (EditText)findViewById(R.id.loginInput);
        LoginButton = (Button)findViewById(R.id.loginBtn);
        verifyLoginButton = (Button)findViewById(R.id.verifyLoginBtn);
        retrieveOTP = (LinearLayout) findViewById(R.id.RetrieveOTP);
        resendOTP = (LinearLayout)findViewById(R.id.resendOTPView);
        progressBar = findViewById(R.id.progressBar);

        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);

        setupOTPinputs();
        verifyLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code1.getText().toString().trim().isEmpty() ||
                        code2.getText().toString().trim().isEmpty() ||
                        code3.getText().toString().trim().isEmpty() ||
                        code4.getText().toString().trim().isEmpty() ||
                        code5.getText().toString().trim().isEmpty() ||
                        code6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Login.this,"Please enter a valid code",Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = code1.getText().toString() +
                        code2.getText().toString() +
                        code3.getText().toString() +
                        code4.getText().toString() +
                        code5.getText().toString() +
                        code6.getText().toString();

                if (verificationId != null){
                    progressBar.setVisibility(View.VISIBLE);
                    verifyLoginButton.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            verifyLoginButton.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){
                      validatePhoneNumber(phone);
                                AllowAccesToAccount(phone);
                                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this, "The Verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        findViewById(R.id.resendOTP).setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                if(MobileNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Login.this,"Please enter Mobile Number",Toast.LENGTH_SHORT).show();
                    return;
                }

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        Login.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerification;
                                Toast.makeText(Login.this,"OTP Sent",Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });

    }
    private void loginOrSignUp(View v){
         phone = MobileNumber.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter your phone number",Toast.LENGTH_SHORT);
        }else{

            progressBar.setVisibility(View.VISIBLE);
            LoginButton.setVisibility(View.INVISIBLE);


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + MobileNumber.getText().toString(),
                    60,
                    TimeUnit.SECONDS,
                    Login.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.GONE);
                            LoginButton.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            LoginButton.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            progressBar.setVisibility(View.GONE);
                            LoginButton.setVisibility(View.GONE);
                            MobileNumber.setVisibility(View.GONE);
                            retrieveOTP.setVisibility(View.VISIBLE);
                            verifyLoginButton.setVisibility(View.VISIBLE);
                            resendOTP.setVisibility(View.VISIBLE);
                        }
                    }
            );



        }
    }
    private void validatePhoneNumber(String phone){
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener(){

        });
    }
    private void AllowAccesToAccount(String phone){
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();
    }


     private void setupOTPinputs() {
         code1.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!charSequence.toString().trim().isEmpty()){
                     code2.requestFocus();
                 }
             }


             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
         code2.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!charSequence.toString().trim().isEmpty()){
                     code3.requestFocus();
                 }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
         code3.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!charSequence.toString().trim().isEmpty()){
                     code4.requestFocus();
                 }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
         code4.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!charSequence.toString().trim().isEmpty()){
                     code5.requestFocus();
                 }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
         code5.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!charSequence.toString().trim().isEmpty()){
                     code6.requestFocus();
                 }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
         code6.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(!charSequence.toString().trim().isEmpty()){
                     // code6.requestFocus();
                 }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });

     }
 }