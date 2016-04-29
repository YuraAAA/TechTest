package com.aizenberg.intech.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aizenberg.intech.R;


/**
 * Created by Yuriy Aizenberg
 */
public class CustomToolbarController {

    private ImageView leftImageView;
    private ImageView rightImageView;
    private View leftContainer;
    private View rightContainer;
    private TextView centerTextView;
    private View customView;
    private Context context;
    private Toolbar toolbar;
    private View defaultHeaderView;

    public CustomToolbarController(Context context, Toolbar toolbar) {
        this.context = context;
        this.toolbar = toolbar;
        resetView();
    }

    private int resolveColor(int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    public void setToolbarColor(int colorId, int textColorId) {
        if (defaultHeaderView != null) {
            defaultHeaderView.setBackgroundColor(resolveColor(colorId));
        }
        if (centerTextView != null) {
            centerTextView.setTextColor(resolveColor(textColorId));
        }
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    public View getDefaultHeaderView() {
        return defaultHeaderView;
    }

    public void resetToolbarColor() {
        setToolbarColor(android.R.color.white, android.R.color.black);
    }

    public void setLeftImageView(Drawable drawable, View.OnClickListener onClickListener) {
        leftImageView.setImageDrawable(drawable);
        leftContainer.setOnClickListener(onClickListener);
    }

    public void setRightImageView(Drawable drawable, View.OnClickListener onClickListener) {
        rightImageView.setImageDrawable(drawable);
        rightContainer.setOnClickListener(onClickListener);
    }

    public void setCenterTextView(@StringRes int resourceId) {
        setCenterTextView(context.getString(resourceId));
    }

    public void setCenterTextView(String text) {
        centerTextView.setText(text);
    }


    private void resetView() {
        toolbar.removeAllViews();

        defaultHeaderView = LayoutInflater.from(context).inflate(R.layout.default_header_bar, null);
        toolbar.addView(defaultHeaderView);
        leftImageView = (ImageView) defaultHeaderView.findViewById(R.id.img_default_header_left);
        leftContainer = defaultHeaderView.findViewById(R.id.container_default_header_left);
        rightImageView = (ImageView) defaultHeaderView.findViewById(R.id.img_default_header_right);
        rightContainer = defaultHeaderView.findViewById(R.id.container_default_header_right);
        centerTextView = (TextView) defaultHeaderView.findViewById(R.id.txt_default_header);
    }


}
