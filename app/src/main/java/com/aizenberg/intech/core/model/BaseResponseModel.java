package com.aizenberg.intech.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.HttpURLConnection;

/**
 * Created by Yuriy Aizenberg
 */
public class BaseResponseModel extends BaseModel {

    @JsonIgnore
    private int httpCode = 200;

    public boolean isSuccess() {
        return httpCode == HttpURLConnection.HTTP_OK;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public boolean isServerError() {
        return !isSuccess();
    }
}
