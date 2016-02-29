package com.lwenkun.dictionary.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 15119 on 2016/1/1.
 */
public class DictDbHelper {

    private static Context mContext;
    private static String mName;
    private static SQLiteDatabase.CursorFactory mFactory;
    private static int mVersion;

    public static SQLiteOpenHelper dictDbHelper;

    private static final String CREATE_ALBUMS = "create table album (" +
            "id integer primary key autoincrement," +
            "name text)";
    private static final String CREATE_COLLECTION = "create table collection (" +
            "id integer primary key autoincrement," +
            "album text," +
            "resultset text)";

    private DictDbHelper() {
        dictDbHelper = new SQLiteOpenHelper(mContext, mName,mFactory, mVersion) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_ALBUMS);
                db.execSQL(CREATE_COLLECTION);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
    }

    private static class DictDbHelperHolder {
        private static DictDbHelper dictDbHelper = new DictDbHelper();
    }

    public static DictDbHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        mContext = context;
        mName = name;
        mFactory = factory;
        mVersion = version;
        return DictDbHelperHolder.dictDbHelper;
    }

    public SQLiteDatabase getWritableDatabase() {
        return dictDbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return dictDbHelper.getReadableDatabase();
    }
}
