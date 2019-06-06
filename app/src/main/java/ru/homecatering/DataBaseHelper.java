package ru.homecatering;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "homecatering.db";
    private static final int VERSION = 1;
    static final String TABLE_PRODUCTS = "products";
    static final String TABLE_CONTENT = "content";

    DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,parent INTEGER,price INTEGER,image BLOB)");
        db.execSQL("CREATE TABLE " + TABLE_CONTENT + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT)");
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"home"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"gallery"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"prepared"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"contacts"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"stew"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"hot"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"cold"});
        db.execSQL("INSERT INTO " + TABLE_CONTENT + " VALUES (null,?)", new Object[]{"grille"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("INFO", "DB upgrade called");
    }
}
