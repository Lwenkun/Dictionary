package com.lwenkun.dictionary.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 15119 on 2015/12/28.
 */
public class TranslateResultSet implements Serializable{

    private String sJSON;
    private String query;
    private String translation;
    private String usPhonetic;
    private String ukPhonetic;
    private String[] explains;

    public final static int TYPE_ALL = 0;
    public final static int TYPE_EXCEPT_PHONETIC = 1;
    public final static int TYPE_EXCEPT_PHONETIC_AND_EXPLAINS = 2;
    public int type = TYPE_EXCEPT_PHONETIC_AND_EXPLAINS;

    public TranslateResultSet(String query,
                              String translation,
                              String usPhonetic,
                              String ukPhonetic,
                              String[] explains) {
        this.query = query;
        this.translation = translation;
        this.usPhonetic = usPhonetic;
        this.ukPhonetic = ukPhonetic;
        this.explains = explains;
    }

//    public TranslateResultSet(String sJSON,
//                              String query,
//                              String translation,
//                              String usPhonetic,
//                              String ukPhonetic,
//                              String[] explains) {
//        this.sJSON = sJSON;
//        this.query = query;
//        this.translation = translation;
//        this.usPhonetic = usPhonetic;
//        this.ukPhonetic = ukPhonetic;
//        this.explains = explains;
//    }

    public TranslateResultSet(@NonNull String sJSON) {
       this.sJSON = sJSON;
    }

    public String getsJSON() {
        return sJSON;
    }

    public String getQuery() {
        if (query == null && sJSON != null) {
            parseJSONIntoResultSet(sJSON);
        }
        return query;
    }

    public String getTranslation() {

        if (translation == null && sJSON != null) {
            parseJSONIntoResultSet(sJSON);
        }
        return translation;
    }

    public String getusPhonetic() {
        if (usPhonetic == null && sJSON != null) {
            parseJSONIntoResultSet(sJSON);
        }
        return usPhonetic;
    }

    public String getukPhonetic() {
        if (ukPhonetic == null && sJSON != null) {
            parseJSONIntoResultSet(sJSON);
        }
        return ukPhonetic;
    }

    public String[] getExplains() {
        if (explains == null && sJSON != null) {
            parseJSONIntoResultSet(sJSON);
        }
        return explains;
    }

    public int getType() {
        return type;
    }

    public void parseJSONIntoResultSet(String jsonData) {

        try {
            JSONObject jResultSet = new JSONObject(jsonData);
            JSONArray translations = jResultSet.getJSONArray("translation");
            translation = translations.getString(0);
            query = jResultSet.getString("query");
            type = TYPE_EXCEPT_PHONETIC_AND_EXPLAINS;
            JSONObject basic = jResultSet.getJSONObject("basic");
            JSONArray jExplains = basic.getJSONArray("explains");
            explains = new String[jExplains.length()];
            for(int i = 0; i < jExplains.length(); i++){
                explains[i] = jExplains.getString(i);
            }
            type = TYPE_EXCEPT_PHONETIC;
            usPhonetic = basic.getString("us-phonetic");
            ukPhonetic = basic.getString("uk-phonetic");
            type = TYPE_ALL;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
