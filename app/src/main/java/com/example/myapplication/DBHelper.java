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

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists CoinDetails");
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

}
