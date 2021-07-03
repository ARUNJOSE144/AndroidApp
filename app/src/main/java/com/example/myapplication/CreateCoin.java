package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateCoin extends AppCompatActivity {

    Button cancel, add, update, delete;
    EditText id, name, max, min;
    DBHelper DB;
    List<String> dropDownList = new ArrayList<>();
    CoinTO selectedCoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coin);

        setDropDownList();
        Spinner spinner = (Spinner) findViewById(R.id.coinSelect);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropDownList);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCoin = MainActivity.coinDetails.get(i);
                TextView infoView = (TextView) findViewById(R.id.infoView);
                infoView.setText(selectedCoin.toString());

                add.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

                if (selectedCoin.isMonitoringCoin()) {
                    update.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    add.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        add = (Button) findViewById(R.id.addButton);
        add.setVisibility(View.INVISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create();
            }
        });


        cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opentoMainPage();
            }
        });


        update = (Button) findViewById(R.id.updateButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });

        delete = (Button) findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete();
            }
        });
    }

    public void opentoMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setDropDownList() {
        dropDownList = new ArrayList<>();
        for (CoinTO coin : MainActivity.coinDetails) {
            dropDownList.add(coin.getName());
        }

    }

    public CoinTO getRequest() {
        CoinTO to = new CoinTO();

        to.setId(selectedCoin.getId());
        to.setName(selectedCoin.getName());

        min = (EditText) findViewById(R.id.coinMinValue);
        to.setMinPrice(min.getText().toString());

        max = (EditText) findViewById(R.id.coinMaxValue);
        to.setMaxPrice(max.getText().toString());

        return to;
    }

    public void Create() {
        DB = new DBHelper(this);

        min = (EditText) findViewById(R.id.coinMinValue);
        max = (EditText) findViewById(R.id.coinMaxValue);

        try {
            if (validate(min.getText().toString()) || validate(max.getText().toString())) {
                if (validate(min.getText().toString())) {
                    Double minVal = Double.parseDouble(min.getText().toString());
                }
                if (validate(max.getText().toString())) {
                    Double maxVal = Double.parseDouble(max.getText().toString());
                }
                boolean stat = DB.inserCoinData(getRequest());
                if (stat) {
                    Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Create Failed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter Atleast Min or Max", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please check Min And Max Values", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    public void Update() {
        min = (EditText) findViewById(R.id.coinMinValue);
        max = (EditText) findViewById(R.id.coinMaxValue);

        try {
            if (validate(min.getText().toString()) || validate(max.getText().toString())) {
                if (validate(min.getText().toString())) {
                    Double minVal = Double.parseDouble(min.getText().toString());
                }
                if (validate(max.getText().toString())) {
                    Double maxVal = Double.parseDouble(max.getText().toString());
                }
                DB = new DBHelper(this);
                boolean stat = DB.updateCoinData(getRequest());
                if (stat) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter Atleast Min or Max", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please check Min And Max Values", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void Delete() {
        DB = new DBHelper(this);
        boolean stat = DB.deleteCoinData(getRequest());
        if (stat) {
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_LONG).show();
        }
    }

    boolean validate(String val) {
        if (val != null && !val.equalsIgnoreCase("")) {
            return true;
        } else
            return false;
    }
}
