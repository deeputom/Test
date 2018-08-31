package com.sams.test.products;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sams.test.Injection;
import com.sams.test.R;
/**
 * Main activity holding the fragment showing the list of products
 * @see {@link ProductListFragment}
 */
public class ProductMainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProductMainActivity";
    private ProductPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "OnCreate");
        setContentView(R.layout.prodcut_container);
        Log.d(LOG_TAG, "OnCreate productListFragment");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ProductListFragment productListFragment = new ProductListFragment();

        // add fragment to the fragment container layout
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                productListFragment).commit();
        mPresenter = new ProductPresenter(Injection.provideRepository(getApplicationContext()),
                productListFragment);
    }
}

