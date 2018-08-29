package com.sams.test.data;

import android.support.annotation.NonNull;

import com.sams.test.data.productinfojson.IProductInfoSource;

/**
 * Interface for getting the products from the repositories
 * can be used for dependency injection
 */

public interface IProductRepository {
    void invalidate();
    void loadProducts(@NonNull int startingIndex,
                             final @NonNull IProductInfoSource.LoadProductsCallback callback);
    int getTotalProductCount();
}
