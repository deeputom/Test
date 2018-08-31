package com.sams.test.data.source.local;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.data.productinfojson.local.Database;
import com.sams.test.data.productinfojson.local.IProductInfoDao;
import com.sams.test.data.productinfojson.local.SQLiteStrorage;
import com.sams.test.util.SingleExecutors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by deepu on 8/25/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProductsLocalDataSourceTest {
    private SQLiteStrorage mProductsLocalDataSource;
    private Database mDatabase;
    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                Database.class)
                .build();
        IProductInfoDao productsDao = mDatabase.productsDao();

        // Make sure that we're not keeping a reference to the wrong instance.
        mProductsLocalDataSource = SQLiteStrorage.getInstance(new SingleExecutors(), productsDao);
        try {
            saveProdcut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Before
    public void getInstance() throws Exception {
    }

    @Test
    public void getProducts() throws Exception {
        mProductsLocalDataSource.loadProducts(10, new IProductInfoSource.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<ProductInfo> productInfoList) {
                System.out.println(productInfoList.size());
                assertThat(productInfoList.size(), is(10));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void getProduct() throws Exception {
    }

    @Test
    public void saveProdcut() throws Exception {
        for (int i = 0; i< 10; i++ ) {
            ProductInfo productInfo = new ProductInfo(""+i, "url"+i, "shortDesc"+i,
                    "longDesc"+i, ""+i, ""+i, ""+i, true, "name"+i);
            mProductsLocalDataSource.saveProdcut(productInfo);
        }

    }

    @Test
    public void clearInstance() throws Exception {
    }

}