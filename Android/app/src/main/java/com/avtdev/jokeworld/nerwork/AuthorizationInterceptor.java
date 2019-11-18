package com.avtdev.jokeworld.nerwork;

import android.content.Context;

import com.avtdev.jokeworld.BuildConfig;
import com.avtdev.jokeworld.utils.Constants;
import com.avtdev.jokeworld.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;

import javax.xml.validation.Validator;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

import static co.abc.utils.abcConstants.ACCESS_TOKEN;
import static co.abc.utils.abcConstants.BASE_URL;
import static co.abc.utils.abcConstants.GCM_TOKEN;
import static co.abc.utils.abcConstants.JWT_TOKEN_PREFIX;
import static co.abc.utils.abcConstants.REFRESH_TOKEN;
import static com.avtdev.jokeworld.utils.Constants.PREFERENCES.ACCESS_TOKEN;

/**
 * Created by ravindrashekhawat on 21/03/17.
 */

public class AuthorizationInterceptor implements Interceptor {
    private static Retrofit retrofit = null;
    private static String accessToken;
    private static String refreshToken;
    private static Context mContext;

    public AuthorizationInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request modifiedRequest = null;

        accessToken = Utils.getStringSharedPreferences(mContext, ACCESS_TOKEN, "");

        Response response = chain.proceed(request);

        accessToken = Utils.getStringSharedPreferences(mContext, ACCESS_TOKEN, "");

        if (response.code() == Constants.HTTP_CODES.UNAUTHORIZED && !Utils.isNull(accessToken)) {

            if(accessToken != null){
                modifiedRequest = request.newBuilder()
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();
                return chain.proceed(modifiedRequest);
            }
        }
        return response;
    }

    public String refreshToken() {
        final String accessToken = null;

        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BuildConfig.URL + "/autologin")
                .method("POST", reqbody)
                .addHeader("Authorization", "Bearer " + refreshToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == Constants.HTTP_CODES.OK) {
                // Get response
                String jsonData = response.body().string();

                Gson gson = new Gson();
                RefreshTokenResponseModel refreshTokenResponseModel = gson.fromJson(jsonData, RefreshTokenResponseModel.class);
                if (refreshTokenResponseModel.getRespCode().equals("1")) {

                    Utils.setSharedPreferences(mContext, ACCESS_TOKEN, refreshTokenResponseModel.getResponse());

                    return refreshTokenResponseModel.getResponse();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

}