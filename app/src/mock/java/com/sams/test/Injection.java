package com.sams.test;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sams.test.util.AppExecutors;
import com.sams.test.data.productinfojson.IProductInfoSource;
import com.sams.test.data.IProductRepository;
import com.sams.test.data.ProductsRepository;
import com.sams.test.data.productinfojson.local.Database;
import com.sams.test.data.productinfojson.local.SQLiteStrorage;
import com.sams.test.data.productinfojson.remote.JsonDownload;
import com.sams.test.json.IJsonParser;
import com.sams.test.json.JsonParser;
import com.sams.test.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by deepu on 8/24/2018.
 */

public class Injection {
    public static IProductRepository provideRepository(@NonNull Context context) {
        checkNotNull(context);
        AppExecutors mAppExcuters;
        IJsonParser mJsonParser = new JsonParser();
        mAppExcuters = new AppExecutors();
        Database database = Database.getInstance(context);
        return ProductsRepository.getInstance(context, FakeRemoteDataSource.getInstance(),
                SQLiteStrorage.getInstance(new AppExecutors(),
                        database.productsDao()));
    }
}
