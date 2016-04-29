package com.aizenberg.intech.core.network;


import com.aizenberg.intech.core.model.RestFault;

/**
 * Created by Yuriy Aizenberg
 */
public interface IRemoteFailureListener {

    void onFailure(RestFault fault);

    class Adapter implements IRemoteFailureListener {

        @Override
        public void onFailure(RestFault fault) {

        }
    }

}
