package com.sams.test.json;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.sams.test.data.productinfojson.ProductInfo;

import java.lang.reflect.Type;

/**
 * This class parses the product info in the JSON payload.
 */

public class JsonResponse {
    private static final String LOG_TAG = "JsonResponse";
    @SerializedName(JsonTags.products)
    private ProductInfo[] mProductsArray;
    @SerializedName(JsonTags.totalProducts)
    private String mTotalProducts;
    @SerializedName(JsonTags.pageNumber)
    private String mPageNumber;
    @SerializedName(JsonTags.pageSize)
    private String mPageSize;
    @SerializedName(JsonTags.statusCode)
    private String mStatusCode;

    public ProductInfo [] getProducts () {
        return mProductsArray;
    }

    public String getPageSize () {
        return mPageSize;
    }

    public String getStatusCode () {
        return mStatusCode;
    }

    public String getTotalProducts () {
        return mTotalProducts;
    }

    public String getPageNumber () {
        return mPageNumber;
    }

    public static class JsonResponseDeserializer implements JsonDeserializer<JsonResponse> {

        @Override
        public JsonResponse deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
            Log.d(LOG_TAG, "deserialize");
            if (json == null) {
                return null;
            }
            JsonObject jsonObject = json.getAsJsonObject();
            Log.d(LOG_TAG, "deserialize jsonObject" + jsonObject);
            if (jsonObject == null) {
                return null;
            }
            final JsonResponse jsonParser = new JsonResponse();

            /**
             * Parse total products in JSON
             */
            JsonElement jsonTotalProducts = jsonObject.get(JsonTags.totalProducts);
            if (jsonTotalProducts != null) {
                String totalProducts = jsonTotalProducts.getAsString();
                jsonParser.mTotalProducts = totalProducts;
            }
            Log.d(LOG_TAG, "deserialize jsonParser.mTotalProducts" + jsonParser.mTotalProducts);
            /**
             * Parse page number in JSON
             */
            JsonElement jsonPageNumber = jsonObject.get(JsonTags.pageNumber);
            if (jsonPageNumber != null) {
                String pageNumber = jsonPageNumber.getAsString();
                jsonParser.mPageNumber = pageNumber;
            }

            /**
             * Parse page Size in JSON
             */
            JsonElement jsonPageSize = jsonObject.get(JsonTags.pageSize);
            if (jsonPageSize != null) {
                String pageSize = jsonPageSize.getAsString();
                jsonParser.mPageSize = pageSize;
            }

            /**
             * Parse Status code in JSON
             */
            JsonElement jsonStatusCode = jsonObject.get(JsonTags.statusCode);
            if (jsonStatusCode != null) {
                String statusCode = jsonStatusCode.getAsString();
                jsonParser.mStatusCode = statusCode;
            }

            /**
             * Parse products array
             */
            ProductInfo[] products = context.deserialize(
                    jsonObject.get(JsonTags.products),
                    ProductInfo[].class);
            jsonParser.mProductsArray = products;
            return jsonParser;
        }
    }

}
