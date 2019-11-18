package com.avtdev.jokeworld.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class ImageManager {

    public static void pickImage(Activity activity, int callBack){
        try {
            Intent intent  = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            activity.startActivityForResult(intent, callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBase64Image(Context context, Uri imageUri){
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            return new String(Base64.encode(bao.toByteArray(), Base64.DEFAULT), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImage(Context context, Uri imageUri){
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            return bao.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadImage(final ImageView image, Activity activity, String imagePath){
        if(imagePath != null){
            ImageLoader imageLoader = ImageLoader.getInstance();

            File cacheDir = StorageUtils.getCacheDirectory(activity);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                    .diskCacheExtraOptions(480, 800, null)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                    .diskCacheSize(50 * 1024 * 1024)
                    .diskCacheFileCount(100)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(activity)) // default
                    .imageDecoder(new BaseImageDecoder(true)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .build();
            imageLoader = imageLoader.getInstance();
            if (!imageLoader.isInited()) {
                imageLoader.init(config);
            }
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .build();
            ImageLoader.getInstance().init(config);
            imageLoader.displayImage(imagePath, image ,defaultOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String url, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    ((ImageView) view).setImageBitmap(null);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    image.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                }
            });
        }
    }
}
