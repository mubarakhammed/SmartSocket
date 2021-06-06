package com.example.smartsocket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.ViewHolder> {

    private List <OutletList> listitems;
    private Context context;
    public static final String TAG = "Control";
    ProgressDialog progressDialog;

    public OutletAdapter(List<OutletList> listitems, int list_item, Context context) {
        this.listitems = listitems;
        this.context = context;
        progressDialog = new ProgressDialog(context);
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

        private TextView ouletname, Frequency, Power, Current, Voltage;
        private String id, status;
        private Button Onn, Off, graph;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ouletname = (TextView) itemView.findViewById(R.id.outlet_name);
            Frequency = (TextView) itemView.findViewById(R.id.freq);
            Power = (TextView) itemView.findViewById(R.id.power);
            Current = (TextView) itemView.findViewById(R.id.current);
            Voltage = (TextView) itemView.findViewById(R.id.volt);

            Onn = (Button) itemView.findViewById(R.id.on_switch);
            Off = (Button) itemView.findViewById(R.id.off_switch);
            graph = (Button) itemView.findViewById(R.id.view_graph);


            Onn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchON();
                }
            });

            Off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchOff();
                }
            });
            graph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent graphIntent = new Intent(context, Graph.class);
                    context.startActivity(graphIntent);
                }
            });


        }
    }

    public void SwitchON(){
        AndroidNetworking.post("https://ebco.com.ng/smartscoket-working-api/device-off-on-update.php")
                .addBodyParameter("id", "1")
                .addBodyParameter("outlet_status", "1")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // animationView.setVisibility(View.GONE);
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
//                            int status = jObj.getInt("status");
//                            String msg = jObj.getString("msg");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        //animationView.setVisibility(View.GONE);

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



    public void SwitchOff(){
        AndroidNetworking.post("https://ebco.com.ng/smartscoket-working-api/device-off-on-update.php")
                .addBodyParameter("id", "1")
                .addBodyParameter("outlet_status", "2")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // animationView.setVisibility(View.GONE);
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
//                            int status = jObj.getInt("status");
//                            String msg = jObj.getString("msg");




                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        //animationView.setVisibility(View.GONE);

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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
