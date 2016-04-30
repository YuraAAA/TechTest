package com.aizenberg.intech.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Yuriy Aizenberg
 */
public class PositionRecyclerView extends RecyclerView {

    private int currentPosition;

    public PositionRecyclerView(Context context) {
        super(context);
    }

    public PositionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PositionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
