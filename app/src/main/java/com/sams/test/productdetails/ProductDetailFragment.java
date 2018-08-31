package com.sams.test.productdetails;


/**
 * This fragment is responsible for displaying product details.
 * it is instantiated from the {@link @{@link com.sams.test.productdetails.ProductDetailActivity}}
 */

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sams.test.R;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.databinding.ProductDetailsBinding;
import com.sams.test.ViewConstants;

/**
 * Displays Product details screen.
 * It uses databinding to display the products from {@link ProductInfo}
 */
public class ProductDetailFragment extends Fragment {
    public static final String LOG_TAG = "ProductDetailFragment";

    private ProductDetailsBinding mBinding;
    private ImageView mImageView;
    private ProductInfo mProductInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.product_details, container, false);
        View view = mBinding.getRoot();
        mImageView = view.findViewById(R.id.product_image_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
        if (mProductInfo != null) {
            mBinding.setProduct(mProductInfo);
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        if (mProductInfo != null) {
            Glide.with(this)
                    .load(mProductInfo.getImgurl()).apply(ViewConstants.GLIDE_REQUEST_OPTIONS)
                    .into(mImageView);
        }

    }

    public void showProductDetails(@NonNull ProductInfo productInfo) {
        Log.d(LOG_TAG, "onActivityCreated");
        mProductInfo = productInfo;
        if (isAdded() && (mProductInfo != null)) {
            mBinding.setProduct(mProductInfo);
        }
    }

}
