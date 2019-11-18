package com.avtdev.jokeworld.cache;

import android.app.Application;
import android.content.Context;

import com.avtdev.jokeworld.cache.model.Config;
import com.avtdev.jokeworld.cache.model.User;
import com.avtdev.jokeworld.nerwork.NetworkParser;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.avtdev.jokeworld.utils.Utils.hasChange;


public class CacheManager extends Application  {

    private static CacheManager mInstance;
    private static Context mContext;
    private NetworkParser mNetworkParser;
    private RealmConfiguration mRealmConfiguration;
    private User mActualUser;

    private CacheManager(Context context){
        mContext = context;
        Realm.init(mContext);

        RealmConfiguration.Builder realmConfigurationBuilder = new RealmConfiguration.Builder()
                                                                    .deleteRealmIfMigrationNeeded()
                                                                    .schemaVersion(1);
        mRealmConfiguration = realmConfigurationBuilder.build();

        mNetworkParser = NetworkParser.getInstance(mContext);

        setUser();
    }

    public static CacheManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new CacheManager(context);
        }
        return mInstance;
    }

    private Realm getRealmInstance(){
        return Realm.getInstance(mRealmConfiguration);
    }

    public void setUser(){
        if(mNetworkParser.alreadyLogged()){
            mActualUser = new User(mNetworkParser.getActualUser());
            try(final Realm realm = getRealmInstance()) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(mActualUser);
                    }
                });
            }
        }else{
            mActualUser = null;
        }
    }

    /*public void updateUser(HashMap<String, String> data){
        User newUser = new User(parseUser);
        if(mActualUser == null || hasChange(mActualUser, newUser)){
            mActualUser = newUser;
            try(final Realm realm = getRealmInstance()) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(mActualUser);
                    }
                });
            }
        }
    }
/*
    public void setUsers(final List<SerializedUser> listSerializedUser){
        try(final Realm realm = getRealmInstance()){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmQuery<User> query = null;
                    List<User> addedOrUpdated = new ArrayList<>();
                    for (SerializedUser serializedUser : listSerializedUser) {
                        if(NetworkConstant.Actions.DELETED.equals(serializedUser.getAction())){
                            if(query == null) query = realm.where(User.class);
                            query.or().equalTo("id", serializedUser.getId());
                        }else{
                            addedOrUpdated.add(new User(serializedUser));
                        }
                    }
                    if(query != null) query.findAll().deleteAllFromRealm();
                    if(addedOrUpdated.size() > 0) realm.copyToRealmOrUpdate(addedOrUpdated);
                }
            });
        }
    }

    public RealmResults<User> getUsers(RealmChangeListener<RealmResults<User>> listener){
        RealmResults<User> result = null;
        try{
            final Realm realm = getRealmInstance();
            result = realm.where(User.class).findAllAsync();
            result.addChangeListener(listener);
        }catch (Exception e){}
        return result;
    }

    public User findUser(){
        try(final Realm realm = getRealmInstance()){
            return null;//realm.copyFromRealm(realm.where(User.class).equalTo("key", key).findFirst());
        }
    }

    public void logout(){
        mActualUser = null;
        try(final Realm realm = getRealmInstance()){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
        }
    }

    public void saveConfig(final List<SerializedConfig> listSerializedConfig){
        if(listSerializedConfig != null){
            try(final Realm realm = getRealmInstance()){
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmQuery<Config> query = null;
                        List<Config> addedOrUpdated = new ArrayList<>();
                        for (SerializedConfig serializedConfig : listSerializedConfig) {
                            if(NetworkConstant.Actions.DELETED.equals(serializedConfig.getAction())){
                                if(query == null) query = realm.where(Config.class);
                                query.or().equalTo("key", serializedConfig.getKey());
                            }else{
                                addedOrUpdated.add(new Config(serializedConfig));
                            }
                        }
                        if(query != null) query.findAll().deleteAllFromRealm();
                        if(addedOrUpdated.size() > 0) realm.copyToRealmOrUpdate(addedOrUpdated);
                    }
                });
            }

        }
    }

    public Config findConfig(final String key){
        try(final Realm realm = getRealmInstance()){
            return realm.copyFromRealm(realm.where(Config.class).equalTo("key", key).findFirst());
        }
    }*/
}
