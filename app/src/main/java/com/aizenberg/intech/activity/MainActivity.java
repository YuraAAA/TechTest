package com.aizenberg.intech.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aizenberg.intech.R;
import com.aizenberg.intech.core.event.NetworkStateChangedEvent;
import com.aizenberg.intech.core.model.Melodies;
import com.aizenberg.intech.fragment.MelodiesFragment;
import com.aizenberg.intech.view.CustomToolbarController;
import com.aizenberg.support.fsm.ISwitcher;
import com.aizenberg.support.fsm.Switcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends AppCompatActivity {

    private View progressView;
    private Crouton crouton;
    private CustomToolbarController toolbarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null && toolbar != null) {
            toolbar.setContentInsetsAbsolute(0, 0);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            toolbarController = new CustomToolbarController(this, toolbar);
        }

        progressView = findViewById(R.id.progressBar);
        ISwitcher iSwitcher = Switcher.createSwitcher(this, R.id.fragment_container)
                .withAnimations(R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_out_right, R.animator.slide_in_right);
        iSwitcher.switchTo(MelodiesFragment.class);
    }

    public CustomToolbarController getToolbarController() {
        return toolbarController;
    }

    @Override
    protected void onDestroy() {
        Switcher.unregisterSwitcher(this);
        super.onDestroy();
    }

    public void switchProgressVisibility(boolean show) {
        if (progressView != null) {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Subscribe(sticky = true)
    public void onNetworkStateChanged(NetworkStateChangedEvent networkStateChangedEvent) {
        if (!networkStateChangedEvent.isNetworkEnabled()) {
            showNoInternetConnection();
        }
    }

    public void showNoInternetConnection() {
        resetPreviousCrouton();
        crouton = Crouton.makeText(this, getString(R.string.no_internet_connection), Style.ALERT, R.id.fragment_container);
        crouton.show();
    }

    private void resetPreviousCrouton() {
        if (crouton != null) {
            try {
                crouton.cancel();
            } catch (Exception ignored) {
            } finally {
                crouton = null;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Crouton.cancelAllCroutons();
        EventBus.getDefault().unregister(this);
    }


    public Crouton makeCustomCrouton(String text, Style style) {
        return Crouton.makeText(this, text, style, R.id.fragment_container);
    }
}
