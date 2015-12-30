package com.lwenkun.dictionary.ui.widget;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lwenkun.dictionary.R;
import com.lwenkun.dictionary.model.TranslateResultSet;

import java.util.List;

/**
 * Created by 15119 on 2015/12/28.
 */
public class CollectionReviewAdapter extends ArrayAdapter<TranslateResultSet> {

    private int resourceId;

    public CollectionReviewAdapter(Context context, int resourceId, List<TranslateResultSet> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TranslateResultSet translateResultSet = getItem(position);
        TextView tv_query;
        TextView tv_translate;
        TextView tv_usPhonetic;
        TextView tv_ukPhonetic;
        TextView tv_more;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            tv_query = (TextView) convertView.findViewById(R.id.tv_query);
            tv_translate = (TextView) convertView.findViewById(R.id.tv_translation);
            tv_usPhonetic = (TextView) convertView.findViewById(R.id.tv_us_phonetic);
            tv_ukPhonetic = (TextView) convertView.findViewById(R.id.tv_uk_phonetic);
            tv_more = (TextView) convertView.findViewById(R.id.tv_more);

            tv_query.setText(translateResultSet.getQuery());
            tv_usPhonetic.setText(translateResultSet.getusPhonetic());
            tv_ukPhonetic.setText(translateResultSet.getukPhonetic());
            tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v);
                }
            });
            ViewHolder viewHolder = new ViewHolder(tv_query, tv_translate, tv_usPhonetic, tv_ukPhonetic);
            convertView.setTag(viewHolder);
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tv_query.setText(translateResultSet.getTranslation());
            viewHolder.tv_translation.setText(translateResultSet.getTranslation());
            viewHolder.tv_usPhonetic.setText(translateResultSet.getusPhonetic());
            viewHolder.tv_ukPhonetic.setText(translateResultSet.getukPhonetic());
        }

        return convertView;
    }

    class ViewHolder {

        TextView tv_query;
        TextView tv_translation;
        TextView tv_usPhonetic;
        TextView tv_ukPhonetic;

        public ViewHolder(TextView tv_query,
                          TextView tv_translation,
                          TextView tv_usPhonetic,
                          TextView tv_ukPhonetic) {

            this.tv_query = tv_query;
            this.tv_translation = tv_translation;
            this.tv_usPhonetic = tv_usPhonetic;
            this.tv_ukPhonetic = tv_ukPhonetic;
        }


    }


    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
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
}
