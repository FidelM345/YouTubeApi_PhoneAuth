package com.example.thebeast.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity{

    private EditText codeText,phoneText;
    private Button verifyButton,phoneSubmit,resend_btn;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken resendToken;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneText = (EditText) findViewById(R.id.edit_phone);
        codeText = (EditText) findViewById(R.id.edit_code);
        progressBar=findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        phoneSubmit= findViewById(R.id.btn_phone);
        verifyButton =findViewById(R.id.btn_code);
        resend_btn =findViewById(R.id.btn_resend);


        mAuth=FirebaseAuth.getInstance();


       phoneSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String phone_no=phoneText.getText().toString();

               if(TextUtils.isEmpty(phone_no)){
                   phoneText.setError("Phone number required");
                   phoneText.requestFocus();

               }else{

                   progressBar.setVisibility(View.VISIBLE);
                   sendVerification(phone_no);

               }


           }
       });

       verifyButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String code_no=codeText.getText().toString();

               verifyCode(code_no);


           }
       });

       resend_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String phone_no=phoneText.getText().toString();
               resendCode(phone_no);

           }
       });


    }


    public void resendCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                resendToken);
    }


    private void sendVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            signInWithPhoneAuthCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
            resendToken=forceResendingToken;

        }
    };


    private void verifyCode(String code_no) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code_no);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure"+task.getException(),Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"The verification code entered was invalid",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }


}
