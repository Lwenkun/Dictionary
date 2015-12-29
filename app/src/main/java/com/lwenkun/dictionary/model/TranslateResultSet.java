package com.lwenkun.dictionary.model;

/**
 * Created by 15119 on 2015/12/28.
 */
public class TranslateResultSet {

    private String query;
    private String translation;
    private String usPhonetic;
    private String ukPhonetic;
    private String[] explains;

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

    public String getQuery() {
        return query;
    }

    public String getTranslation() {
        return translation;
    }

    public String getusPhonetic() {
        return usPhonetic;
    }

    public String getukPhonetic() {
        return ukPhonetic;
    }

    public String[] getExplains() {
        return explains;
    }
}
