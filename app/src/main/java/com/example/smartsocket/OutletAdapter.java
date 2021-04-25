package com.example.smartsocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.ViewHolder> {

    private List <OutletList> listitems;
    private Context context;
    public static  String npame;
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

       // holder.planname.setText(listHomeItem.getPlan_name());

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

        private TextView planname, plancategory, planbalance, planinterest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          //  planname = itemView.findViewById(R.id.user_plan_name);
        }
    }
}
