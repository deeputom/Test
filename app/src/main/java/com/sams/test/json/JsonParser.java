package com.sams.test.json;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.util.AppUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by deepu on 8/24/2018.
 */

public class JsonParser implements IJsonParser{

    private static final String LOG_TAG = "JsonParser";

    private JsonResponse mParsedJsonObj;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean parse(@NonNull InputStream inputStream) {
        Log.d(LOG_TAG, "parse");
        boolean parseSuccess = false;
        try (Reader br = new BufferedReader(
                new InputStreamReader(
                        inputStream, StandardCharsets.UTF_8))){
            final GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(JsonResponse.class,
                    new JsonResponse.JsonResponseDeserializer());
            builder.registerTypeAdapter(ProductInfo.class,
                    new ProductInfo.ProductInfoDeserializer());
            final Gson gson = builder.create();
            //AppUtils.print(inputStream);
            mParsedJsonObj = gson.fromJson(br, JsonResponse.class);
            parseSuccess = (mParsedJsonObj != null) ? true : false;
        } catch (IOException | JsonParseException e) {
            Log.e(LOG_TAG, "JsonParser exception: " , e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parseSuccess;
    }

    @Override
    public int getTotalProductCount() {
        int count = 0;
        try {
            count = Integer.parseInt(mParsedJsonObj.getTotalProducts());
        } catch (NumberFormatException e) {
            Log.d(LOG_TAG, "getTotalProductCount NumberFormatException", e);
        }
        return count;
    }

    @Override
    public List<ProductInfo> getProducts () {
        return new ArrayList<>(Arrays.asList(mParsedJsonObj.getProducts()));
    }

    @Override
    public int getPageIndex() {
        int count = 0;
        try {
            count = Integer.parseInt(mParsedJsonObj.getPageNumber());
        } catch (NumberFormatException e) {
            Log.d(LOG_TAG, "getPageIndex NumberFormatException", e);
        }
        return count;
     }

    public String toJsonString() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String jsonToString = gson.toJson(mParsedJsonObj);
        Log.d(LOG_TAG, "********Printing JSON");
        Log.d(LOG_TAG, jsonToString);
        return jsonToString;
    }
}
