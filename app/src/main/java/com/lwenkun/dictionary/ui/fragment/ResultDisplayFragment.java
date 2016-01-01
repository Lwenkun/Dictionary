package com.lwenkun.dictionary.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;
import com.lwenkun.dictionary.ui.activity.MainActivity;
import com.lwenkun.dictionary.util.NetworkSearchTask;

/**
 * Created by 15119 on 2015/12/30.
 */
public class ResultDisplayFragment extends Fragment {

    private TextView tv_query;
    private TextView tv_usPhonetic;
    private TextView tv_ukPhonetic;
    private TextView tv_translation;
    private ListView lv_explains;
    private ImageView iv_star;
    private State state = State.cancel;
    private enum State {
        select,cancel
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_display, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        tv_query = (TextView) view.findViewById(R.id.tv_query);
        tv_usPhonetic = (TextView) view.findViewById(R.id.tv_us_phonetic);
        tv_ukPhonetic = (TextView) view.findViewById(R.id.tv_uk_phonetic);
        tv_translation = (TextView) view.findViewById(R.id.tv_translation);
        lv_explains = (ListView) view.findViewById(R.id.lv_explains);
        iv_star = (ImageView) view.findViewById(R.id.iv_star);
        iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCollectionState();
            }
        });
        TextView tv_more = (TextView) view.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        ((UIUpdater) getActivity()).onShowProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        TranslateResultSet resultSet = (TranslateResultSet) getArguments().getSerializable(MainActivity.KEY_SER);
        View phonetics = getView().findViewById(R.id.phonetics);

        if (resultSet != null) {

            tv_query.setText(resultSet.getQuery());
            tv_translation.setText(resultSet.getTranslation());

            switch (getArguments().getInt(MainActivity.KEY_TYPE)) {

                case NetworkSearchTask.TYPE_EXCEPT_PHONETIC:
                    lv_explains.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_explains, resultSet.getExplains()));

                case NetworkSearchTask.TYPE_EXCEPT_PHONETIC_AND_EXPLAINS:
                    phonetics.setVisibility(View.INVISIBLE);
                    break;

                case NetworkSearchTask.TYPE_ALL:
                    tv_usPhonetic.setText(resultSet.getusPhonetic());
                    tv_ukPhonetic.setText(resultSet.getukPhonetic());
                    lv_explains.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_explains, resultSet.getExplains()));
                    phonetics.setVisibility(View.VISIBLE);
                    break;
            }
        }
        ((UIUpdater)getActivity()).onHideProgressBar();
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_item_more, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.show_rest:
                        ;
                        break;
                    case R.id.web_translation:
                        ;
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public void changeCollectionState() {
        if(state == State.cancel) {
            iv_star.setImageResource(R.drawable.ic_star_rate_yellow_500_24dp);
            state = State.select;
            Toast.makeText(getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
        } else {
            iv_star.setImageResource(R.drawable.ic_star_rate_grey_500_24dp);
            state = State.cancel;
            Toast.makeText(getActivity(), "已取消收藏", Toast.LENGTH_SHORT).show();
        }
    }
}
