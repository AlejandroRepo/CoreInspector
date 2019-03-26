package com.example.daelin.foodinspector;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class BBDD extends SQLiteAssetHelper  {

    private static final String DBNAME = "alimentos.db";
    private static final int DATABASE_VERSION = 1;

    public BBDD(Context context1) {
        super(context1, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}