package com.lwenkun.dictionary.util;

import android.os.AsyncTask;

import com.lwenkun.dictionary.ui.Interface.UIUpdater;
import com.lwenkun.dictionary.model.TranslateResultSet;

/**
 * Created by 15119 on 2015/12/30.
 */
public class DisplayCollectionTask extends AsyncTask<Void,Void,TranslateResultSet> {

    private UIUpdater updater;

    public DisplayCollectionTask(UIUpdater updater) {
        this.updater = updater;
    }

    @Override
    protected void onPreExecute() {
        updater.onShowProgressBar();
    }

    @Override
    protected TranslateResultSet doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(TranslateResultSet resultSet) {
        updater.onHideProgressBar();
        updater.onDataUpdate(resultSet);
    }
}
