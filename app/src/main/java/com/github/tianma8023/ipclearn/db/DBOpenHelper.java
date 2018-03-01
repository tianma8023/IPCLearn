package com.github.tianma8023.ipclearn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tianma on 2018/2/28.
 */

public class DBOpenHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "ipclearn.db";
    public static final String TABLE_NAME_BOOK = "book";
    public static final String TABLE_NAME_USER = "user";

    private static final int DB_VERSION = 1;

    private String CREATE_TABLE_BOOK = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BOOK + "(_id INTEGER PRIMARY KEY, name TEXT)";
    private String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + "(_id INTEGER PRIMARY KEY, name TEXT)";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }
}
