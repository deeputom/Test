package com.sams.test.products;


/**
 * Defining callback that is called when a prodcut item is selected from ProductListAdapter
 * @see {@link ProductListAdapter}
 */

public interface IProductSelectedListner {
    void onProductSelected(int position, int currTotSize);
}
