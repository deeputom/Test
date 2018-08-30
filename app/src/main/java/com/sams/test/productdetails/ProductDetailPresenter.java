package com.sams.test.productdetails;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sams.test.data.IProductRepository;
import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.productinfojson.ProductInfo;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Presenter for the Product Details
 * This presenter is purely dump and acts just as an interface to Model to get the product item
 */
public class ProductDetailPresenter implements IProductDetailContract.Presenter {
    private static final String LOG_TAG = "ProductDetailPresenter";
    private IProductRepository mProductsRepository;
    private static IProductDetailContract.Presenter INSTANCE;

    private ProductDetailPresenter(@NonNull IProductRepository productsRepository) {
        mProductsRepository = checkNotNull(productsRepository,
                "Product Repository cannot be null");
    }

    public static IProductDetailContract.Presenter getInstance(@NonNull IProductRepository productsRepository) {
        if (INSTANCE == null) {
            INSTANCE = new ProductDetailPresenter(productsRepository);
        }
        return INSTANCE;
    }

    @Override
    public ProductInfo get(int index) {
        return mProductsRepository.loadProduct(index);
    }

    @Override
    public void start() {

    }
}
