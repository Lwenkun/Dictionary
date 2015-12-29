package com.lwenkun.dictionary.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.widget.CollectionReviewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15119 on 2015/12/28.
 */
public class CollectionDisplayFragment extends Fragment {

    private List<TranslateResultSet> translateResultSetList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_display, container, true);
        ListView collectionReview = (ListView) view.findViewById(R.id.lv_review);
        collectionReview.setAdapter(new CollectionReviewAdapter(getActivity(), R.layout.list_item_collection_review, translateResultSetList));
        return view;
    }

}
