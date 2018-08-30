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

package com.sams.test.productdetails;

import com.sams.test.BasePresenter;
import com.sams.test.BaseView;
import com.sams.test.data.productinfojson.ProductInfo;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 * This interface is just to be a dummy between the view and model
 */
public interface IProductDetailContract {

    interface Presenter extends BasePresenter {
        // Only get the index that is already loaded.
        ProductInfo get(int index);
    }
}
