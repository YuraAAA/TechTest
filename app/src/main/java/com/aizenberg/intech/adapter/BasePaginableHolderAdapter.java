package com.aizenberg.intech.adapter;

import android.content.Context;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class BasePaginableHolderAdapter<T, VH extends BaseViewHolder> extends BaseHolderAdapter<T, VH> {

    private boolean stopped;
    public static final int LIMIT = 20;
    private int offset;

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isStopped() {
        return stopped;
    }

    public BasePaginableHolderAdapter(Context context) {
        super(context);
    }

    public int getOffset() {
        return getItemCount();
    }
}
