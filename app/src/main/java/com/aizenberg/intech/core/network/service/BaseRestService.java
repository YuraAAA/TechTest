package com.aizenberg.intech.core.network.service;


import android.app.Application;

import com.aizenberg.intech.TheApplication;
import com.aizenberg.intech.core.IntechException;
import com.aizenberg.intech.core.event.NetworkStateChangedEvent;
import com.aizenberg.intech.core.event.ProgressVisibilityEvent;
import com.aizenberg.intech.core.model.BaseResponseModel;
import com.aizenberg.intech.core.model.RestFault;
import com.aizenberg.intech.core.model.ServerError;
import com.aizenberg.intech.core.network.IRemoteFailureListener;
import com.aizenberg.intech.core.network.IRemoteSuccessListener;
import com.aizenberg.intech.core.network.IService;
import com.aizenberg.intech.core.network.NoNetworkConnectivityError;
import com.aizenberg.support.logger.Logger;
import com.aizenberg.support.logger.LoggerFactory;
import com.aizenberg.support.network.NetworkConnectionManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class BaseRestService<T extends BaseResponseModel> implements Callback<T> {

    //ReAuth if server returns 401
    private int retryCount;
    private Call<T> currentCall;

    private IRemoteSuccessListener<T> successListener;
    private IRemoteFailureListener failureListener;
    private Logger logger;

    public BaseRestService() {
        logger = LoggerFactory.getLogger(getClass());
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapper;
    }

    public BaseRestService<T> setSuccessListener(IRemoteSuccessListener<T> successListener) {
        this.successListener = successListener;
        return this;
    }

    public void setFailureListener(IRemoteFailureListener failureListener) {
        this.failureListener = failureListener;
    }

    protected IService getAPI() {
        return ServiceHolder.getService();
    }

    private boolean isNetworkConnection() {
        Application instance = TheApplication.getInstance();
        NetworkConnectionManager.NetworkState currentNetworkState =
                NetworkConnectionManager.getInstance().getCurrentNetworkState(instance);
        return currentNetworkState != null && currentNetworkState.isEnabled();

    }

    public void run() {
        if (!isNetworkConnection()) {
            //Notify about no network connectivity
            EventBus.getDefault().postSticky(new NetworkStateChangedEvent(false));
            //Fire callback for correct lifecycle flow completion
            fireFail(new NoNetworkConnectivityError());
            return;
        }
        currentCall = invoke(getAPI());
        currentCall.enqueue(this);
    }

    public boolean isNullOrCanceled() {
        return currentCall == null || currentCall.isCanceled();
    }

    public void cancel() {
        if (!isNullOrCanceled()) {
            currentCall.cancel();
        }
    }

    public boolean isInProgress() {
        return currentCall != null && !currentCall.isCanceled() && !currentCall.isExecuted();
    }

    protected abstract Call<T> invoke(IService API);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T t = response.body();
        int httpStatusCode = response.code();

        if (t != null) {
            t.setHttpCode(httpStatusCode);
        }

        if (t == null) {
            String data = extractError(response);
            if (data == null) {
                fireFail(new NullPointerException("Empty result"));
            } else {
                fireFail(new IntechException(data));
            }
        } else if (t.isServerError()) {
            int httpCode = t.getHttpCode();
            fireServerError(new ServerError("Server say " + httpCode, "Fault", httpCode));
        } else {
            fireSuccess(t);
        }
    }

    private String extractError(Response<?> response) {
        ResponseBody responseBody = response.errorBody();
        if (responseBody != null) {
            try {
                return responseBody.string();
            } catch (IOException ignored) {
            } finally {
                Util.closeQuietly(responseBody);
            }
        }
        return null;
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        fireFail(t);
    }

    protected void fireFail(Throwable throwable) {
        fireVisibilityEvent(false);
        RestFault failureEvent = new RestFault(throwable);
        if (failureListener != null) {
            failureListener.onFailure(failureEvent);
        } else {
            EventBus.getDefault().post(failureEvent);
        }
    }

    protected void fireServerError(ServerError serverError) {
        fireVisibilityEvent(false);
        RestFault failureEvent = new RestFault(serverError);
        if (failureListener != null) {
            failureListener.onFailure(failureEvent);
        } else {
            EventBus.getDefault().post(failureEvent);
        }
    }

    protected Object wrap(T data) {
        return null;
    }


    protected void fireSuccess(T data) {
        fireVisibilityEvent(false);
        if (successListener != null) {
            successListener.onSuccess(data);
        } else {
            Object wrapped = wrap(data);
            EventBus.getDefault().post(wrapped != null ? wrapped : data);
        }
    }

    private void fireVisibilityEvent(boolean show) {
        EventBus.getDefault().postSticky(new ProgressVisibilityEvent(show));
    }


    protected enum AuthType {
        EMPTY,
        TOKEN
    }

}
