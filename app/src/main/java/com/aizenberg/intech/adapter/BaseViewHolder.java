package com.aizenberg.intech.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aizenberg.support.viewinjector.ViewInjector;


/**
 * Created by Yuriy Aizenberg
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    protected View rootView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        ViewInjector.reflect(this, itemView);
    }
}
