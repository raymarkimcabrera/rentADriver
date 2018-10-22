package com.renta.renta_driver.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
public class ImageUtil {

    public static void loadImageFromUrl(Context context, ImageView imageView, String url) {
        Glide.with(context)   // pass Context
                .load(url)// pass the image url
                .into(imageView);
    }

}
