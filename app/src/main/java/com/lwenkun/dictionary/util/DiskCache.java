package com.lwenkun.dictionary.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by 15119 on 2016/1/2.
 */
public class DiskCache {

    private static Context mContext;

    private static File root;

    private DiskCache() {
        root = getRootDir();
    }

    public static DiskCache openDiskCache(Context context) {
        mContext = context;
        return DiskCacheHolder.diskCache;
    }

    private static class DiskCacheHolder {
        private static DiskCache diskCache = new DiskCache();
    }

    public void save(byte[] bytes, String mCacheDirName, String fileName) {

        File cacheFile = new File(new File(root, mCacheDirName), fileName);

        if(! cacheFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(cacheFile);
                fos.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File getRootDir() {

        File root;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            root = mContext.getExternalCacheDir();
        } else {
            root = mContext.getCacheDir();
        }
        return root;
    }

    public void delete(String mCacheDirName, String fileName) {

        File fileToDelete = new File(new File(root, mCacheDirName), fileName);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }
}
