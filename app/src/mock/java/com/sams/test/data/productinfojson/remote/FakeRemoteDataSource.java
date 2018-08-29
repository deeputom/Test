package com.sams.test.data.productinfojson.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;


import com.google.common.collect.Lists;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.data.productinfojson.IProductInfoSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class FakeRemoteDataSource implements IProductInfoSource {

    private static FakeRemoteDataSource INSTANCE;

    private static final Map<String, ProductInfo> PRODUCTS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeRemoteDataSource() {
    }

    public static FakeRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeRemoteDataSource();
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadProductsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void loadProducts(@NonNull final int startingIndex,
                             @NonNull final IProductInfoSource.LoadProductsCallback callback) {
        //callback.
    }

    @Override
    public int getTotalProducts() {
        return 500;
    }


    @Override
    public void loadProduct(@NonNull String productId, @NonNull LoadProductCallback callback) {

    }
}
