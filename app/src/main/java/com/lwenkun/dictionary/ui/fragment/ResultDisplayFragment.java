package com.lwenkun.dictionary.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;
import com.lwenkun.dictionary.ui.activity.MainActivity;

/**
 * Created by 15119 on 2015/12/30.
 */
public class ResultDisplayFragment extends Fragment {

    private TextView tv_query;
    private TextView tv_usPhonetic;
    private TextView tv_ukPhonetic;
    private TextView tv_translation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_display, container, false);
        initView(view);
        Log.d("dd", container.toString());
        return view;
    }

    public void initView(View view) {
        tv_query = (TextView) view.findViewById(R.id.tv_query);
        tv_usPhonetic = (TextView) view.findViewById(R.id.tv_us_phonetic);
        tv_ukPhonetic = (TextView) view.findViewById(R.id.tv_uk_phonetic);
        tv_translation = (TextView) view.findViewById(R.id.tv_translation);
        ((UIUpdater)getActivity()).onShowProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        TranslateResultSet resultSet = (TranslateResultSet) getArguments().getSerializable(MainActivity.SER_KEY);
        if(resultSet != null) {
            tv_query.setText(resultSet.getQuery());
            tv_translation.setText(resultSet.getTranslation());
            tv_usPhonetic.setText(resultSet.getusPhonetic());
            tv_ukPhonetic.setText(resultSet.getukPhonetic());
        }
        ((UIUpdater)getActivity()).onHideProgressBar();
    }
}
