package com.sams.test.data.productinfojson.remote;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.data.productinfojson.local.SQLiteStrorage;
import com.sams.test.json.IJsonParser;
import com.sams.test.util.AppExecutors;
import com.sams.test.util.AppUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Implementation of data source that comes from walmart server using rest api.
 * It will download json that describes the product list and info
 */

public class JsonDownload implements IProductInfoSource {
    private static final String LOG_TAG = "JsonDownload";

    private static final String TEMP_JSON_FILE = "temp_json.txt";
    private AppExecutors mAppExecutors;
    private static JsonDownload INSTANCE;
    private IJsonParser mJsonParser;
    private Context mContext;

    // Prevent direct instantiation.
    private JsonDownload(@NonNull AppExecutors appExecutors,
                           @NonNull IJsonParser jsonParser, Context context) {
        mAppExecutors = appExecutors;
        mJsonParser = jsonParser;
        mContext = context;
    }

    public static JsonDownload getInstance(@NonNull AppExecutors appExecutors,
                                             @NonNull IJsonParser jsonParser,
                                           @NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteStrorage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JsonDownload(appExecutors,
                            jsonParser, context);
                }
            }
        }
        return INSTANCE;
    }

    private int getPageIndex (int arrayStartingPosition) {

        int index = arrayStartingPosition / 30 +1;
        return index;
    }

    private String createUrl(int startingIndex) {
        int pageIndex = getPageIndex(startingIndex);
        StringBuilder builder = new StringBuilder();
        builder.append("https://mobile-tha-server.appspot.com/walmartproducts/");
        builder.append(pageIndex);
        int productsToGet = 30;
        builder.append("/");
        builder.append(productsToGet); // Always get the max possible download.
        return builder.toString();
    }

    /**
     * Note: {@link LoadProductsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void loadProducts(@NonNull final int startingIndex,
                             @NonNull final LoadProductsCallback callback) {
        Log.d(LOG_TAG, "loadProducts");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    try {
                    FileOutputStream  outputStream =
                            mContext.openFileOutput(TEMP_JSON_FILE, Context.MODE_PRIVATE);
                    boolean success =
                            AppUtils.getJsonResponse(createUrl(startingIndex),
                                    outputStream);
                    if (!success) {
                        failed(callback, "json response failed");
                        return;
                    }
                    FileInputStream  in =
                                mContext.openFileInput(TEMP_JSON_FILE);
                    if (!mJsonParser.parse(in)) {
                        failed(callback, "parsing failed");
                        return;
                    }

                    final List<ProductInfo> productInfos = mJsonParser.getProducts();
                    Log.d(LOG_TAG, "loadProducts size: " + productInfos.size());
                    mAppExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (productInfos == null || productInfos.isEmpty()) {
                                callback.onDataNotAvailable();
                            } else {
                                callback.onProductsLoaded(productInfos);
                            }
                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    failed(callback, "FileNotFoundException");
                }
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public int getTotalProducts() {
        return mJsonParser.getTotalProductCount();
    }

    private void failed(@NonNull final LoadProductsCallback callback, String message) {
        Log.e(LOG_TAG, "error: " + message);
        mAppExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                Log.e(LOG_TAG, "calling CB");
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void loadProduct(@NonNull String productId, @NonNull LoadProductCallback callback) {

    }
}
