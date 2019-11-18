package com.avtdev.jokeworld.nerwork;

import android.content.Context;

import com.avtdev.jokeworld.BuildConfig;
import com.avtdev.jokeworld.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class NetworkParser {

    private static NetworkParser mNetworkParser;

    private NetworkParser(Context context){
        mContext = context;
        Logger.info(BuildConfig.ParseId);
        Logger.info(BuildConfig.ParseKey);

        Parse.initialize(new Parse.Configuration.Builder(mContext)
                .applicationId(BuildConfig.ParseId)
                .clientKey(BuildConfig.ParseKey)
                .server("https://parse.buddy.com/parse/")
                .build());
        Parse.Buddy.initialize();
    }

    public static NetworkParser getInstance(Context context){
        if(mNetworkParser == null){
            mNetworkParser = new NetworkParser(context);
        }
        return mNetworkParser;
    }

    public boolean alreadyLogged(){
        return ParseUser.getCurrentUser() != null;
    }

    public ParseUser getActualUser(){
        return ParseUser.getCurrentUser();
    }

    /**
     * START USER FUNCTIONS
     */

    public void login(String username, String password){
        try {
            ParseUser user = new ParseUser();
            user.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {

                    } else {
                       // Logger.error(e.getStackTrace());
                    }
                }
            });
        }catch (Exception e){
            Logger.error(e);
        }
    }

    public void signUp(String email, String username, String password){
        try {
            ParseUser user = new ParseUser();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                    } else {
                        Logger.error(e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            Logger.error(e.getMessage());
        }
    }

    public void updateProfileImage(String file){
      /*  if(alreadyLogged()){
            final ParseFile parseFile = new ParseFile("image.jpg", file);
            HashMap<String, Object> data = new HashMap<>();
            data.put(NetworkConstant.OBJECT.USER_IMAGE, parseFile);
            updateUser(data);
        }*/

        HashMap<String, Object> data = new HashMap<>();
        data.put(NetworkConstant.OBJECT.ID, "8bUtXoV5kr");
        data.put(NetworkConstant.OBJECT.USER_IMAGE, file);
        ParseCloud.callFunctionInBackground("changeImage", data, new FunctionCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if(e == null){
                    Logger.info("Correcto");

                }else{
                    Logger.info(e.getMessage());
                }
            }
        });
    }

    public void updateUser(Map<String, Object> userData){
        if(alreadyLogged()) {
            userData.put(NetworkConstant.OBJECT.ID, ParseUser.getCurrentUser().getObjectId());
            ParseCloud.callFunctionInBackground(NetworkConstant.FUNCTIONS.USER_UPDATE, userData, new FunctionCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {

                }
            });
        }
    }

    public void resetPassword(String email){
        try {
            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // An email was successfully sent with reset instructions.
                    } else {
                        Logger.error(e);
                    }
                }
            });
        }catch (Exception e){
            Logger.error(e);
        }
    }

    public void logOut(){
        try {
            ParseUser.logOut();
        }catch (Exception e){
            Logger.error(e);
        }
    }

    /**
     * END USER FUNCTIONS
     */
}
