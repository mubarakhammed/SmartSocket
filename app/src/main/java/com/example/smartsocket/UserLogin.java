package com.example.smartsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.mbms.DownloadProgressListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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

import java.util.Calendar;

public class UserLogin extends AppCompatActivity {

    private TextView registerLink, greetings;
    private Button Login;
    private EditText email_input, password_input;
    private  static final String TAG ="login";
    ProgressDialog progressDialog;
    private Dialog mdialog;
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


        greetings = (TextView) findViewById(R.id.greetings);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greetings.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greetings.setText("Wonderful Night");
        }else {
            Toast.makeText(this, "Good Day", Toast.LENGTH_SHORT).show();

        }



        registerLink = (TextView) findViewById(R.id.registerLink);
        registerLink.setPaintFlags(registerLink.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        Login = (Button) findViewById(R.id.loginUser) ;
        email_input = (EditText) findViewById(R.id.user_email) ;
        password_input = (EditText) findViewById(R.id.user_password) ;




        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    if (email_input.getText().toString().isEmpty() || password_input.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Empty Fields not allowed", Toast.LENGTH_LONG).show();
                    }else{
                        Intent register = new Intent(UserLogin.this, ControlPanel.class);
                        startActivity(register);
                    }

                } else {
                    Showdialog();
                }


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

    private  void Showdialog(){
        mdialog = new Dialog(UserLogin.this);
        mdialog.setContentView(R.layout.internet_error);
        mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = mdialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        mdialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        mdialog.show();
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
