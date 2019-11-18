package com.avtdev.jokeworld.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.avtdev.jokeworld.cache.CacheManager;
import com.avtdev.jokeworld.cache.model.Config;
import com.avtdev.jokeworld.utils.Constants;
import com.avtdev.jokeworld.utils.ImageManager;
import com.avtdev.jokeworld.utils.Logger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BaseActivity extends AppCompatActivity {

    protected CacheManager mCacheManager;
    private AlertDialog mProgressDialog;
    boolean declined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCacheManager = CacheManager.getInstance(this);
    }

    protected boolean checkPermission(int permissionCode) {
        boolean permissionGranted = false;

        switch (permissionCode) {
            case Constants.PERMISSIONS.READ_USER:
            case Constants.PERMISSIONS.READ_JOKE:
                if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissionCode);
                } else {
                    permissionGranted = true;
                }
            break;
        }
        return permissionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS.READ_USER:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageManager.pickImage(this, Constants.CALLBACKS.USER_IMAGE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case Constants.PERMISSIONS.READ_JOKE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImageManager.pickImage(this, Constants.CALLBACKS.JOKE_IMAGE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }
/*
    protected void showErrorDialog(Integer iconId, Integer titleId, Integer infoId){
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_error, null);
        ((ImageView) dialogView.findViewById(R.id.errorDialogIcon)).setImageDrawable(getDrawable(iconId != null ? iconId : R.drawable.ic_default_error));
        ((TextView) dialogView.findViewById(R.id.errorDialogTitle)).setText(getString(titleId != null ? titleId : R.string.error_server_title));
        ((TextView) dialogView.findViewById(R.id.errorDialogInfo)).setText(getString(infoId != null ? infoId : R.string.error_server_info));
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);
        dialogView.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void showNoInternetDialog(){
        showErrorDialog(R.drawable.ic_no_internet, R.string.error_no_internet_title, R.string.error_no_internet_info);
    }

    protected void showServerErrorDialog(){
        showErrorDialog(R.drawable.ic_cloud_off, R.string.error_server_title, R.string.error_server_info);
    }

    public void showProgressDialog(){
        if(mProgressDialog == null){
            ProgressBar progressBar = new ProgressBar(this);
            progressBar.setIndeterminate(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(progressBar);
            builder.setCancelable(false);

            mProgressDialog = builder.create();
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog.hide();
        }
    }



    protected void showPrivacyDialog(){
        Config privacyConfig = mCacheManager.findConfig(Constants.ConfigKeys.PRIVACY_URL);
        Config privacyVersionConfig = mCacheManager.findConfig(Constants.ConfigKeys.PRIVACY_VERSION);
        Integer privacyVersion = -1;
        try {
            privacyVersion = privacyVersionConfig != null ? Integer.valueOf(privacyVersionConfig.getValue()) : privacyVersion;
        }catch (Exception e){
            Logger.error(null, e.getMessage());
        }

        declined = false;
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_privacy, null);

        WebView webView =  dialogView.findViewById(R.id.privacyWebView);

        if(privacyVersion == null || privacyVersion <= serializedUser.getPrivacyAcceptedVersion()){
            ((LoginActivity) this).enterApplication(serializedUser);
            return;
        }


        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);
        final Button acceptButton = dialogView.findViewById(R.id.btn_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((LoginActivity) BaseActivity.this).enterApplication(serializedUser);
            }
        });

        dialogView.findViewById(R.id.btn_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(declined){
                    dialog.dismiss();
                    logout();
                }else{
                    declined = true;
                    showErrorDialog(R.drawable.ic_lock, R.string.error_privacy_declined_title, R.string.error_privacy_declined_info);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                acceptButton.setEnabled(true);

                view.postDelayed( new Runnable () {
                    @Override
                    public void run() {
                        view.scrollTo(0, 0);
                    }
                }, 100);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                    setTitle(R.string.app_name);
            }
        });


        webView.loadUrl(privacyConfig.getValue());
        dialog.show();
    }

    protected void logout(){
        if(!(this instanceof LoginActivity)){
            mCacheManager.logout();
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }*/
}
