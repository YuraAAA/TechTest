package com.aizenberg.intech.core.model;


import com.aizenberg.intech.core.IntechException;

/**
 * Created by Yuriy Aizenberg
 */
public class RestFault {

    private Throwable throwable;
    private ServerError serverError;


    public RestFault(Throwable throwable) {
        this.throwable = throwable;
        checkArguments();
    }

    public RestFault(ServerError serverError) {
        this.serverError = serverError;
        checkArguments();
    }

    public RestFault(Throwable throwable, ServerError serverError) {
        this.throwable = throwable;
        this.serverError = serverError;
        checkArguments();
    }

    private void checkArguments() {
        if (isServerError()) return;
        if (throwable == null) throwable = new IntechException("Something went wrong");
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public ServerError getServerError() {
        return serverError;
    }

    public boolean isServerError() {
        return serverError != null;
    }

    public boolean isException() {
        return throwable != null;
    }


    @Override
    public String toString() {
        if (isServerError()) {
            return serverError.toString();
        } else {
            return getThrowable().getMessage();
        }
    }


}
