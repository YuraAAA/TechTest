package com.aizenberg.intech.core.event;

/**
 * Created by Yuriy Aizenberg
 */
public class ProgressVisibilityEvent {

    private boolean visibility;

    public ProgressVisibilityEvent(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
