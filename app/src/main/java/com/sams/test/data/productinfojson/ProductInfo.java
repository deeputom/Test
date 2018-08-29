package com.sams.test.data.productinfojson;

/**
 * Prodcut info, all the parameters are String for simplicty of Sample app.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.sams.test.json.JsonTags;

import java.lang.reflect.Type;

/**
 * Immutable model class for a ProductInfo.
 */
@Entity(tableName = "ProductInfo")
public final class ProductInfo {
    private static final String LOG_TAG = "ProductInfo";
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName(JsonTags.productId)
    private final String id;

    @Nullable
    @ColumnInfo(name = "imgurl")
    @SerializedName(JsonTags.productImage)
    private final String imgurl;

    @Nullable
    @ColumnInfo(name = "shortdescription")
    @SerializedName(JsonTags.shortDescription)
    private final String shortdescription;

    @Nullable
    @ColumnInfo(name = "longdescription")
    @SerializedName(JsonTags.longDescription)
    private final String longdescription;

    @Nullable
    @ColumnInfo(name = "price")
    @SerializedName(JsonTags.price)
    private final String price;

    @Nullable
    @ColumnInfo(name = "rating")
    @SerializedName(JsonTags.reviewRating)
    private final String rating;

    @Nullable
    @ColumnInfo(name = "reviewcount")
    @SerializedName(JsonTags.reviewCount)
    private final String reviewcount;

    @Nullable
    @ColumnInfo(name = "instock")
    @SerializedName(JsonTags.inStock)
    private final boolean instock;

    @Nullable
    @ColumnInfo(name = "productname")
    @SerializedName(JsonTags.productName)
    private final String productname;

    /**
     * Use this constructor to create productInfo
     *
     * @param id      prodcut id
     * @param imgurl url of image
     * @param shortdescription   short description of product
     * @param longdescription  long description of product
     * @param price      price of product
     * @param rating  rating of product
     * @param reviewcount   reviewCount product
     * @param instock  is the item in stock
     * @param productname  name of product
     */
    public ProductInfo(@NonNull String id, @Nullable String imgurl,
                @Nullable String shortdescription, @Nullable String longdescription,
                       @Nullable String price, @Nullable String rating,
                       @Nullable String reviewcount, @Nullable boolean instock,
                       @Nullable String productname) {
        this.id = id;
        this.imgurl = imgurl;
        this.shortdescription = shortdescription;
        this.longdescription = longdescription;
        this.price = price;
        this.rating = rating;
        this.reviewcount = reviewcount;
        this.instock = instock;
        this.productname = productname;
    }

    public Bundle getAsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(JsonTags.productId, id);
        bundle.putString(JsonTags.productImage, imgurl);
        bundle.putString(JsonTags.shortDescription, shortdescription);
        bundle.putString(JsonTags.longDescription, longdescription);
        bundle.putString(JsonTags.price, price);
        bundle.putString(JsonTags.reviewRating, rating);
        bundle.putString(JsonTags.reviewCount, reviewcount);
        bundle.putBoolean(JsonTags.inStock, instock);
        bundle.putString(JsonTags.productName, productname);
        return bundle;
    }

    public static ProductInfo getFromBundle(Bundle bundle) {
        String id = bundle.getString(JsonTags.productId);
        String productImage = bundle.getString(JsonTags.productImage, "");
        String shortDescription = bundle.getString(JsonTags.shortDescription, "");
        String longDescription = bundle.getString(JsonTags.longDescription);
        String price = bundle.getString(JsonTags.price);
        String rating = bundle.getString(JsonTags.reviewRating);
        String reviewCount = bundle.getString(JsonTags.reviewCount);
        boolean inStock = bundle.getBoolean(JsonTags.inStock);
        String productName = bundle.getString(JsonTags.productName);
        ProductInfo productInfo = new ProductInfo(id, productImage,
                shortDescription, longDescription, price,
                rating, reviewCount, inStock, productName);
        return productInfo;
    }
    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getImgurl() {
        return imgurl;
    }

    @Nullable
    public String getShortdescription() {
        return shortdescription;
    }

    @Nullable
    public String getLongdescription() {

        return longdescription;
    }

    @Nullable
    public String getPrice() {
        return price;
    }

    @Nullable
    public String getRating() {
        return rating;
    }

    @Nullable
    public String getReviewcount() {
        return reviewcount;
    }

    @Nullable
    public boolean getInstock() {
        return instock;
    }

    @Nullable
    public String getProductname() {
        return productname;
    }

    public static class ProductInfoDeserializer implements JsonDeserializer<ProductInfo> {

        @Override
        public ProductInfo deserialize(JsonElement json, Type typeOfT,
                                                          JsonDeserializationContext context) throws JsonParseException {
            if (json == null) {
                return null;
            }
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject == null) {
                return null;
            }
            /**
             * Parse productId in "products"
             */
            JsonElement jsonProductID = jsonObject.get(JsonTags.productId);
            String productID = "";
            if (jsonProductID != null) {
                productID = jsonProductID.getAsString();
            }

            /**
             * Parse productName in "products"
             */
            String productName = "";
            JsonElement jsonProductName = jsonObject.get(JsonTags.productName);
            if (jsonProductName != null) {
                productName = jsonProductName.getAsString();
            }

            /**
             * Parse short description in "products"
             */
            String shortDescription = "";
            JsonElement jsonShortDescription = jsonObject.get(JsonTags.shortDescription);
            if (jsonShortDescription != null) {
                shortDescription = jsonShortDescription.getAsString();
            }

            /**
             * Parse long description in "products"
             */
            String longDescription = "";

            try {
                JsonElement jsonLongDescription = jsonObject.get(JsonTags.longDescription);
                if (jsonLongDescription != null) {
                    longDescription = jsonLongDescription.getAsString();
                }
            } catch ( JsonSyntaxException e) {
                Log.e(LOG_TAG, "JsonParsing exception of longDescription", e);
            }

            /**
             * Parse price description in "products"
             */
            String price = "";
            JsonElement jsonPrice = jsonObject.get(JsonTags.price);
            if (jsonPrice != null) {
                price = jsonPrice.getAsString();
            }

            /**
             * Parse product Image in "products"
             */
            String productImage = "";
            JsonElement jsonProductImage = jsonObject.get(JsonTags.productImage);
            if (jsonProductImage != null) {
                productImage = "https://mobile-tha-server.appspot.com" + jsonProductImage.getAsString();
            }

            /**
             * Parse review rating in "products"
             */
            String reviewRating = "";
            JsonElement jsonReviewRating = jsonObject.get(JsonTags.reviewRating);
            if (jsonReviewRating != null) {
                reviewRating = jsonReviewRating.getAsString();
            }

            /**
             * Parse review count in "products"
             */
            String reviewCount = "";
            JsonElement jsonReviewCount = jsonObject.get(JsonTags.reviewCount);
            if (jsonReviewCount != null) {
                reviewCount = jsonReviewCount.getAsString();
            }

            /**
             * Parse review count in "products"
             */
            boolean inStock = false;
            JsonElement jsonInStock = jsonObject.get(JsonTags.inStock);
            if (jsonInStock != null) {
                inStock = jsonInStock.getAsBoolean();
            }
            ProductInfo products = new ProductInfo(productID, productImage, shortDescription,
                    longDescription, price, reviewRating, reviewCount, inStock,productName );
            return products;
        }
    }
}
