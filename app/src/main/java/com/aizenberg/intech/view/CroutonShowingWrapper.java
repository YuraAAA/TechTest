package com.aizenberg.intech.view;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;

/**
 * Created by Yuriy Aizenberg
 */
public class CroutonShowingWrapper implements LifecycleCallback {

    private boolean isShowing;

    public CroutonShowingWrapper(Crouton crouton) {
        crouton.setLifecycleCallback(this);
    }

    @Override
    public void onDisplayed() {
        isShowing = true;
    }

    @Override
    public void onRemoved() {
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
    }
}
