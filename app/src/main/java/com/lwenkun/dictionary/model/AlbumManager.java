package com.lwenkun.dictionary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lwenkun.dictionary.model.db.DictDbHelper;

import java.util.ArrayList;

/**
 * Created by 15119 on 2016/1/1.
 */
public class AlbumManager {

    private static Context mContext;

    private DictDbHelper databaseHelper;

    private ArrayList<String> albumList;

    public final static int MSG_ALBUM_NOT_EXIST = 0;
    public final static int MSG_ALBUM_ALREADY_EXIST = 1;
    public final static int MSG_CREATE_SUCCESSFUL = 2;
    public final static int MSG_EMPTY_NOT_ALLOWED = 3;

    private AlbumManager() {
        albumList = new ArrayList<>();
        loadAlbums();
    }

    public static AlbumManager getInstance(Context context) {
        mContext = context;
        return AlbumManagerHolder.manager;
    }

    private static class AlbumManagerHolder {
         private static AlbumManager manager = new AlbumManager();
    }

    public int addNewAlbum(String albumName) {

        int result;
        if("".equals(albumName)) {
            result = MSG_EMPTY_NOT_ALLOWED;
        } else if(albumList.contains(albumName)) {
            result = MSG_ALBUM_ALREADY_EXIST;
        } else {
            ContentValues values = new ContentValues();
            values.put("name", albumName);
            getDatabase().insert("album", null, values);
            albumList.add(albumName);
            result = MSG_CREATE_SUCCESSFUL;
        }

        return result;
    }

    public void loadAlbums() {

        Cursor cursor = getDatabase().query("album", null, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            String name;
            do {
                if ((name = cursor.getString(cursor.getColumnIndex("name"))) != null) {
                    albumList.add(name);
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public SQLiteDatabase getDatabase() {

        SQLiteDatabase sqLiteDatabase;

        if(databaseHelper == null) {
            databaseHelper = DictDbHelper.getInstance(mContext, "dictionary", null, 1);
        }
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return sqLiteDatabase;
    }

    public boolean deleteAlbum(String albumName) {
        if (albumList.contains(albumName)) {
            getDatabase().delete("album", "where name = ?", new String[]{albumName});
            albumList.remove(albumName);
            return true;
        } else {
           return false;
        }
    }

    public ArrayList<String> getAlbums() {
        return albumList;
    }

}
