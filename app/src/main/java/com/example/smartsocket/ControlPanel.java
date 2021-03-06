package com.example.smartsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LottieAnimationView lottieAnimationView;
    private static final String TAG = "Outlet";
    RecyclerView.Adapter[] adapter = new RecyclerView.Adapter[1];
    List<OutletList> listitems;

    public String id = UserLogin.id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        recyclerView= (RecyclerView) findViewById(R.id.oulet_recycler);
        lottieAnimationView= (LottieAnimationView) findViewById(R.id.just_loading1);

        recyclerView.setHasFixedSize(false);
        listitems = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter[0] = new OutletAdapter(listitems, R.layout.oulet_list, this);
        recyclerView.setAdapter(adapter[0]);


        getInfo();
    }

    private void getInfo() {
         lottieAnimationView .setVisibility(View.VISIBLE);
        AndroidNetworking.post("https://ebco.com.ng/smartscoket-working-api/display-outlet-readings.php?id="+id)
             // .addBodyParameter("id", "3")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                         lottieAnimationView.setVisibility(View.GONE);
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
//
                            JSONArray array = jObj.getJSONArray("outlet");
                            Log.d(TAG, "onResponse: " + array);

                            if (array.length()<1){
                                Log.d(TAG, "onResponse: " + "I am empty");
                              //  empty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                OutletList item = new OutletList(
                                        o.getString("outlet_id"),
                                        o.getString("outlet_device"),
                                        o.getString("outlet_status"),
                                        o.getString("frequency_rating"),
                                        o.getString("power_rating"),
                                        o.getString("currenct_rating"),
                                        o.getString("voltage_rating"),
                                        o.getString("date_time")


                                );
                                listitems.add(item);
                            }

                            adapter[0] = new OutletAdapter(listitems, R.layout.oulet_list, getApplicationContext());
                            recyclerView.setAdapter(adapter[0]);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        lottieAnimationView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        Log.d(TAG, "onError:  " + error);
                        if (error.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            // get parsed error object (If ApiError is your class)

                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }
}