package com.aizenberg.intech.fragment.common;

/**
 * Created by Yuriy Aizenberg
 */

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 10;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private RecyclerView.LayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setmLinearLayoutManager(RecyclerView.LayoutManager mLinearLayoutManager) {
        this.mLinearLayoutManager = mLinearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        if (mLinearLayoutManager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) mLinearLayoutManager).findFirstVisibleItemPosition();
        } else if (mLinearLayoutManager instanceof GridLayoutManager) {
            firstVisibleItem = ((GridLayoutManager) mLinearLayoutManager).findFirstVisibleItemPosition();
        } else {
            throw new IllegalArgumentException("This layout manager doesn't support");
        }

        if ((totalItemCount - visibleItemCount)
                <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
