/*
 * Copyright 2016, The Android Open Source Project
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

package com.sams.test.products;

import com.sams.test.BasePresenter;
import com.sams.test.BaseView;
import com.sams.test.data.productinfojson.ProductInfo;

import java.util.List;

/**
 * This specifies the contract between the Product list view and the presenter.
 */
public interface IProductContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(final boolean active);
        boolean isActive();
        void showProducts(List<ProductInfo> products);
        void onError(String error);
    }

    interface Presenter extends BasePresenter {
        void getImageDetails(int index);
        int getTotalProdutCount();
        void loadProducts(final boolean showLoadingUI, int index);
        void invalidate();
    }
}
