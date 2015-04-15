package com.helme.helpmebutton.rest.requests;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRequest {

    public enum BaseRequestParam {
        GRANT_TYPE("grant_type"),
        CLIENT_ID("client_id"),
        CLIENT_SECRET("client_secret");

        private final String param;

        private BaseRequestParam(String param) {
            this.param = param;
        }

        @Override
        public String toString() {
            return param;
        }
    }

    private static final String CLIENT_ID = "client_android_quy";
    private static final String CLIENT_SECRET = "128xhhd75t6dhd";

    private List<NameValuePair> mParams;

    public BaseRequest(String grantType) {
        mParams = new ArrayList<>();
        mParams.add(new BasicNameValuePair(BaseRequestParam.GRANT_TYPE.toString(), grantType));
        mParams.add(new BasicNameValuePair(BaseRequestParam.CLIENT_ID.toString(), CLIENT_ID));
        mParams.add(new BasicNameValuePair(BaseRequestParam.CLIENT_SECRET.toString(), CLIENT_SECRET));
    }

    public void addParam(BasicNameValuePair nameValuePair) {
        mParams.add(nameValuePair);
    }

    public List<NameValuePair> getParams() {
        return mParams;
    }
}













