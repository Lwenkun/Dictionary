package com.lwenkun.dictionary.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 15119 on 2016/1/1.
 */
public class AlbumDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final String CREATE_ALBUMS = "create table albums (" +
            "id integer primary key autoincrement," +
            "name text)";

    public AlbumDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALBUMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
