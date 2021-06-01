package com.omkar.conversionrates.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omkar.conversionrates.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateHolder>{

    public List<Map.Entry<String, Double>> rates;
    public Context context;
    private LayoutInflater inflater;
    public String baseName;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Map.Entry<String, Double> item);
    }
    public RateAdapter(List<Map.Entry<String, Double>> rates,Context context,String baseName,OnItemClickListener listener) {

        this.rates=rates;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
        this.baseName =baseName;
        this.listener =listener;
    }


    @NonNull
    @Override
    public RateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.rateadapter,parent,false);
        return new RateHolder(view); }

    @Override
    public void onBindViewHolder(@NonNull RateHolder holder,int position){

        String rateName = rates.get(position).getKey();
        String rateValue = rates.get(position).getValue().toString();
        holder.rateName.setText(rateName);
        holder.rateValue.setText(rateValue);

        if(rateName.equals(baseName)){
            holder.deleteBtn.setVisibility(View.GONE);
        }else{
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(rates.get(position));
            }
        });

    }

    @Override
    public int getItemCount(){
        return rates.size();
    }

    class RateHolder extends RecyclerView.ViewHolder {

        private TextView rateName;
        private TextView rateValue;
        private TextView deleteBtn;

        public RateHolder(View itemView)
        {
            super(itemView);
            rateName =itemView.findViewById(R.id.tvratename);
            rateValue =itemView.findViewById(R.id.tvratevalue);
            deleteBtn =itemView.findViewById(R.id.tvbtndelete);
        }

    }

}
