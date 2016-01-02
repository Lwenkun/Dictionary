package com.lwenkun.dictionary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.lwenkun.dictionary.model.db.AlbumDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by 15119 on 2016/1/1.
 */
public class AlbumManager {

    private static Context mContext;

    private AlbumDatabaseHelper databaseHelper;

    private ArrayList<String> albumList;

    private static Handler mHandler;

    public final static int MSG_ALBUM_NOT_EXIST = 0;
    public final static int MSG_ALBUM_ALREADY_EXIST = 1;
    public final static int MSG_CREATE_SUCCESSFUL = 2;

    private AlbumManager() {
        albumList = new ArrayList<>();
        loadAlbums();
    }

    public static AlbumManager getInstance(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        return AlbumManagerHolder.manager;
    }

    private static class AlbumManagerHolder {
         private static AlbumManager manager = new AlbumManager();
    }

    public void addNewAlbum(String albumName) {
        Message msg = new Message();
        if(albumList.contains(albumName)) {
            msg.what = MSG_ALBUM_ALREADY_EXIST;
        } else {
            ContentValues values = new ContentValues();
            values.put("name", albumName);
            getAlbumDatabase().insert("album", null, values);
            albumList.add(albumName);
            msg.what = MSG_CREATE_SUCCESSFUL;
        }
        mHandler.sendMessage(msg);
    }

    public void loadAlbums() {

        Cursor cursor = getAlbumDatabase().query("album", null, null, null, null, null, null);
        String name;
        if(cursor != null && cursor.moveToFirst()) {
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

    public SQLiteDatabase getAlbumDatabase() {

        SQLiteDatabase sqLiteDatabase;

        if(databaseHelper == null) {
            databaseHelper = new AlbumDatabaseHelper(mContext, "dictionary", null, 1);
        }
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return sqLiteDatabase;
    }

    public void deleteAlbum(String albumName) {
        if (albumList.contains(albumName)) {
            getAlbumDatabase().delete("album", "where name = ?", new String[]{albumName});
            albumList.remove(albumName);
        } else {
            Message msg = new Message();
            msg.what = MSG_ALBUM_NOT_EXIST;
            mHandler.sendMessage(msg);
        }
    }

}
