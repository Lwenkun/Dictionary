package com.lwenkun.dictionary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lwenkun.dictionary.model.db.DictDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2016/1/2.
 */
public class CollectionManager {

    private static Context mContext;

    private DictDbHelper dbHelper;

    private CollectionManager() {
    }

    public static CollectionManager getInstance(Context context) {
        mContext = context;
        return  CollectionManagerHolder.manager;
    }

    private static class CollectionManagerHolder {
        private static CollectionManager manager = new CollectionManager();
    }

    public SQLiteDatabase getDatabase() {

        SQLiteDatabase database = null;
        if (dbHelper == null) {
            dbHelper = DictDbHelper.getInstance(mContext, "dictionary", null, 1);
        }
        database = dbHelper.getWritableDatabase();
        return database;
    }

    public List<TranslateResultSet> getCollectionsFromAlbum(String album) {
        List<TranslateResultSet> resultSets = new ArrayList<>();
        Cursor cursor = getDatabase().query("collection", null, "album = ?", new String[]{album}, null, null, null);

        if (cursor != null && cursor.moveToFirst())  {
            do {
                String sResultSet = cursor.getString(cursor.getColumnIndex("resultset"));
                TranslateResultSet resultSet = new TranslateResultSet(sResultSet);
                resultSets.add(resultSet);
            }while(cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return resultSets;
    }

    public void saveToAlbum(TranslateResultSet resultSet, String album) {
        ContentValues values = new ContentValues();
        values.put("resultset", resultSet.getsJSON());
        values.put("album", album);
        getDatabase().insert("collection", null, values);
    }
}
