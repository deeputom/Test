package com.sams.test.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.data.productinfojson.local.Database;
import com.sams.test.data.productinfojson.local.SQLiteStrorage;
import com.sams.test.data.productinfojson.remote.JsonDownload;
import com.sams.test.json.IJsonParser;
import com.sams.test.json.JsonParser;
import com.sams.test.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

/**
 *  Model of MVP pattern
 *  Repository for getting json response product array from server.
 *  Strategy is like this
 *  1. check in local cache
 *  2. check in SQLite DB
 *  3. download from server
 */

public class ProductsRepository implements IProductRepository {
    private static final String LOG_TAG  = "ProductsRepository";
    /**
     * Local source for the products storage, SQLite is used
     */
    private IProductInfoSource mProductLocalSource;
    /**
     * Fectes the products from remote url
     */
    private IProductInfoSource mRemoteDataSource;
    /**
     * local cache for the products
     * TODO right now it stores all the products, local cache should have only a fixed length
     * like 200.
     */
    private List<ProductInfo> mProductInfoCache;
    /**
     * Total count of the product, that we know so far.
     */
    private int mTotalProductCount;
    /**
     * current number of products that we have
     */
    private int mCurrCount;
    /**
     * Static instance of repository
     */
    private static ProductsRepository INSTANCE;
    /**
     * Application context
     * TODO store a weak reference of context so that memory can be reclaimed
     */
    private Context mContext;

    private ProductsRepository(Context ctx, IProductInfoSource local,
                               IProductInfoSource remote) {
        mContext = ctx;
        mProductInfoCache = new ArrayList<>();
        mProductLocalSource = local;
        mRemoteDataSource = remote;
    }

    public static IProductRepository getInstance(Context ctx, IProductInfoSource local,
                                                 IProductInfoSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsRepository(ctx, local, remote);
        }
        return INSTANCE;
    }
    /**
     * Refresh clears local cache and SQLite DB
     * Forces server get from next fetch
     */
    @Override
    public void invalidate() {
        mProductInfoCache.clear();
        mCurrCount = 0;
        mTotalProductCount = 0;
    }

    /**
     * Assumption, requests comes in ascending order
     * @param startingIndex starting index of the product to be loaded
     * @param callback returns list of products when the starting index passed
     */
    @Override
    public void loadProducts(@NonNull final int startingIndex,
                             final @NonNull IProductInfoSource.LoadProductsCallback callback) {
        /**
         * case 1, check in cache
         */
        if (startingIndex <= mProductInfoCache.size()-1)  {
            callback.onProductsLoaded(mProductInfoCache);
            return;
        }

        /**
         * Case 2: check in persistent storage
         */

        /**
         * Case 3: get from remote
         */
        mRemoteDataSource.loadProducts(startingIndex, new IProductInfoSource.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<ProductInfo> products) {
                Log.d(LOG_TAG, "onProductsLoaded startingIndex: " + startingIndex);
                if ((false == mProductInfoCache.isEmpty())
                        && (getTotalProductCount() == mProductInfoCache.size())) {
                    // Max products already downloaded, nothing more in server
                    Log.i(LOG_TAG, "Already loaded max products");
                    callback.onProductsLoaded(mProductInfoCache);
                    return;

                }
                int totalToCopy = startingIndex +  products.size();
                for (int count = 0, i = startingIndex; i< totalToCopy; i++, count++) {
                    mCurrCount++;
                    mProductInfoCache.add(i, products.get(count));
                }
                callback.onProductsLoaded(mProductInfoCache);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(LOG_TAG, "onDataNotAvailable");
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public int getTotalProductCount() {
        return mRemoteDataSource.getTotalProducts();
    }
}
