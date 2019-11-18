package com.avtdev.jokeworld.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.avtdev.jokeworld.R;
import com.avtdev.jokeworld.nerwork.NetworkParser;
import com.avtdev.jokeworld.utils.Constants;
import com.avtdev.jokeworld.utils.ImageManager;
import com.avtdev.jokeworld.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class LoginActivity extends BaseActivity/* implements ILoginActivityCallback*/ {

    private ViewFlipper mCardFrontLayout;
    private FloatingActionButton mSignupButton;
    private int mActualCard;
    private NetworkParser mNetworkParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        mActualCard = 0;
        mNetworkParser = NetworkParser.getInstance(this);
    }

    private void findViews() {
        mCardFrontLayout = findViewById(R.id.card_front);
        mSignupButton = findViewById(R.id.signupButton);
    }

    public void signUpChange(View view){
        flipCard(1);
    }

    public void passwordRecoverChange(View view){
        flipCard(-1);
    }

    public void loginChange(View view){
        flipCard(0);
    }

    public void flipCard(final int nextCard) {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(mCardFrontLayout, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(mCardFrontLayout, "scaleX", 0f, 1f);
        if(nextCard > mActualCard){
            oa1.setInterpolator(new DecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        }else{
            oa1.setInterpolator(new AccelerateDecelerateInterpolator());
            oa2.setInterpolator(new DecelerateInterpolator());
        }
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(nextCard == -1){
                    mCardFrontLayout.setDisplayedChild(2);
                }else if(nextCard == 1){
                    mCardFrontLayout.setDisplayedChild(1);
                    mSignupButton.show();
                }else{
                    mCardFrontLayout.setDisplayedChild(0);
                    mSignupButton.hide();
                }
                mActualCard = nextCard;
                oa2.start();
            }
        });
        oa1.start();
    }

    public void login(View view){
        TextInputEditText username = findViewById(R.id.usernameLogin);
        TextInputEditText password = findViewById(R.id.passwordLogin);

        username.setText("uve");
        password.setText("123");

        mNetworkParser.login(username.getText().toString(), password.getText().toString());

    }

    public void signUp(View view){
        TextInputEditText email = findViewById(R.id.emailSignUp);
        TextInputEditText username = findViewById(R.id.usernameSignUp);
        TextInputEditText password = findViewById(R.id.passwordSignUp);
        TextInputEditText passwordRepeat = findViewById(R.id.passwordRepeatSignUp);

        if(Utils.isNull(email.getText().toString())){

        }else if(Utils.isNull(username.getText().toString())){

        }else if(Utils.isNull(password.getText().toString())){

        }else if(Utils.isNull(passwordRepeat.getText().toString())){

        }else if(passwordRepeat.getText().toString().equals(password.getText().toString())){

        }

        mNetworkParser.signUp(email.getText().toString(), username.getText().toString(), password.getText().toString());
/*
        if (checkPermission(Constants.PERMISSIONS.READ_USER)){
            ImageManager.pickImage(this, Constants.CALLBACKS.USER_IMAGE);
        }*/
    }

    public void recoverPassword(View view){
        TextInputEditText email = findViewById(R.id.emailPasswordRecovery);
        if(!Utils.isNull(email.getText().toString()))
            mNetworkParser.resetPassword(email.getText().toString().trim());
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String imageBytes = ImageManager.getBase64Image(this, selectedImage);
            byte[] imageByte = ImageManager.getImage(this, selectedImage);
            if(imageBytes != null)
                mNetworkParser.updateProfileImage(imageBytes);
        }else {
            Toast.makeText(LoginActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
/*
    @Override
    public void loginResult(int status, SerializedUser serializedUser, List<SerializedConfig> serializedConfig) {
       /* if(status == NetworkConstant.ResponseCode.OK){
            mCacheManager.saveConfig(serializedConfig);
            showPrivacyDialog(serializedUser);
        }else if(status == NetworkConstant.ResponseCode.UNAUTHORIZED){
            showErrorDialog(R.drawable.ic_lock, R.string.error_incorrect_credentials_title, R.string.error_incorrect_credentials_info);
        }else if(status == NetworkConstant.ResponseCode.NO_CONNECTION){
            showNoInternetDialog();
        }else{
            showServerErrorDialog();
        }
    }

    @Override
    public void signupResult(int status, SerializedUser serializedUser, List<SerializedConfig> serializedConfig) {
      /*  if(status == NetworkConstant.ResponseCode.OK){
            mCacheManager.saveConfig(serializedConfig);
            showPrivacyDialog(serializedUser);
        }else if(status == NetworkConstant.ResponseCode.ACCOUNT_EXISTS){
            showErrorDialog(null, R.string.error_account_exists_title, R.string.error_account_exists_info);
        }else if(status == NetworkConstant.ResponseCode.USERNAME_EXISTS){
            showErrorDialog(null, R.string.error_username_exists_title, R.string.error_username_exists_info);
        }else if(status == NetworkConstant.ResponseCode.NO_CONNECTION){
            showNoInternetDialog();
        }else{
            showServerErrorDialog();
        }
    }

    @Override
    public void passwordRecoveryResult(int status) {
        /*if(status == NetworkConstant.ResponseCode.OK){

        }else if(status == NetworkConstant.ResponseCode.NO_CONNECTION){
            showNoInternetDialog();
        }else{
            showServerErrorDialog();
        }
    }

    public void enterApplication(){
        /*mCacheManager.updateActualUser();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }*/
}
