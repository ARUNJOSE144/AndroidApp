package com.example.myapplication;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int last_refresed = 0;
    List<String> coinList;
    List<String> childList;
    Map<String, List<String>> coinCollections;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<CoinTO> coinDetails;
    Double DollerInINR = 74.14;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Handler handler = new Handler();

        mp = MediaPlayer.create(this, R.raw.beep);

        //Created Thread for Calling the API in multiple times
        final Runnable r = new Runnable() {
            public void run() {
                getDataFromApi();
                last_refresed = 0;
                handler.postDelayed(this, 300000);
            }
        };

        final Runnable lastUpdatedTime = new Runnable() {
            public void run() {
                last_refresed++;
                TextView item = (TextView) findViewById(R.id.last_refreshed);
                item.setTypeface(null, Typeface.BOLD);
                item.setText("Last Updated in " + last_refresed + " Sec");
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(lastUpdatedTime, 1000);
        handler.postDelayed(r, 1);
    }

    private void loadChild(CoinTO coinTO) {
        childList = new ArrayList<>();
        childList.add(coinTO.getName());
        childList.add(coinTO.getPrice() + "");
        childList.add(coinTO.getId() + "");
        childList.add(coinTO.getCmc_rank() + "");
        childList.add(coinTO.getLast_updated() + "");
        childList.add(coinTO.getSymbol() + "");

    }

    private void createCollections() {
        coinCollections = new HashMap<>();
        for (int i = 0; i < coinDetails.size(); i++) {
            CoinTO coinTO = coinDetails.get(i);
            loadChild(coinTO);
            coinCollections.put(coinTO.getName(), childList);
        }
    }

    void loadCoin() {
        coinList = new ArrayList<>();
        for (int i = 0; i < coinDetails.size(); i++) {
            coinList.add(coinDetails.get(i).getName());
        }
    }


    void getDataFromApi() {
        mp.start();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=84a48d89-edce-4e92-8eb4-ea4cb906a37c",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(), "Api Sucess", Toast.LENGTH_LONG).show();
                            System.out.println("Succwssssssssssssssssssssssssss : " + response.toString());
                            JSONObject reader = new JSONObject(response.toString());
                            JSONArray data = reader.getJSONArray("data");
                            setResponseAfterApiCall(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Api Failed", Toast.LENGTH_SHORT).show();
                        System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrr : " + error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }


    void setResponseAfterApiCall(String response) {
        JSONObject reader = null;
        List<CoinTO> list = null;
        try {
            reader = new JSONObject(response.toString());
            JSONArray data = reader.getJSONArray("data");
            list = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                CoinTO coinTO = new CoinTO();
                coinTO.setId(Integer.parseInt(object.get("id").toString()));
                coinTO.setName(object.get("name").toString());
                coinTO.setSymbol(object.get("symbol").toString());
                coinTO.setSlug(object.get("slug").toString());
                coinTO.setCmc_rank(Integer.parseInt(object.get("cmc_rank").toString()));
                coinTO.setDate_added(object.get("date_added").toString());
                coinTO.setLast_updated(object.get("last_updated").toString());
                JSONObject quote = object.getJSONObject("quote");
                JSONObject usd = quote.getJSONObject("USD");

                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);

                Double finalPrice = Double.parseDouble(usd.get("price").toString()) * DollerInINR;
                coinTO.setPrice(df.format(finalPrice));

                list.add(coinTO);
            }
            coinDetails = list;
            loadCoin();
            createCollections();
            expandableListView = findViewById(R.id.expanded_menu);
            expandableListAdapter = new MyExpandableListAdapter(this, coinDetails);
            expandableListView.setAdapter(expandableListAdapter);
            /*expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int lastExpandedPosition = -1;

                @Override
                public void onGroupExpand(int i) {
                    if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                        expandableListView.collapseGroup(lastExpandedPosition);
                    }
                    lastExpandedPosition = i;
                }
            });*/

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    String selected = expandableListAdapter.getChild(i, i1).toString();
                    Toast.makeText(getApplicationContext(), "Selected : " + selected, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


            System.out.println("Coin Info ==============================================:" + coinDetails.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}