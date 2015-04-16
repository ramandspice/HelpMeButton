package com.helme.helpmebutton.rest.responses;

public class WebResponse {

    private String mResponseEntity;
    private int mResponseCode;

    public WebResponse (String mResponseEntity, int mResponseCode) {
        this.mResponseEntity = mResponseEntity;
        this.mResponseCode = mResponseCode;
    }

    public String getResponseEntity() {
        return mResponseEntity;
    }

    public int getResponseCode() {
        return mResponseCode;
    }
}
