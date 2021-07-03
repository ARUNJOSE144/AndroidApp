package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int last_refresed = 0, totalAttemts = 0, successCount = 0, failedCount = 0, totalTime = 0;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<CoinTO> coinDetails;
    Double DollerInINR = 74.14;
    int refreshInSeconds = 100;
    DBHelper DB;
    List<CoinTO> monitoringCoins;
    Button goToCreateCoinButton, goToProps, refreshButton;
    boolean debugMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Handler handler = new Handler();

        //Created Thread for Calling the API in multiple times
        final Runnable r = new Runnable() {
            public void run() {
                getDataFromApi();
                handler.postDelayed(this, refreshInSeconds * 1000);
            }
        };


        final Runnable lastUpdatedTime = new Runnable() {
            public void run() {
                last_refresed++;
                totalTime++;

                TextView item = (TextView) findViewById(R.id.last_refreshed);
                item.setTypeface(null, Typeface.BOLD);
                item.setText("Last Updated in " + last_refresed + " Sec");

                TextView countInfo = (TextView) findViewById(R.id.countInfo);
                countInfo.setTypeface(null, Typeface.BOLD);
                countInfo.setText(totalTime + " : " + ((totalTime / refreshInSeconds) + 1) + " : " + totalAttemts + " : " + successCount + " : " + failedCount);

                handler.postDelayed(this, 1000);
            }
        };

        goToCreateCoinButton = (Button) findViewById(R.id.goToCreateCoin);
        goToCreateCoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateCoinPage();
            }
        });

        goToProps = (Button) findViewById(R.id.propsButton);
        goToProps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPropsPage();
            }
        });

        refreshButton = (Button) findViewById(R.id.refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                last_refresed = 0;
                totalAttemts = 0;
                successCount = 0;
                failedCount = 0;
                totalTime = 0;
                getDataFromApi();
            }
        });

        handler.postDelayed(lastUpdatedTime, 1000);
        handler.postDelayed(r, 1);


    }

    public void openCreateCoinPage() {
        Intent intent = new Intent(this, CreateCoin.class);
        startActivity(intent);
    }

    public void openPropsPage() {
        Intent intent = new Intent(this, PropertyActivity.class);
        startActivity(intent);
    }

    void getDataFromApi() {
        LoadProps();
        setMonitoringCoinDetails();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        totalAttemts++;
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=84a48d89-edce-4e92-8eb4-ea4cb906a37c",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            last_refresed = 0;
                            if (debugMode) {
                                Toast.makeText(getApplicationContext(), "Api Sucess", Toast.LENGTH_LONG).show();
                            }
                            JSONObject reader = new JSONObject(response.toString());
                            JSONArray data = reader.getJSONArray("data");
                            setResponseAfterApiCall(response.toString());
                            successCount++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (debugMode) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                        failedCount++;
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


            coinDetails = sortList(list);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void alert() {
        MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.beep);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);
        mp.start();
    }


    CoinTO isPresentInMonitoringList(int id) {
        CoinTO coinTO = null;
        for (CoinTO to : monitoringCoins) {
            if (to.getId() == id) {
                return to;
            }

        }
        return coinTO;
    }

    List<CoinTO> sortList(List<CoinTO> list) {
        List<CoinTO> sortedList = new ArrayList<>();
        boolean activeAlert = false;

        for (CoinTO coinTO : list) {
            CoinTO monitoringTo = isPresentInMonitoringList(coinTO.getId());
            if (monitoringTo != null) {
                coinTO.setMonitoringCoin(true);
                coinTO.setMaxPrice(monitoringTo.getMaxPrice());
                coinTO.setMinPrice(monitoringTo.getMinPrice());
                sortedList.add(0, coinTO);

                //check for alert
                Double price = Double.parseDouble(coinTO.getPrice());
                Double minPrice = Double.parseDouble(coinTO.getMinPrice());
                Double maxPrice = Double.parseDouble(coinTO.getMaxPrice());
                if (price <= minPrice || price >= maxPrice) {
                    activeAlert = true;
                }
            } else {
                coinTO.setMonitoringCoin(false);
                sortedList.add(coinTO);
            }
        }

        if (activeAlert) {
            alert();
        }
        return sortedList;
    }

    void setMonitoringCoinDetails() {
        monitoringCoins = new ArrayList<>();
        DB = new DBHelper(this);
        monitoringCoins = DB.getCoinData();

    }

    void LoadProps() {
        DB = new DBHelper(this);
        List<Property> properties = DB.getAllProperty();
        for (Property property : properties) {
            if (property.getName().equalsIgnoreCase("DollerInINR")) {
                DollerInINR = Double.parseDouble(property.getValue().toString());
            } else if (property.getName().equalsIgnoreCase("refreshInSeconds")) {
                refreshInSeconds = Integer.parseInt(property.getValue().toString());
            } else if (property.getName().equalsIgnoreCase("debugMode")) {
                if (property.getValue().equalsIgnoreCase("true"))
                    debugMode = true;
                else
                    debugMode = false;

            }
        }
    }
}


