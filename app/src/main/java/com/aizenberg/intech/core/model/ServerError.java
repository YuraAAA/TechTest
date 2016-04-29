package com.aizenberg.intech.core.model;

import java.io.Serializable;

/**
 * Created by Yuriy Aizenberg
 */
public class ServerError implements Serializable {

    private String status;
    private String message;
    private int httpCode;

    public ServerError(String status, String message, int httpCode) {
        this.status = status;
        this.message = message;
        this.httpCode = httpCode;
    }

    public ServerError(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String toString() {
        return "ServerError{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", httpCode=" + httpCode +
                '}';
    }
}
