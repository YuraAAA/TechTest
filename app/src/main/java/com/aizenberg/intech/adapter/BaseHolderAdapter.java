package com.aizenberg.intech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.support.common.collection.NullExcludeSafetyList;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class BaseHolderAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> list = new NullExcludeSafetyList<>();
    private IItemClickListener<T> itemClickListener;
    private Picasso picasso;
    private Context context;

    public void setItemClickListener(IItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    protected View inflate(ViewGroup parent, int resource) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    }

    protected void setupClickListener(View view, final T data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(data);
                }
            }
        });
    }

    public Picasso getPicasso() {
        return picasso;
    }

    public BaseHolderAdapter(Context context) {
        this.context = context;
        picasso = Picasso.with(context);
    }

    public Context getContext() {
        return context;
    }

    public void setData(List<T> data) {
        setData(data, false);
    }

    protected T getItemById(int position) {
        return list.get(position);
    }

    public void setData(List<T> data, boolean addToStart) {
        if (addToStart) {
            list.addAll(0, data);
        } else {
            list.addAll(data);
        }
        beforeDataSetChanged();
        notifyDataSetChanged();
    }

    protected void beforeDataSetChanged() {}

    public BaseHolderAdapter<T, VH> clear() {
        list.clear();
        return this;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean hasData() {
        return !isEmpty();
    }

    public interface IItemClickListener<T> {
        void onItemClick(T data);
    }
}
