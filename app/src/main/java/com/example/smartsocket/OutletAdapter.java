package com.example.smartsocket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.ViewHolder> {

    private List <OutletList> listitems;
    private Context context;
    public static final String TAG = "Control";
    public static String id_new, outletname;


    public OutletAdapter(List<OutletList> listitems, int list_item, Context context) {
        this.listitems = listitems;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.oulet_list, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OutletList listHomeItem = listitems.get(position);

       holder.ouletname.setText(listHomeItem.getDevice());
        holder.Frequency.setText(listHomeItem.getFrequency());
        holder.Power.setText(listHomeItem.getPower());
        holder.Current.setText(listHomeItem.getCurrent());
        holder.Voltage.setText(listHomeItem.getVoltage());

        holder.status = listHomeItem.getStatus();
        holder.id = listHomeItem.getDevice_id();

        switch(holder.status){
            case "1":
                holder.outlet_status.setText("Active");
                holder.Onn.setVisibility(View.GONE);
                break;
            case "2":
                holder.outlet_status.setText("Off");
                holder.Off.setVisibility(View.GONE);
                break;
            default:
                holder.outlet_status.setText("Neutral");
                break;
        }



    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ouletname, Frequency, Power, Current, Voltage, outlet_status;
        ProgressBar progressBar;
        private String id, status;
        private Button Onn, Off, graph;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ouletname = (TextView) itemView.findViewById(R.id.outlet_name);
            Frequency = (TextView) itemView.findViewById(R.id.freq);
            Power = (TextView) itemView.findViewById(R.id.power);
            Current = (TextView) itemView.findViewById(R.id.current);
            Voltage = (TextView) itemView.findViewById(R.id.volt);
            outlet_status = (TextView) itemView.findViewById(R.id.outletstatus);
            progressBar = (ProgressBar) itemView.findViewById(R.id.switch_progress);


            Onn = (Button) itemView.findViewById(R.id.on_switch);
            Off = (Button) itemView.findViewById(R.id.off_switch);
            graph = (Button) itemView.findViewById(R.id.view_graph);


            Onn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    AndroidNetworking.post("https://ebco.com.ng/smartscoket-working-api/device-off-on-update.php?id="+"1"+"&outlet_status=1")
                            //   .addBodyParameter("id", "1")
                            //.addBodyParameter("outlet_status", "1")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: " + response);
                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        String status = jObj.getString("status");

                                        switch (status){
                                            case "200":
                                                progressBar.setVisibility(View.GONE);
                                                outlet_status.setText("Active");
                                                Onn.setVisibility(View.GONE);
                                                Off.setVisibility(View.VISIBLE);
                                                break;
                                            default:
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Fatal error encountered", Toast.LENGTH_LONG).show();
                                                break;


                                        }

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(ANError error) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "Fatal error encountered", Toast.LENGTH_LONG).show();
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
            });

            Off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    AndroidNetworking.post("https://ebco.com.ng/smartscoket-working-api/device-off-on-update.php?id="+"1"+"&outlet_status=2")
                            // .addBodyParameter("id", "1")
                            // .addBodyParameter("outlet_status", "2")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: " + response);
                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        String status = jObj.getString("status");

                                        switch (status){
                                            case "200":
                                                progressBar.setVisibility(View.GONE);
                                                outlet_status.setText("Off");
                                                Off.setVisibility(View.GONE);
                                                Onn.setVisibility(View.VISIBLE);
                                                break;
                                            default:
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Fatal error encountered", Toast.LENGTH_LONG).show();
                                                break;


                                        }





                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(ANError error) {
                                    progressBar.setVisibility(View.GONE);
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
            });
            graph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  id_new = id;
//                    Intent n_act = new Intent(context, Graph.class);
//                    n_act.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(n_act);

                }
            });


        }
    }
}
