package com.avtdev.jokeworld.utils;

import android.util.Log;

import com.google.gson.Gson;

public class Logger {

    private static String TAG = "WearinLog";

    private static String createMessage(String className, String method, Object... data){
        String message = "[" + className + " - " + method + "]";
        message += " --> ";
        if(data != null){
            boolean first = true;
            for (Object dataAux : data) {
                if(!first){
                    message += " -- ";
                }else{
                    first = false;
                }
                if(dataAux == null){
                    message += "null";
                }else if(dataAux instanceof String){
                    message += String.valueOf(dataAux);
                }else{
                    message += new Gson().toJson(dataAux);
                }
            }

        }else{
            message += "null";
        }
        return message;
    }

    public static void info(String method, Object... data){
        method = method == null ? Thread.currentThread().getStackTrace()[3].getMethodName() : method;
        String message = createMessage(Thread.currentThread().getStackTrace()[3].getClassName(),method, data);
        Log.i(TAG, message);
    }

    public static void info(Object... data){
        info(data);
    }

    public static void debug(String method, Object... data){
        method = method == null ? Thread.currentThread().getStackTrace()[3].getMethodName() : method;
        String message = createMessage(Thread.currentThread().getStackTrace()[3].getClassName(), method, data);
        Log.d(TAG, message);
    }

    public static void debug( Object... data){
        debug(data);
    }

    public static void error(String method, Object... data){
        method = method == null ? Thread.currentThread().getStackTrace()[3].getMethodName() : method;
        String message = createMessage(Thread.currentThread().getStackTrace()[3].getClassName(), method, data);
        Log.e(TAG, message);
    }

    public static void error(Object... data){
        error(null, data);
    }

}
