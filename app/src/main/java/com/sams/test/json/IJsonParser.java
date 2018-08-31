package com.sams.test.json;

import com.sams.test.data.productinfojson.ProductInfo;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Interface for parsing json file
 */

public interface IJsonParser {
    boolean parse(InputStream file);
    List<ProductInfo> getProducts();
    int getPageIndex();
    int getTotalProductCount();
}
