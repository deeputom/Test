package com.sams.test.products;

/**
 * Created by deepu on 8/24/2018.
 */

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sams.test.R;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.databinding.ProductDetailsBinding;

/**
 * Displays Product details screen.
 * It uses databinding to display the products from {@link ProductInfo}
 */
public class ProductDetailFragment extends Fragment {
    public static final String ARG_POSITION = "position";

    private int mCurrentPosition = -1;
    private ProductDetailsBinding mBinding;
    private ImageView mImageView;
    private ProductInfo mProductInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ViewConstants.PROD_LIST_POSITION);
        }
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.product_details, container, false);
        View view = mBinding.getRoot();
        mImageView = view.findViewById(R.id.product_image_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mProductInfo != null) {
            mBinding.setProduct(mProductInfo);
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(this)
                .load(mProductInfo.getImgurl()).apply(ViewConstants.GLIDE_REQUEST_OPTIONS)
                .into(mImageView);
    }

    public void showProductDetails(@NonNull ProductInfo productInfo) {
        mProductInfo = productInfo;
        if (isAdded()) {
            mBinding.setProduct(mProductInfo);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ViewConstants.PROD_LIST_POSITION, mCurrentPosition);
    }
}
