package com.lwenkun.dictionary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.lwenkun.dictionary.model.db.AlbumDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by 15119 on 2016/1/1.
 */
public class AlbumManager {

    private static Context mContext;

    private AlbumDatabaseHelper databaseHelper;

    private ArrayList<String> albumList;

    private AlbumManager() {
        albumList = new ArrayList<>();
        loadAlbums();
    }

    public static AlbumManager from(Context context) {
        mContext = context;
        return AlbumManagerHolder.manager;
    }

    private static class AlbumManagerHolder {
         private static AlbumManager manager = new AlbumManager();
    }

    public void addNewAlbum(String albumName) {
        if(albumName.contains(albumName)) {
            Toast.makeText(mContext, "该单词本已存在，换个名字呗", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("name", "albumName");
            getAlbumDatabase().insert("Album", null, values);
            albumList.add(albumName);
        }
    }

    public void loadAlbums() {

        Cursor cursor = getAlbumDatabase().query("name", null, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            albumList.add(cursor.getString(cursor.getColumnIndex("name")));
        } while (cursor.moveToNext());
        cursor.close();
    }

    public SQLiteDatabase getAlbumDatabase() {

        SQLiteDatabase sqLiteDatabase = null;

        if(databaseHelper == null) {
            databaseHelper = new AlbumDatabaseHelper(mContext, "Album", null, 1);
        } else {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    public void deleteAlbum(String albumName) {
        if (albumList.contains(albumName)) {
            getAlbumDatabase().delete("Album", "where name = ?", new String[]{albumName});
            albumList.remove(albumName);
        } else {
            Toast.makeText(mContext, "出错啦，没有该单词本哦", Toast.LENGTH_SHORT);
        }
    }

}
