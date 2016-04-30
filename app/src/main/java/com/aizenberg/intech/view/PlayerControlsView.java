package com.aizenberg.intech.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aizenberg.intech.R;
import com.aizenberg.support.viewinjector.ViewInjector;
import com.aizenberg.support.viewinjector.annotation.Id;

/**
 * Created by Yuriy Aizenberg
 */
public class PlayerControlsView extends LinearLayout {

    @Id(R.id.volume_view)
    private VolumeView volumeView;
    @Id(R.id.seek_player)
    private SeekBar playerBar;
    @Id(R.id.txt_time_current)
    private TextView txtCurrentTime;
    @Id(R.id.txt_time_elapsed)
    private TextView txtElapsed;
    @Id(R.id.img_player_play_action)
    private ImageView imgPlayerState;

    private IStateChangeListener stateChangeListener;


    public PlayerControlsView(Context context) {
        super(context);
        init();
    }

    public PlayerControlsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerControlsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setStateChangeListener(IStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public void setChangeListener(VolumeView.IVolumeChangeListener changeListener) {
        volumeView.setChangeListener(changeListener);
    }

    private void init() {
        inflate(getContext(), R.layout.player_controlls_view, this);
        ViewInjector.reflect(this, this);

    }

    public void setElapsed(String elapsed) {
        txtElapsed.setText(elapsed);
    }

    public void setCurrent(String current) {
        txtCurrentTime.setText(current);
    }


    public void setPlayIcon() {
        imgPlayerState.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        setClickListenerInternal(State.PLAY);
    }

    public void setStopIcon() {
        imgPlayerState.setImageResource(R.drawable.ic_stop_black_24dp);
        setClickListenerInternal(State.STOP);
    }

    public void setPauseIcon() {
        imgPlayerState.setImageResource(R.drawable.ic_pause_black_24dp);
        setClickListenerInternal(State.PAUSE);
    }

    private void setClickListenerInternal(final State state) {
        imgPlayerState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateChangeListener != null) {
                    stateChangeListener.onStateChanged(state);
                }
            }
        });
    }

    public void setSeekbarProgress(int progress) {
        playerBar.setProgress(progress);
    }

    public void block() {
        blockUnblockInternal(true);
    }

    public void unblock() {
        blockUnblockInternal(false);
    }

    private void blockUnblockInternal(boolean blocked) {
        if (blocked) {
            volumeView.block();
        } else {
            volumeView.unblock();
        }
        playerBar.setEnabled(!blocked);
    }

    public void setSeekChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        playerBar.setOnSeekBarChangeListener(listener);
    }


    public void reset() {
        setElapsed("00:00");
        setCurrent("00:00");
        playerBar.setProgress(0);
    }

    public enum State {
        PLAY,
        PAUSE,
        STOP
    }

    public interface IStateChangeListener {
        void onStateChanged(State state);
    }

}
