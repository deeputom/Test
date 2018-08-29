/*
 * Copyright 2017, The Android Open Source Project
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

package com.sams.test.data.productinfojson.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sams.test.data.productinfojson.ProductInfo;

import java.util.List;

/**
 * Data Access Object for the Prodcuts table.
 */
@Dao
public interface IProductInfoDao {

    /**
     * Select all Products from the Products table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM ProductInfo")
    List<ProductInfo> getProducts();

    /**
     * Select a Product by index.
     *
     * @param productID the product ID.
     * @return the ProductInfo with productID.
     */
    @Query("SELECT * FROM ProductInfo WHERE id = :productID")
    ProductInfo getProductByIndex(String productID);

    /**
     * Insert a Product in the database. If the Product already exists, replace it.
     *
     * @param ProdcutInfo the product to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductInfo ProdcutInfo);

}
