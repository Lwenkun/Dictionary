package com.lwenkun.dictionary.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;
import com.lwenkun.dictionary.ui.widget.CollectionReviewAdapter;
import com.lwenkun.dictionary.util.DisplayCollectionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2015/12/28.
 */
public class CollectionDisplayFragment extends Fragment implements UIUpdater{

    private ListView lv_collectionReview;

    private ProgressBar progressBar;

    private List<TranslateResultSet> translateResultSetList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_display, container, true);
        lv_collectionReview = (ListView) view.findViewById(R.id.lv_review);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_load_collections);
        lv_collectionReview.setAdapter(new CollectionReviewAdapter(getActivity(), R.layout.list_item_collection_review, translateResultSetList));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayCollectionTask task = new DisplayCollectionTask(this);
        task.execute();
    }

    @Override
    public void onShowProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDataUpdate(Object data, int type) {
        ((ArrayAdapter)lv_collectionReview.getAdapter()).notifyDataSetChanged();
    }
}
