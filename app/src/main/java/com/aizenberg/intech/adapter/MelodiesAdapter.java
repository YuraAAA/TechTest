package com.aizenberg.intech.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aizenberg.intech.R;
import com.aizenberg.intech.core.model.Melody;
import com.aizenberg.support.utils.StringUtils;
import com.aizenberg.support.viewinjector.annotation.Id;

/**
 * Created by Yuriy Aizenberg
 */
public class MelodiesAdapter extends BasePaginableHolderAdapter<Melody, MelodiesAdapter.MelodiesHolder> {


    public MelodiesAdapter(Context context) {
        super(context);
    }

    @Override
    public MelodiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MelodiesHolder(inflate(parent, R.layout.melody_item));
    }

    @Override
    public void onBindViewHolder(MelodiesHolder holder, int position) {
        Melody itemById = getItemById(position);
        String pictureUrl = itemById.getPictureUrl();
        if (!StringUtils.isEmpty(pictureUrl)) {
            getPicasso().load(pictureUrl).fit().centerCrop().into(holder.imgArtist);
        }
    }

    static class MelodiesHolder extends BaseViewHolder {

        @Id(R.id.img_artist)
        private ImageView imgArtist;

        public MelodiesHolder(View itemView) {
            super(itemView);
        }
    }

}
