package com.sams.test;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Constants used by view
 */

public class ViewConstants {
        public final static String PROD_LIST_POSITION = "position";
        public final static String CURR_TOTAL_SIZE = "curr_total_size";
        @SuppressLint("ResourceType")
        public static final RequestOptions GLIDE_REQUEST_OPTIONS
                =  new RequestOptions().
                centerCrop().
                fitCenter().
                placeholder(Color.BLACK).
                diskCacheStrategy(DiskCacheStrategy.ALL);
}
