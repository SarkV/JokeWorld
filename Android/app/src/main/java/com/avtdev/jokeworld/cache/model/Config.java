package com.avtdev.jokeworld.cache.model;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Config extends RealmObject implements RealmModel {

    @PrimaryKey
    private String key;

    private String value;

    public Config(){}
/*
    public Config(SerializedConfig serializedUser){
        this.key = serializedUser.getKey();
        this.value = serializedUser.getValue();
    }*/

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value ;
    }
}
