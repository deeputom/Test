package com.sams.test.products;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.sams.test.data.IProductRepository;
import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.ProductsRepository;
import com.sams.test.data.productinfojson.ProductInfo;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Presenter between list view and model
 * intermediate between view and model
 */

public class ProductPresenter implements IProductContract.Presenter {
    private static final String LOG_TAG = "ProductDetailPresenter";
    private IProductRepository mProductsRepository;
    private IProductContract.View mProductView;

    public ProductPresenter(@NonNull IProductRepository tasksRepository,
                            @NonNull IProductContract.View productView) {
        mProductsRepository = checkNotNull(tasksRepository,
                "Product Repository cannot be null");
        mProductView = checkNotNull(productView, "product view cannot be null!");
        mProductView.setPresenter(this);
    }

    @Override
    public void loadProducts(final boolean showLoadingUI, int index) {
        Log.d(LOG_TAG, "loadProducts: " + showLoadingUI);
        if (showLoadingUI) {
            mProductView.setLoadingIndicator(true);
        }
        mProductsRepository.loadProducts(index, new IProductInfoSource.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<ProductInfo> products) {
                boolean isViewActive = mProductView.isActive();
                Log.d(LOG_TAG, "onProductsLoaded: " + isViewActive);
                // The view may not be able to handle UI updates anymore
                if (!isViewActive) {
                    return;
                }
                if (showLoadingUI) {
                    mProductView.setLoadingIndicator(false);
                }
                mProductView.showProducts(products);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(LOG_TAG, "onDataNotAvailable: ");
                if (showLoadingUI) {
                    mProductView.setLoadingIndicator(false);
                }
                // TODO pass the error string from source
                mProductView.onError("");
            }
        });
    }

    @Override
    public void invalidate() {
        mProductsRepository.invalidate();
    }

    @Override
    public void start() {
       loadProducts(true, 0);
    }

    @Override
    public void getImageDetails(int index) {

    }

    @Override
    public int getTotalProdutCount() {
        return mProductsRepository.getTotalProductCount();
    }

}
