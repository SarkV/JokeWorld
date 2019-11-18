package com.avtdev.jokeworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;

public class Utils {

    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getDevideId(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String utcToLocal(String utcDate){
        String localDate = null;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(utcDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            localDate = dateFormatter.format(value);
        } catch (ParseException e) {
            localDate = "00-00-0000 00:00";
        }
        return localDate;
    }

    public static String localToUtc(String localDate){
        String utcDate = null;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getDefault());
            Date value = formatter.parse(localDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            utcDate = dateFormatter.format(value);
        } catch (ParseException e) {
            utcDate = "00-00-0000 00:00";
        }
        return utcDate;
    }

    public static boolean hasChange(Object originalObject, Object newObject){
        if(newObject == null){
            return false;
        }
        if(originalObject == null){
            return true;
        }
        String objectString1 = new Gson().toJson(originalObject);
        String objectString2 = new Gson().toJson(newObject);
        return !objectString1.equals(objectString2);
    }

    public static boolean isNull(String data){
        return data == null || data.trim().length() == 0;
    }

    public static Object getSharedPreferences(Context context, String key, @NonNull Object defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES.PREFERENCES_FILE, Context.MODE_PRIVATE);

        if(defaultValue instanceof String){
            return sharedPreferences.getString(key, (String) defaultValue);
        }else if(defaultValue instanceof Integer){
            return sharedPreferences.getInt(key, (Integer) defaultValue);
        }else if(defaultValue instanceof Boolean){
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        }else{
            return null;
        }
    }

    public static String getStringSharedPreferences(Context context, String key, @NonNull String defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES.PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, defaultValue);
    }

    public static int getIntSharedPreferences(Context context, String key, @NonNull int defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES.PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(key, defaultValue);
    }

    public static boolean getBooleanSharedPreferences(Context context, String key, @NonNull boolean defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES.PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void setSharedPreferences(Context context, String key, Object value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES.PREFERENCES_FILE, Context.MODE_PRIVATE);
        try{
            SharedPreferences.Editor edit = sharedPreferences.edit();

            if(value == null){
                edit.remove(key);
            }else if(value instanceof String){
                edit.putString(key, (String) value);
            }else if(value instanceof Integer){
                edit.putInt(key, (Integer) value);
            }else if(value instanceof Boolean){
                edit.putBoolean(key, (Boolean) value);
            }

            edit.commit();
        }catch (Exception ex){
            Logger.error("setSharedPreferences", ex.getMessage());
        }
    }
}
