package com.aizenberg.intech.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.aizenberg.intech.R;
import com.aizenberg.support.viewinjector.ViewInjector;
import com.aizenberg.support.viewinjector.annotation.Id;

/**
 * Created by Yuriy Aizenberg
 */
public class VolumeView extends RelativeLayout {

    @Id(R.id.volume_seek_bar)
    private VolumeSeekBar volumeSeekBar;
    @Id(R.id.volume_img)
    private ImageView volumeImage;

    private IVolumeChangeListener changeListener;

    public void setChangeListener(IVolumeChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public VolumeView(Context context) {
        super(context);
        init();
    }

    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.player_volume_view, this);
        ViewInjector.reflect(this, this);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                convertSeekProgressToVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setVolumeLevel(float volumeLevel) {
        volumeLevel = normalize(volumeLevel);
        if (volumeLevel == 0) {
            volumeImage.setImageResource(R.drawable.ic_volume_off_black_24dp);
        } else if (volumeLevel >= .9f) {
            volumeImage.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            volumeImage.setImageResource(R.drawable.ic_volume_down_black_24dp);
        }
        if (changeListener != null) {
            changeListener.onVolumeChanged(volumeLevel);
        }
    }

    public void block() {
        volumeSeekBar.setEnabled(false);
    }

    public void unblock() {
        volumeSeekBar.setEnabled(true);
    }

    private float normalize(float volumeLevel) {
        if (volumeLevel < 0) volumeLevel = 0;
        if (volumeLevel > 1) volumeLevel = 1;
        return volumeLevel;
    }

    private void convertSeekProgressToVolume(int level) {
        int volumeSeekBarMax = volumeSeekBar.getMax();
        float volumeLevel = level * 1f / volumeSeekBarMax;
        setVolumeLevel(volumeLevel);
    }


    public interface IVolumeChangeListener {
        void onVolumeChanged(float volumeLevel);
    }
}
