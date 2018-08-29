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
    public boolean parse(InputStream file);
    public List<ProductInfo> getProducts();
    public int getPageIndex();
    public int getTotalProductCount();
}
