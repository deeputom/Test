package com.sams.test.data.productinfojson.remote;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.json.IJsonParser;
import com.sams.test.json.JsonParser;
import com.sams.test.util.AppExecutors;
import com.sams.test.util.SingleExecutors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by deepu on 8/26/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class JsonDownloadTest {
    private static final String LOG_TAG = "JsonDownloadTest";
    private IProductInfoSource mJsonDownload;
    private AppExecutors mAppExecutors;
    private IJsonParser mJsonParser;
    @Before
    public void setup() {
        mJsonParser = new JsonParser();
        mJsonDownload = JsonDownload.getInstance(new SingleExecutors(), mJsonParser,
                InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void loadProductsTest() {
        mJsonDownload.loadProducts(1, new IProductInfoSource.LoadProductsCallback() {
            @Override
            public void onProdcutsLoaded(List<ProductInfo> tasks) {
                Log.d(LOG_TAG, "loadProductsTest size: " + tasks.size());
                assertThat(tasks.size(), not(0));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }
}
