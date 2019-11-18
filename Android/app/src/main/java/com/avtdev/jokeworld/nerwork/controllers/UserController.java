package com.avtdev.jokeworld.nerwork.controllers;

import android.content.Context;

import com.avtdev.jokeworld.nerwork.apis.UserApi;
import com.avtdev.jokeworld.utils.Logger;

public class UserController extends AbstractController{

    public static UserController mInstance;

    UserApi mUserApi;

    private UserController(){
        super();
        mUserApi = mRetrofitBuilder.build().create(UserApi.class);
    }

    public UserController getInstance(){
        if(mInstance == null) {
            mInstance = new UserController();
        }
        return mInstance;
    }

    public void login(Context context, String email, String password, ICallback listener){
        try{
            mUserApi.

        }catch (Exception ex){
            Logger.error("login", ex.getMessage());
        }


    }
}
