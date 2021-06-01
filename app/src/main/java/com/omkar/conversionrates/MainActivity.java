package com.omkar.conversionrates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.omkar.conversionrates.adapter.RateAdapter;
import com.omkar.conversionrates.dataservice.RRretrofitclient;
import com.omkar.conversionrates.model.RequiredRate;
import com.omkar.conversionrates.network.RRnetwork;
import com.omkar.conversionrates.network.RRnwreceiver;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements RateAdapter.OnItemClickListener {

    @BindView(R.id.edtconvert)
    EditText edtconvert;

    @BindView(R.id.tvconvert)
    TextView tvconvert;

    @BindView(R.id.tvbtnRefresh)
    TextView tvbtnRefresh;

    @BindView(R.id.tvupdatedTime)
    TextView tvupdatedTime;

   @BindView(R.id.rvRates)
    RecyclerView rvRates;

   @BindView(R.id.tvbase)
   TextView tvBase;

    private RequiredRate requiredRateModel;
    private List<Map.Entry<String, Double>> listOfrates;
    private RateAdapter rateAdapter;

    public final static String screenName = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
            tvupdatedTime.setText(getString(R.string.lastupdatedtime)+" : "+preferences.getString("conversion_Date",sdf.format(new Date())));

            getDatafromServer();
        } catch (Exception e) {
            Log.d(screenName,e.getMessage());
        }
    }

    private void getDatafromServer() {
        try {
            Call<RequiredRate> resp = RRretrofitclient.getInstance().getMyApi().getDatafromServer();
            resp.enqueue(new Callback<RequiredRate>() {
                @Override
                public void onResponse(Call<RequiredRate> call, Response<RequiredRate> response) {

                    requiredRateModel = response.body();
                    listOfrates = response.body().getRates().entrySet()
                            .stream()
                            .collect(Collectors.toList());;

                    getConversionView();
                }

                @Override
                public void onFailure(Call<RequiredRate> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), RRnetwork.getConnectivityStatusString(getApplicationContext()),Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            Log.d(screenName,e.getMessage());
        }


    }

    private void getConversionView()
    {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
            preferences .edit().putString("conversion_Date", sdf.format(new Date())).apply();
            tvupdatedTime.setText(getString(R.string.lastupdatedtime)+" : "+preferences.getString("conversion_Date",sdf.format(new Date())));


            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rvRates.setLayoutManager(layoutManager);
            rateAdapter = new RateAdapter(listOfrates,getApplicationContext(),requiredRateModel.getBase(),this);
            rvRates.setAdapter(rateAdapter);
            tvBase.setText(requiredRateModel.getBase());
        } catch (Exception e) {
            Log.d(screenName,e.getMessage());
        }


    }
    @OnClick(R.id.tvconvert)
    public  void onConvert(){
        try {
            if(!edtconvert.getText().toString().equals("")) {
                 Double dvalue = Double.valueOf(edtconvert.getText().toString());

                for (int i = 0; i < listOfrates.size(); i++) {
                    Double newValue = listOfrates.get(i).getValue() * dvalue;
                    listOfrates.get(i).setValue(newValue);

                }
            }
            getConversionView();

        } catch (NumberFormatException e) {
            Log.d(screenName,e.getMessage());
        }


    }
    @OnClick(R.id.tvbtnRefresh)
    void onRefreshbtnClick()
    {
        getConversionView();
    }

    @Override
    public void onItemClick(Map.Entry<String, Double> item) {

        try {
            for (int i=0; i<listOfrates.size();i++)
            {
                if(item == listOfrates.get(i)) {
                    listOfrates.remove(i);
                }

            }
            getConversionView();
        } catch (Exception e) {
            Log.d(screenName,e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new RRnwreceiver(), filter);
        if(RRnetwork.getConnectivityStatus(getApplicationContext()) == 0)
        {
            Toast.makeText(getApplicationContext(), RRnetwork.getConnectivityStatusString(getApplicationContext()),Toast.LENGTH_LONG).show();

        }
    }
}
