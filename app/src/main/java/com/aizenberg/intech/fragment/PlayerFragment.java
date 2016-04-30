package com.aizenberg.intech.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.aizenberg.intech.R;
import com.aizenberg.intech.core.model.Melody;
import com.aizenberg.intech.view.PlayerControlsView;
import com.aizenberg.intech.view.VolumeView;
import com.aizenberg.support.utils.StringUtils;
import com.aizenberg.support.viewinjector.annotation.Id;
import com.aizenberg.support.viewinjector.annotation.Layout;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.util.Util;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Yuriy Aizenberg
 */
@Layout(R.layout.player_fragment)
public class PlayerFragment extends BaseFragment implements ExoPlayer.Listener {

    @Id(R.id.player_controls)
    private PlayerControlsView controlsView;
    @Id(R.id.img_artist)
    private ImageView imgArtist;

    private ExoPlayer exoPlayer;
    private Melody data;
    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int AUDIO_BUFFER_SEGMENTS = 54;
    private Handler playerTickHandler;
    //Need to pause mode
    private long savedPosition;


    @Override
    protected void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            data = (Melody) arguments.getSerializable("data");
        }
        if (data == null || data.getDemoUrl() == null) {
            getActivity().onBackPressed();
            return;
        }
        String pictureUrl = data.getPictureUrl();
        if (!StringUtils.isEmpty(pictureUrl)) {
            Picasso.with(getActivity()).load(pictureUrl).fit().centerCrop().into(imgArtist);
        }
        controlsView.block();
        exoPlayer = ExoPlayer.Factory.newInstance(1);
        exoPlayer.addListener(this);
        exoPlayer.setPlayWhenReady(true);
        final TrackRenderer renderer = getRenderer();
        exoPlayer.prepare(renderer);
        controlsView.setChangeListener(new VolumeView.IVolumeChangeListener() {
            @Override
            public void onVolumeChanged(float volumeLevel) {
                exoPlayer.sendMessage(renderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, volumeLevel);
            }
        });
        controlsView.setSeekChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    long duration = exoPlayer.getDuration();
                    long targetPosition = (long) ((progress * 1f / seekBar.getMax()) * duration);
                    exoPlayer.seekTo(targetPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        controlsView.setStateChangeListener(new PlayerControlsView.IStateChangeListener() {
            @Override
            public void onStateChanged(PlayerControlsView.State state) {
                switch (state) {
                    case PLAY:
                        if (exoPlayer.getPlaybackState() == ExoPlayer.STATE_ENDED) {
                            controlsView.setPauseIcon();
                            exoPlayer.seekTo(0);
                        } else {
                            exoPlayer.setPlayWhenReady(true);
                            controlsView.setPauseIcon();
                        }
                        break;
                    case PAUSE:
                        savedPosition = exoPlayer.getCurrentPosition();
                        exoPlayer.setPlayWhenReady(false);
                        controlsView.setPlayIcon();
                        break;
                    case STOP:
                        savedPosition = 0;
                        exoPlayer.setPlayWhenReady(false);
                        controlsView.setPlayIcon();
                        break;
                }
            }
        });
    }

    private TrackRenderer getRenderer() {
        return new MediaCodecAudioTrackRenderer(getSampleSource(), MediaCodecSelector.DEFAULT);
    }

    private SampleSource getSampleSource() {
        return new ExtractorSampleSource(
                Uri.parse(data.getDemoUrl()),
                new DefaultHttpDataSource(Util.getUserAgent(getActivity(), stringFromResources(R.string.app_name)), null),
                new DefaultAllocator(BUFFER_SEGMENT_SIZE),
                BUFFER_SEGMENT_SIZE * AUDIO_BUFFER_SEGMENTS
        );
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == ExoPlayer.STATE_READY) {
            controlsView.setPauseIcon();
            controlsView.unblock();
            startPlayerHandler();
            if (savedPosition > 0) {
                exoPlayer.seekTo(savedPosition);
                savedPosition = 0;
            }
            controlsView.setPauseIcon();
        } else if (playbackState == ExoPlayer.STATE_ENDED) {
            controlsView.block();
            stopPlayerHandler();
            controlsView.reset();
            controlsView.setPlayIcon();
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        makeCustomCrouton(error.getMessage(), Style.ALERT, true);
        controlsView.block();
        stopPlayerHandler();
    }

    private void startPlayerHandler() {
        stopPlayerHandler();
        final long updateTime = TimeUnit.SECONDS.toMillis(1);
        playerTickHandler = new Handler();
        playerTickHandler.post(new Runnable() {
            @Override
            public void run() {
                long duration = exoPlayer.getDuration();
                long currentPosition = exoPlayer.getCurrentPosition();
                float percentsFloat = currentPosition * 1f / duration;
                int percents = (int) (percentsFloat * 100);
                setupCurrentTime(currentPosition);
                setupElapsedTime(duration - currentPosition);
                controlsView.setSeekbarProgress(percents);
                //Refresh self
                playerTickHandler.postDelayed(this, updateTime);
            }

            private void setupCurrentTime(long currentPosition) {
                long minutes = toMinutes(currentPosition);
                long seconds = toSeconds(currentPosition);
                String minutesString = minutes < 10 ? "0" + minutes : minutes + "";
                String secondsString = seconds < 10 ? "0" + seconds : seconds + "";
                controlsView.setCurrent(minutesString + ":" + secondsString);

            }

            private void setupElapsedTime(long currentPosition) {
                long minutes = toMinutes(currentPosition);
                long seconds = toSeconds(currentPosition);
                String minutesString = minutes < 10 ? "0" + minutes : minutes + "";
                String secondsString = seconds < 10 ? "0" + seconds : seconds + "";
                controlsView.setElapsed("-" + minutesString + ":" + secondsString);
            }


        });
    }

    private void stopPlayerHandler() {
        if (playerTickHandler != null) {
            playerTickHandler.removeCallbacksAndMessages(null);
        }
        playerTickHandler = null;
    }

    private long toMinutes(long currentTime) {
        return TimeUnit.MINUTES.convert(currentTime, TimeUnit.MILLISECONDS);
    }

    private long toSeconds(long currentTime) {
        return TimeUnit.SECONDS.convert(currentTime - TimeUnit.MINUTES.toMillis(toMinutes(currentTime)), TimeUnit.MILLISECONDS);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupCenterText(data != null ? data.getArtist() : "");
        setupRightImage(null, null);
        setupLeftImage(R.drawable.back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        return super.onBackPressed();
    }
}
