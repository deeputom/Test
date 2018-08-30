/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.sams.test.productdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sams.test.Injection;
import com.sams.test.R;
import com.sams.test.ViewConstants;
import com.sams.test.data.ProductsRepository;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.productdetails.ProductDetailFragment;

/**
 * Displays Product details screen.
 * @see {@link ProductDetailFragment}
 */
public class ProductDetailActivity extends AppCompatActivity {

    public static final String LOG_TAG = "ProductDetailActivity";

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Current position of the list item selected by user
     */
    private int mCurrentPosition;

    /**
     * current total size displayed in adapter list view
     */
    private int mCurrentTotalSize;
    private IProductDetailContract.Presenter mProductDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.product_detailed_container);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        mProductDetailPresenter = ProductDetailPresenter.getInstance(Injection.provideRepository(this));
        mCurrentPosition = getIntent().getIntExtra(ViewConstants.PROD_LIST_POSITION, 0);
        mCurrentTotalSize = getIntent().getIntExtra(ViewConstants.CURR_TOTAL_SIZE, 0);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

     }

    /**
     * Swipe view works only from the starting postion to the end where the list is loaded.
     * Swipe cannot go back the place where the swipe is started.
     * Also it cannot load the the items that is not yet downloaded
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ProductDetailFragment productDetailFragment = new ProductDetailFragment();
            int adjustedPos = (mCurrentPosition >= position) ? mCurrentPosition-position
                    : mCurrentPosition+position;
            productDetailFragment.showProductDetails(
                    mProductDetailPresenter.get(adjustedPos));
            return productDetailFragment;
        }

        @Override
        public int getCount() {
            return mCurrentTotalSize;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}