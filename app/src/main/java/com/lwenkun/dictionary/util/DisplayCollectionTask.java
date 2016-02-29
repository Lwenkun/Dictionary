package com.lwenkun.dictionary.util;

import android.app.Fragment;
import android.os.AsyncTask;

import com.lwenkun.dictionary.model.AlbumManager;
import com.lwenkun.dictionary.model.CollectionManager;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 15119 on 2015/12/30.
 */
public class DisplayCollectionTask extends AsyncTask<Void,Void,List<TranslateResultSet>> {

    private Fragment fragment;

    private UIUpdater updater;

    private List<TranslateResultSet> mResultSets;

    public DisplayCollectionTask(Fragment fragment, List<TranslateResultSet> resultSets) {
        this.fragment = fragment;
        this.updater = (UIUpdater) fragment;
        this.mResultSets = resultSets;
    }

    @Override
    protected void onPreExecute() {
        updater.onShowProgressBar();
    }

    @Override
    protected List<TranslateResultSet> doInBackground(Void... params) {
        AlbumManager albumManager = AlbumManager.getInstance(fragment.getActivity());
        ArrayList<String> albumList = albumManager.getAlbums();
        return getTranslateResultSets(albumList);

    }

    @Override
    protected void onPostExecute(List<TranslateResultSet> resultSets) {
        updater.onHideProgressBar();
        updater.onDataUpdate(resultSets);
    }

    public List<TranslateResultSet> getTranslateResultSets(ArrayList<String> albumList) {

        CollectionManager collectionManager = CollectionManager.getInstance(fragment.getActivity());
        for(String album : albumList) {
            List<TranslateResultSet> resultSets = collectionManager.getCollectionsFromAlbum(album);
            int range = resultSets.size();
            if(range > 0) {
                mResultSets.add(resultSets.get(rand(range)));
            }
        }
        return mResultSets;
    }

    public int rand(int range) {
        Random random = new Random();

        if (range != 0) {
            return Math.abs(random.nextInt() % range);
        }

        return 0;
    }
}
