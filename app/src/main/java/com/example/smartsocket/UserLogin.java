package com.example.smartsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.DownloadProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity {

    private TextView registerLink;
    private Button Login;
    private EditText email_input, password_input;
    private  static final String TAG ="login";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        progressDialog= new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("logging in.... please wait");


        registerLink = (TextView) findViewById(R.id.registerLink);
        Login = (Button) findViewById(R.id.loginUser) ;
        email_input = (EditText) findViewById(R.id.user_email) ;
        password_input = (EditText) findViewById(R.id.user_password) ;
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email_input.getText().toString().isEmpty() || password_input.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Empty Fieldsnot allowed", Toast.LENGTH_LONG).show();
                }
                Intent register = new Intent(UserLogin.this, ControlPanel.class);
                startActivity(register);
            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(UserLogin.this, UserRegister.class);
                startActivity(register);
            }
        });
    }



    private  void logUser(){

    }
}