package com.lwenkun.dictionary.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.AlbumManager;
import com.lwenkun.dictionary.model.CollectionManager;
import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;
import com.lwenkun.dictionary.ui.activity.MainActivity;

import java.util.ArrayList;

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
    private TranslateResultSet resultSet;
    private State state = State.cancel;
    private enum State {
        select,cancel
    }



    private boolean isCollectEnabled;

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
                if (isCollectEnabled()) {
                    updateCollectionState();
                }
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
        resultSet = (TranslateResultSet) getArguments().getSerializable(MainActivity.KEY_SER);
        setCollectEnable(true);

        if (resultSet != null) {

            tv_query.setText(resultSet.getQuery());
            tv_translation.setText(resultSet.getTranslation());
            View phonetics = getView().findViewById(R.id.phonetics);
            switch (resultSet.getType()) {

                case TranslateResultSet.TYPE_EXCEPT_PHONETIC:
                    lv_explains.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_explains, resultSet.getExplains()));

                case TranslateResultSet.TYPE_EXCEPT_PHONETIC_AND_EXPLAINS:
                    phonetics.setVisibility(View.INVISIBLE);
                    break;

                case TranslateResultSet.TYPE_ALL:
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

    public void showCollectioinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("收藏词汇")
                .setIcon(R.drawable.ic_book_black_18dp)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public void updateCollectionState() {
        if(state == State.cancel) {
            iv_star.setImageResource(R.drawable.ic_star_rate_yellow_500_24dp);
            state = State.select;
            chooseAlbumToSave();
            Toast.makeText(getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
        } else {
            iv_star.setImageResource(R.drawable.ic_star_rate_grey_500_24dp);
            state = State.cancel;
            Toast.makeText(getActivity(), "已取消收藏", Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseAlbumToSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View lv_albumList = getActivity().getLayoutInflater().inflate(R.layout.list_item_albums, null, false);
        ListView lv_albums = (ListView) lv_albumList.findViewById(R.id.list_view_albums);
        final ArrayList<String> albums = AlbumManager.getInstance(getActivity()).getAlbums();
        lv_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CollectionManager.getInstance(getActivity()).saveToAlbum(resultSet, albums.get(position));
            }
        });
        lv_albums.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, albums));
        builder.setTitle("请选择单词本")
                .setView(lv_albumList)
                .setPositiveButton("创建新单词本", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).showAddNewDialog();
                    }
                })
                .create()
                .show();

    }

    private void setCollectEnable(boolean isCollectEnabled) {
        this.isCollectEnabled = isCollectEnabled;
    }

    public boolean isCollectEnabled() {
        return isCollectEnabled;
    }
}