package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PropertyActivity extends AppCompatActivity {
    DBHelper DB;
    EditText DollerInINR, refreshInSeconds, debugMode;
    Button cancelButton, updateAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        getSetData();

        cancelButton = (Button) findViewById(R.id.cancelPropsButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opentoMainPage();
            }
        });

        updateAllButton = (Button) findViewById(R.id.updateAllPropsButton);
        updateAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    public void opentoMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void getSetData() {
        DB = new DBHelper(this);
        DollerInINR = (EditText) findViewById(R.id.DollerInINR);
        refreshInSeconds = (EditText) findViewById(R.id.refreshInSeconds);
        debugMode = (EditText) findViewById(R.id.debugMode);

        List<Property> properties = DB.getAllProperty();
        for (Property property : properties) {
            if (property.getName().equalsIgnoreCase("DollerInINR")) {
                DollerInINR.setText(property.getValue());
            } else if (property.getName().equalsIgnoreCase("refreshInSeconds")) {
                refreshInSeconds.setText(property.getValue());
            } else if (property.getName().equalsIgnoreCase("debugMode")) {
                debugMode.setText(property.getValue());
            }
        }

    }


    void saveData() {
        DB = new DBHelper(this);
        DollerInINR = (EditText) findViewById(R.id.DollerInINR);
        refreshInSeconds = (EditText) findViewById(R.id.refreshInSeconds);
        debugMode = (EditText) findViewById(R.id.debugMode);

        List<Property> properties = new ArrayList<>();

        Property dollerInINRProperty = new Property();
        dollerInINRProperty.setName("DollerInINR");
        dollerInINRProperty.setValue(DollerInINR.getText().toString());
        properties.add(dollerInINRProperty);

        Property refreshInSecondsProperty = new Property();
        refreshInSecondsProperty.setName("refreshInSeconds");
        refreshInSecondsProperty.setValue(refreshInSeconds.getText().toString());
        properties.add(refreshInSecondsProperty);

        Property debugModeProperty = new Property();
        debugModeProperty.setName("debugMode");
        debugModeProperty.setValue(debugMode.getText().toString());
        properties.add(debugModeProperty);
        DB.deleteAllProperty();
        boolean stat = DB.inserAllProps(properties);
        if (stat) {
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
        }
    }


}