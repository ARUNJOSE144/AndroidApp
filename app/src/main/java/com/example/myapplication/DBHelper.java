package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Coin.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table CoinDetails(id TEXT primary key,name TEXT,min TEXT,max TEXT)");
        DB.execSQL("create Table PropertyDetails(name TEXT primary key,value TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists CoinDetails");
        DB.execSQL("drop table if exists PropertyDetails");
    }

    public boolean inserCoinData(CoinTO coinTO) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", coinTO.getId());
        contentValues.put("name", coinTO.getName());
        contentValues.put("min", coinTO.getMinPrice());
        contentValues.put("max", coinTO.getMaxPrice());

        long result = DB.insert("CoinDetails", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean updateCoinData(CoinTO coinTO) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", coinTO.getName());
        contentValues.put("min", coinTO.getMinPrice());
        contentValues.put("max", coinTO.getMaxPrice());

        Cursor cursor = DB.rawQuery("Select *  from CoinDetails where id=?", new String[]{coinTO.getId() + ""});

        if (cursor.getCount() > 0) {
            long result = DB.update("CoinDetails", contentValues, "id=?", new String[]{coinTO.getId() + ""});
            if (result == -1)
                return false;
            else
                return true;
        }
        return false;
    }

    public boolean deleteCoinData(CoinTO coinTO) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select *  from CoinDetails where id=?", new String[]{coinTO.getId() + ""});

        if (cursor.getCount() > 0) {
            long result = DB.delete("CoinDetails", "id=?", new String[]{coinTO.getId() + ""});
            if (result == -1)
                return false;
            else
                return true;
        }
        return false;
    }


    public List<CoinTO> getCoinData() {
        List<CoinTO> list = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select *  from CoinDetails", null);
        while (cursor.moveToNext()) {
            CoinTO coin = new CoinTO();
            coin.setId(Integer.parseInt(cursor.getString(0)));
            coin.setName(cursor.getString(1));
            coin.setMinPrice(cursor.getString(2));
            coin.setMaxPrice(cursor.getString(3));
            list.add(coin);
        }

        return list;
    }


    public boolean inserAllProps(List<Property> list) {
        SQLiteDatabase DB = this.getWritableDatabase();
        boolean stat = false;
        for (Property property : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", property.getName());
            contentValues.put("value", property.getValue());
            long result = DB.insert("PropertyDetails", null, contentValues);
        }
        stat = true;
        return stat;
    }

    public boolean deleteAllProperty() {
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("PropertyDetails", null, null);
        if (result == -1)
            return false;
        else
            return true;
    }

    public List<Property> getAllProperty() {
        List<Property> list = new ArrayList<>();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select *  from PropertyDetails", null);
        while (cursor.moveToNext()) {
            Property property = new Property();
            property.setName(cursor.getString(0));
            property.setValue(cursor.getString(1));
            list.add(property);
        }
        return list;
    }


}
