package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCoin extends AppCompatActivity {

    Button cancel, add, update, delete;
    EditText id, name, max, min;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coin);

        add = (Button) findViewById(R.id.addButton);
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

    public CoinTO getRequest() {
        CoinTO to = new CoinTO();
        id = (EditText) findViewById(R.id.coinId);
        to.setId(Integer.parseInt(id.getText().toString()));

        name = (EditText) findViewById(R.id.coinName);
        to.setName(name.getText().toString());

        min = (EditText) findViewById(R.id.coinMinValue);
        to.setMinPrice(min.getText().toString());

        max = (EditText) findViewById(R.id.coinMaxValue);
        to.setMaxPrice(max.getText().toString());

        return to;
    }

    public void Create() {
        DB = new DBHelper(this);
        boolean stat = DB.inserCoinData(getRequest());
        if (stat) {
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
            Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Create Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void Update() {
        DB = new DBHelper(this);
        boolean stat = DB.updateCoinData(getRequest());
        if (stat) {
           /* Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void Delete() {
        DB = new DBHelper(this);
        boolean stat = DB.deleteCoinData(getRequest());
        if (stat) {
          /*  Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_LONG).show();
        }
    }
}
