package com.aizenberg.intech.core.event;

/**
 * Created by Yuriy Aizenberg
 */
public class NetworkStateChangedEvent {

    private boolean isNetworkEnabled;

    public NetworkStateChangedEvent(boolean isNetworkEnabled) {
        this.isNetworkEnabled = isNetworkEnabled;
    }

    public boolean isNetworkEnabled() {
        return isNetworkEnabled;
    }

    public void setNetworkEnabled(boolean networkEnabled) {
        isNetworkEnabled = networkEnabled;
    }
}
