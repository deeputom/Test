package com.sams.test.products;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sams.test.R;
import com.sams.test.ViewConstants;
import com.sams.test.data.productinfojson.ProductInfo;

import java.util.List;

/**
 * Recycler view adapter for product items
 * image view is displayed using Glide library
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.CustomViewHolder> {
    private final String LOG_TAG = "ProductListAdapter";
    private List<ProductInfo> mProductInfoList;
    private Fragment mCallingFragment;
    private int mTotalCount;
    private IProductSelectedListner mProdListner;
    private static final int THUMB_NAIL_WIDTH = 100;
    private static final int THUMB_NAIL_HEIGHT = 200;
    private RequestOptions mRequestOptionsAll;
    private static final RequestOptions REQUEST_OPTIONS_THUMB_NAIL = new RequestOptions().
                                        override(THUMB_NAIL_WIDTH, THUMB_NAIL_HEIGHT);

    public ProductListAdapter(Fragment fragment) {
        Log.d(LOG_TAG, "ProductListAdapter");
        mCallingFragment = fragment;
        mTotalCount = 0;
        mRequestOptionsAll = new RequestOptions();
        mRequestOptionsAll.diskCacheStrategy(DiskCacheStrategy.ALL);
        mRequestOptionsAll.fitCenter();

    }

    public void setOnProductClickListner(
            IProductSelectedListner productSelectedListner) {
        mProdListner = productSelectedListner;
    }

    public void setData(List<ProductInfo> productInfos) {
        mProductInfoList = productInfos;
    }

   @Override
    public ProductListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
       Log.d(LOG_TAG, "onCreateViewHolder");
       ProductListAdapter.CustomViewHolder viewHolder;
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, null);
       viewHolder = new CustomViewHolder(view);
       return viewHolder;
    }

    /**
     * start from -1 because fragment fetches from the next item
     * @return
     */
    public int getCurrCount() {
        return (mProductInfoList == null)? 0 : mProductInfoList.size();
    }

    public boolean isAllItemsDisplayed() {
        return (mProductInfoList == null) ? false : (mTotalCount == mProductInfoList.size());
    }

    @Override
    public void onBindViewHolder(ProductListAdapter.CustomViewHolder viewHolder, final int position) {

        if ((mProductInfoList == null) || position > mProductInfoList.size()-1) {
            /**
             * Data is not available it, show loading in rating view as it is in the center.
             * Hide other views
             */
            viewHolder.productImageView.setVisibility(View.INVISIBLE);
            viewHolder.productShortDescView.setVisibility(View.INVISIBLE);
            viewHolder.productPriceView.setVisibility(View.INVISIBLE);
            // product rating is in the middle
            viewHolder.productRating.setText("loading...");
            return;
        }

        /**
         * Data is available, show the products
         */
        viewHolder.productImageView.setVisibility(View.VISIBLE);
        viewHolder.productShortDescView.setVisibility(View.VISIBLE);
        viewHolder.productPriceView.setVisibility(View.VISIBLE);

        /**
         * Add the listner when the list item is selected so that we can show the full product
         * details in {@link com.sams.test.productdetails.ProductDetailFragment}
         */
        final ProductInfo productInfo=  mProductInfoList.get(mProductInfoList.size()-1 - position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProdListner.onProductSelected(mProductInfoList.size()-1 - position,
                        mProductInfoList.size());
            }
        });

        /**
         * Display thumbnail image
         */
        Glide.with(mCallingFragment)
                .load(productInfo.getImgurl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(mCallingFragment)
                        .load(productInfo.getImgurl()).apply(REQUEST_OPTIONS_THUMB_NAIL))
                .apply(ViewConstants.GLIDE_REQUEST_OPTIONS)
                .into(viewHolder.productImageView);


        /**
         * Display price, rating and short description
         */
        viewHolder.productPriceView.setText(productInfo.getPrice());
        // Todo dont hard code "Rating", have it in strings.xml for localization
        viewHolder.productRating.setText("Rating: " + productInfo.getRating());
        Spanned text = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) ?
                Html.fromHtml(productInfo.getShortdescription(), Html.FROM_HTML_MODE_COMPACT) :
                Html.fromHtml(productInfo.getShortdescription());
        viewHolder.productShortDescView.setText(text);
    }

    @Override
    public int getItemCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public void invalidate() {
        if (mProductInfoList != null) {
            mProductInfoList.clear();
        }
        mTotalCount = 0;
        notifyDataSetChanged();
    }

    /**
     * Items in the recycler view
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView productImageView;
        protected TextView productPriceView;
        protected TextView productShortDescView;
        protected TextView productRating;

        public CustomViewHolder(View view) {
            super(view);
            this.productImageView = (ImageView) view.findViewById(R.id.product_thumbnail);
            this.productShortDescView = (TextView) view.findViewById(R.id.product_short_description);
            this.productPriceView = (TextView) view.findViewById(R.id.product_price);
            this.productRating = (TextView) view.findViewById(R.id.product_rating);
        }
    }
}