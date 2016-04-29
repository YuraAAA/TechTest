package com.aizenberg.intech.core.network;

/**
 * Created by Yuriy Aizenberg
 */
public interface IRemoteSuccessListener<T> {

    void onSuccess(T data);

    class Adapter<T> implements IRemoteSuccessListener<T> {

        @Override
        public void onSuccess(T data) {

        }
    }

}
