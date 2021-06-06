package com.example.smartsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegister extends AppCompatActivity {

    private EditText  email, password, username, address, confirm_password;
    private Button register;
    private TextView loginLink;
    ProgressDialog progressDialog;
    private  static final String TAG ="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering... pleae wait");


        email = (EditText) findViewById(R.id.registeremail);
        password = (EditText) findViewById(R.id.registerPassword);
        confirm_password = (EditText) findViewById(R.id.registerConfirmPassword);
        username = (EditText) findViewById(R.id.registerUsername);
        address = (EditText) findViewById(R.id.registerAddress);
        register = (Button) findViewById(R.id.registerUser);
        loginLink = (TextView) findViewById(R.id.loginLink);


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(UserRegister.this, UserLogin.class);
                startActivity(login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty()|| password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Empty Fields are not allowed", Toast.LENGTH_LONG).show();
                }else if(!password.getText().toString().equals(confirm_password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Passwords are not matching", Toast.LENGTH_LONG).show();

                }else{
                   registerUser();
                }

            }
        });




    }

    private void registerUser(){
        showDialog();
        AndroidNetworking.post("https://ebco.com.ng/smartscoket-working-api/authetication-create.php")
                .addBodyParameter("username", email.getText().toString())
                .addBodyParameter("email", password.getText().toString())
                .addBodyParameter("password", confirm_password.getText().toString())
                .addBodyParameter("address", username.getText().toString())
                .addBodyParameter("full_name", address.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // do anything with response
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int status = jObj.getInt("status");
                            String msg = jObj.getString("msg");

                            switch (status) {

                                case 200:
                                    //success
                                    hideDialog();
                                    Intent intent = new Intent(getApplicationContext(), ControlPanel.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    break;


                                default:
                                    hideDialog();

                                    Log.d(TAG, "onResponse: " + "an error occurred");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        hideDialog();

                        if (error.getErrorCode() != 0) {

                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                        } else {

                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }


                });


    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}