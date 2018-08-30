package com.sams.test.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sams.test.R;
import com.sams.test.data.productinfojson.ProductInfo;
import com.sams.test.productdetails.ProductDetailActivity;

import java.util.List;


/**
 * This class is responsible for displaying the product list items.
 * Swipe up refresh pulls more
 * Refresh menu invalidates all caches and database and loads fresh from server
 */

public class ProductListFragment extends Fragment implements IProductContract.View,
        IProductSelectedListner {
    private static final String LOG_TAG = "ProductListFragment";

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private ProductListAdapter mAdapter;
    private boolean mLoading;

    private ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private static final int VISIBLE_TRESHOLD = 5;

    private IProductContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetVariables();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /**
     * Get the uptodate information of list of products from presenter
     * @param products
     */
    @Override
    public void showProducts(List<ProductInfo> products) {
        Log.d(LOG_TAG, "showProducts: " + products.size());
        hideErrorText();
        // update every time when a new invalidate is happened.
        mAdapter.setTotalCount(mPresenter.getTotalProdutCount());
        mAdapter.setData(products);
        mAdapter.notifyDataSetChanged();
        mLoading = false;
        int lastVisibleItem = mLayoutManager
                .findLastVisibleItemPosition();
        if (lastVisibleItem > products.size()) {
            // user scrolled more, download again
            //loadMore();
        }
    }

    @Override
    public void onError(String error) {
        Log.d(LOG_TAG, "onError: ");
        if (isActive()) {
            if (mAdapter.getCurrCount() > 0) {
                Log.d(LOG_TAG, "show toast: ");
                // if already products are there in the list, show toast.
                Toast.makeText(getActivity().getApplicationContext(),
                        "Error fetching products", Toast.LENGTH_LONG).show();
            } else {
                mErrorTextView.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                resetVariables();
                mPresenter.invalidate();
                mAdapter.invalidate();
                loadMore();
                break;
            case R.id.about_me:
                Intent intent = new Intent(getActivity(), AboutMeActivity.class);
                getActivity().startActivity(intent);
                break;
        }
        return true;
    }

    private void resetVariables() {
        mLoading = false;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.product_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.products_recycler_view);
        mErrorTextView = (TextView)  rootView.findViewById(R.id.error_text);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ProductListAdapter(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        int lastVisibleItem = mLayoutManager
                                .findLastVisibleItemPosition();
                        Log.d(LOG_TAG, "onScrolled: lastVisibleItem: " + lastVisibleItem);
                        Log.d(LOG_TAG, "onScrolled: mLoading: " + mLoading);
                        Log.d(LOG_TAG, "onScrolled: getCurrCount: " + mAdapter.getCurrCount());
                        Log.d(LOG_TAG, "onScrolled: mAdapter.isAllItemsDisplayed(): " + mAdapter.isAllItemsDisplayed());
                        if ((false == mLoading) // Not loading
                                && (false == mAdapter.isAllItemsDisplayed()) // Not fully downloaded
                                && (mAdapter.getCurrCount()-1 < (lastVisibleItem + VISIBLE_TRESHOLD))) { // User scrolling more
                            // End has been reached, load more
                            loadMore();
                        }
                    }
                });

        mRecyclerView.setAdapter(mAdapter);
        // pass the activity listener to adapter
        mAdapter.setOnProductClickListner((IProductSelectedListner) this);
        // Set up progress indicator
        mSwipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        mSwipeRefreshLayout.setScrollUpChild(mRecyclerView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Read again to see if there are more prodcuts
                // make sure user has not refreshed before we start loading
                loadMore();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    private void hideErrorText() {
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void loadMore() {
        Log.d(LOG_TAG, "loadMore from: " + mAdapter.getCurrCount());
        hideErrorText();
        mPresenter.loadProducts(true, mAdapter.getCurrCount());
        mLoading = true;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(IProductContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onProductSelected(ProductInfo productInfo) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtras(productInfo.getAsBundle());
        getActivity().startActivity(intent);
    }


}
