package com.aizenberg.intech.core;

/**
 * Created by Yuriy Aizenberg
 */
public class IntechException extends RuntimeException {

    public IntechException() {
    }

    public IntechException(String detailMessage) {
        super(detailMessage);
    }

    public IntechException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
