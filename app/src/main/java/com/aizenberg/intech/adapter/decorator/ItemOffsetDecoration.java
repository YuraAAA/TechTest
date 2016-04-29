package com.aizenberg.intech.adapter.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Yuriy Aizenberg
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;
    private int count;

    public ItemOffsetDecoration(int offset, int count) {
        this.offset = offset;
        this.count = count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
//        Boolean left = isLeft(childAdapterPosition);
//        if (left != null) {
//            if (!left) {
//                outRect.left = offset / 2;
//                outRect.right = offset;
//            } else {
//                outRect.right = offset / 2;
//                outRect.left = offset;
//            }
//        } else {
            outRect.left = offset;
            outRect.right = offset;
//        }
        outRect.bottom = offset;
        if (childAdapterPosition < count) {
            outRect.top = offset;
        }

    }

    private Boolean isLeft(int position) {
        if (count != 2) return null;
        return position % 2 == 0;
    }
}
