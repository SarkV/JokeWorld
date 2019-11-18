package com.avtdev.jokeworld.nerwork.controllers;

import com.avtdev.jokeworld.BuildConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AbstractController {

    Retrofit.Builder mRetrofitBuilder;

    public interface ICallback{
        void sendReponse(int requestType, int responseCode);
    }

    AbstractController(boolean withToken){
        setRetrofitBuilder(withToken);
    }

    public void setRetrofitBuilder(boolean withToken) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        mRetrofitBuilder = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        if(withToken){
            TokenInterceptor tokenInterceptor = new TokenInterceptor();

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .authenticator(tokenAuthenticator)
                    .addInterceptor(tokenInterceptor)
                    .dispatcher(dispatcher)
                    .build();
        }
    }
}
