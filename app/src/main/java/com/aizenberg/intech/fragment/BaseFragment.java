package com.aizenberg.intech.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.intech.TheApplication;
import com.aizenberg.intech.activity.MainActivity;
import com.aizenberg.intech.core.event.ProgressVisibilityEvent;
import com.aizenberg.intech.core.model.RestFault;
import com.aizenberg.intech.core.network.NoNetworkConnectivityError;
import com.aizenberg.intech.core.network.service.BaseRestService;
import com.aizenberg.intech.view.CustomToolbarController;
import com.aizenberg.support.logger.Logger;
import com.aizenberg.support.logger.LoggerFactory;
import com.aizenberg.support.network.NetworkConnectionManager;
import com.aizenberg.support.viewinjector.ViewInjector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class BaseFragment extends Fragment {

    /**
     * In classes list stored instances of listeners for event bus.
     */
    protected View rootView;


    protected void afterViewCreated(Bundle savedInstanceState) {

    }


    protected void switchProgressVisibility(boolean show) {
        MainActivity mainActivity = getMainActivity();
        if (mainActivity != null) {
            mainActivity.switchProgressVisibility(show);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = ViewInjector.reflectLayout(this, inflater, container);
        afterViewCreated(savedInstanceState);
        return rootView;
    }

    public String stringFromResources(@StringRes int stringResourceId) {
        Resources resources = getResources();
        if (resources != null) {
            return resources.getString(stringResourceId);
        }
        return "";
    }

    public String stringFromResources(int stringResourceId, Object... args) {
        Resources resources = getResources();
        if (resources != null) {
            return resources.getString(stringResourceId, args);
        }
        return "";
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        switchProgressVisibility(false);
        super.onPause();
    }


    protected void setupLeftImage(int drawableId, View.OnClickListener clickListener) {
        setupLeftImage(ContextCompat.getDrawable(getActivity(), drawableId), clickListener);
    }

    protected void setupLeftImage(Drawable drawable, View.OnClickListener clickListener) {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.setLeftImageView(drawable, clickListener);

        }
    }

    protected void setToolbarColor(int colorId, int textColorId) {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.setToolbarColor(colorId, textColorId);
        }
    }

    protected void resetToolbarColor() {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.resetToolbarColor();
        }
    }


    protected void setupRightImage(int drawableId, View.OnClickListener clickListener) {
        setupRightImage(ContextCompat.getDrawable(getActivity(), drawableId), clickListener);
    }

    protected void setupRightImage(Drawable drawable, View.OnClickListener clickListener) {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.setRightImageView(drawable, clickListener);
        }
    }

    protected void setupCenterText(@StringRes int textId) {
        setupCenterText(stringFromResources(textId));
    }

    protected void setupCenterText(String text) {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.setCenterTextView(text);

        }
    }


    protected boolean isNetworkConnectedNow() {
        NetworkConnectionManager.NetworkState currentNetworkState =
                NetworkConnectionManager.getInstance().getCurrentNetworkState(TheApplication.getInstance());
        return currentNetworkState != null && currentNetworkState.isEnabled();
    }

    protected void showNoNetworkConnection() {
        MainActivity mainActivity = getMainActivity();
        if (mainActivity != null) {
            mainActivity.showNoInternetConnection();
        }
    }


    protected <T extends View> T getViewById(int viewId) {
        return getViewById(rootView, viewId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

    protected void executeService(BaseRestService<?> service, boolean suppressProgressBar) {
        if (!suppressProgressBar) {
            switchProgressVisibility(true);
        }
        service.run();
    }

    protected void executeService(BaseRestService<?> service) {
        executeService(service, false);
    }


    protected MainActivity getMainActivity() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing() && activity instanceof MainActivity) {
            return (MainActivity) activity;
        }
        return null;
    }


    protected Crouton makeCustomCrouton(@StringRes int txtId, Style style) {
        return makeCustomCrouton(stringFromResources(txtId), style);
    }

    protected Crouton makeCustomCrouton(String text, Style style) {
        return makeCustomCrouton(text, style, true);
    }

    protected Crouton makeCustomCrouton(@StringRes int txtId, Style style, boolean autoShow) {
        return makeCustomCrouton(stringFromResources(txtId), style, autoShow);
    }

    protected Crouton makeCustomCrouton(String text, Style style, boolean autoShow) {
        MainActivity baseActivityIfCanInteract = getMainActivity();
        if (baseActivityIfCanInteract != null) {
            Crouton crouton = baseActivityIfCanInteract.makeCustomCrouton(text, style);
            if (autoShow && crouton != null) {
                crouton.show();
            }
            return crouton;
        }
        return null;
    }


    protected CustomToolbarController getToolBar() {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            return activity.getToolbarController();
        }
        return null;
    }

    protected void hideToolbar() {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.hideToolbar();
        }
    }

    protected void showToolbar() {
        CustomToolbarController toolBar = getToolBar();
        if (toolBar != null) {
            toolBar.showToolbar();
        }
    }


    @Subscribe(sticky = true)
    public void onProgressVisibilityChangeRequest(ProgressVisibilityEvent visibilityEvent) {
        if (!visibilityEvent.isVisibility()) {
            switchProgressVisibility(false);
        }
    }

    @Subscribe
    public void onFailure(RestFault restFault) {
        Throwable throwable = restFault.getThrowable();
        if (restFault.isException() && throwable instanceof NoNetworkConnectivityError) {
            return;
        }
        Logger logger = LoggerFactory.getLogger(getClass());
        if (restFault.isException()) {
            logger.e(throwable.getMessage(), throwable);
        } else {
            logger.e(restFault.toString());
        }
    }


}
