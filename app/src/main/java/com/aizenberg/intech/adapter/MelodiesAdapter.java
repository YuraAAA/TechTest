package com.aizenberg.intech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aizenberg.intech.R;
import com.aizenberg.intech.core.model.Melody;
import com.aizenberg.intech.view.PositionRecyclerView;
import com.aizenberg.support.utils.StringUtils;
import com.aizenberg.support.viewinjector.annotation.Id;

/**
 * Created by Yuriy Aizenberg
 */
public class MelodiesAdapter extends BasePaginableHolderAdapter<Melody, MelodiesAdapter.MelodiesHolder> {

    private PositionRecyclerView recyclerView;


    public MelodiesAdapter(Context context) {
        super(context);
    }

    @Override
    public MelodiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MelodiesHolder(inflate(parent, R.layout.melody_item));
    }

    @Override
    public void onBindViewHolder(MelodiesHolder holder, int position) {
        Melody melody = getItemById(position);
        String pictureUrl = melody.getPictureUrl();
        if (!StringUtils.isEmpty(pictureUrl)) {
            getPicasso().load(pictureUrl).fit().centerCrop().into(holder.imgArtist);
        }
        holder.txtAlbumName.setText(melody.getTitle());
        holder.txtAuthorName.setText(melody.getArtist());
        savePosition(position);
        setupClickListener(holder.rootView, melody);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = (PositionRecyclerView) recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    private void savePosition(int position) {
        if (recyclerView != null) {
            recyclerView.setCurrentPosition(position);
        }
    }

    static class MelodiesHolder extends BaseViewHolder {

        @Id(R.id.img_artist)
        private ImageView imgArtist;
        @Id(R.id.txt_album_name)
        private TextView txtAlbumName;
        @Id(R.id.txt_artist_name)
        private TextView txtAuthorName;

        public MelodiesHolder(View itemView) {
            super(itemView);
        }
    }

}
