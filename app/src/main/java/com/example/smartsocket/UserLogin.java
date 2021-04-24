package com.example.smartsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.DownloadProgressListener;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLogin extends AppCompatActivity {

    private TextView registerLink;
    private Button Login;
    private EditText email_input, password_input;
    private  static final String TAG ="login";
    ProgressDialog progressDialog;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

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

        Preference();
    }



    private  void logUser(){
        showDialog();
        AndroidNetworking.post("https://mydealtrackerweb.staging.cloudware.ng/ussd/trove/api/v1/user_login.php")
                .addBodyParameter("email", email_input.getText().toString())
                .addBodyParameter("password", password_input.getText().toString())
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
                        bottomError();
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

    private void bottomError() {
        View view = getLayoutInflater().inflate(R.layout.bottomloginerror, null);
        BottomSheetDialog dialog = new BottomSheetDialog(UserLogin.this);
        dialog.setContentView(view);
        dialog.show();
    }



    private void Preference() {
        String usermail = mPreferences.getString(getString(R.string.stored_mail), "");
        email_input.setText(usermail);

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
