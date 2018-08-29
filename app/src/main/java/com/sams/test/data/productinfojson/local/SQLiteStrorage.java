/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sams.test.data.productinfojson.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation of a data source as a db.
 */
public class SQLiteStrorage implements IProductInfoSource {

    private static final String LOG_TAG = "SQLiteStrorage";
    private static volatile SQLiteStrorage INSTANCE;

    private IProductInfoDao mDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private SQLiteStrorage(@NonNull AppExecutors appExecutors,
                           @NonNull IProductInfoDao productsDao) {
        mAppExecutors = appExecutors;
        mDao = productsDao;
    }

    public static SQLiteStrorage getInstance(@NonNull AppExecutors appExecutors,
                                             @NonNull IProductInfoDao productsDao) {
        if (INSTANCE == null) {
            synchronized (SQLiteStrorage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SQLiteStrorage(appExecutors, productsDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadProductsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void loadProducts(int startingIndex, @NonNull final LoadProductsCallback callback) {
        Log.d(LOG_TAG, "loadProducts");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<ProductInfo> prodcutInfos = mDao.getProducts();
                Log.d(LOG_TAG, "loadProducts size: " + prodcutInfos.size());
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (prodcutInfos.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onProductsLoaded(prodcutInfos);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public int getTotalProducts() {
        return 0;
    }

    /**
     * Note: {@link LoadProductCallback#onDataNotAvailable()} is fired if the {@link ProductInfo} isn't
     * found.
     */
    @Override
    public void loadProduct(@NonNull final String productId, @NonNull final LoadProductCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ProductInfo prodcutInfo = mDao.getProductByIndex(productId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (prodcutInfo != null) {
                            callback.onProdcutLoaded(prodcutInfo);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    public void saveProdcut(@NonNull final ProductInfo prodcutInfo) {
        Log.d(LOG_TAG, "saveProdcut");
        checkNotNull(prodcutInfo);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mDao.insertProduct(prodcutInfo);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }


    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}
