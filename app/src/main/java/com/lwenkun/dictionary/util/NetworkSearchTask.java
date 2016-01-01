package com.lwenkun.dictionary.util;

import android.os.AsyncTask;
import android.util.Log;

import com.lwenkun.dictionary.model.TranslateResultSet;
import com.lwenkun.dictionary.resource.Constants;
import com.lwenkun.dictionary.ui.Interface.UIUpdater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 15119 on 2015/12/30.
 */
public class NetworkSearchTask extends AsyncTask<String,Void,TranslateResultSet> {

    private String TAG = "NetworkSearchTask";

    public final static int TYPE_ALL = 0;
    public final static int TYPE_EXCEPT_PHONETIC = 1;
    public final static int TYPE_EXCEPT_PHONETIC_AND_EXPLAINS = 2;
    private static int type = TYPE_EXCEPT_PHONETIC_AND_EXPLAINS;

    private UIUpdater updater;

    public NetworkSearchTask(UIUpdater updater) {
        this.updater = updater;
    }

    @Override
    protected void onPreExecute() {
        updater.onShowProgressBar();
    }

    @Override
    protected TranslateResultSet doInBackground(String... params) {
        return getResultSetFromUrl(params[0]);
    }

    public TranslateResultSet getResultSetFromUrl(String key) {

        TranslateResultSet resultSet = null;
        HttpURLConnection connection = null;
        try {
            URL url = buildURL(key);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream inputStream = connection.getInputStream();
            resultSet = decodeResultFromInputStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return resultSet;
    }

    public URL buildURL(String key) {

        URL url = null;

        try {
            url = new URL(Constants.YD_TRANSLATE_URL
                    + "?keyfrom=" + Constants.YD_KEY_FROM
                    + "&key=" + Constants.YD_TRANSLATE_API_KEY
                    + "&type=" + Constants.YD_TYPE
                    + "&doctype=" + Constants.YD_DOCTYPE
                    + "&version=" + Constants.YD_VERSION
                    + "&q=" + key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }

    public TranslateResultSet decodeResultFromInputStream(InputStream inputStream) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;

        try {
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, builder.toString());

        return parseJsonIntoResultSet(builder.toString());
    }

    public TranslateResultSet parseJsonIntoResultSet(String jsonData) {

        String translation = null;
        String query = null;
        String usPhonetic = null;
        String ukPhonetic = null;
        String sExplains[] = null;

        try {
            JSONObject jResultSet = new JSONObject(jsonData);
            JSONArray translations = jResultSet.getJSONArray("translation");
            translation = translations.getString(0);
            query = jResultSet.getString("query");
            type = TYPE_EXCEPT_PHONETIC_AND_EXPLAINS;
            JSONObject basic = jResultSet.getJSONObject("basic");
            JSONArray explains = basic.getJSONArray("explains");
            sExplains = new String[explains.length()];
            for(int i = 0; i < explains.length(); i++){
                sExplains[i] = explains.getString(i);
                Log.d(TAG, sExplains[i]);
            }
            type = TYPE_EXCEPT_PHONETIC;
            usPhonetic = basic.getString("us-phonetic");
            ukPhonetic = basic.getString("uk-phonetic");
            type = TYPE_ALL;
        } catch (Exception e) {
//            JSONObject jResultSet = new JSONObject(jsonData);
//            JSONArray translations = jResultSet.getJSONArray("translation");
//            JSONObject basic = jResultSet.getJSONObject("basic");
//            JSONArray explains = basic.getJSONArray("explains");
//            String query = jResultSet.getString("query");
//            String translation = translations.getString(0);
            e.printStackTrace();
        }
        return new TranslateResultSet(query, translation, usPhonetic, ukPhonetic, sExplains);
    }

    @Override
    protected void onPostExecute(TranslateResultSet translateResultSet) {
        updater.onDataUpdate(translateResultSet, type);
    }
}
