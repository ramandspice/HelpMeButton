package com.helme.helpmebutton.rest.requests;

import org.apache.http.message.BasicNameValuePair;

public class LoginRequest extends BaseRequest {

    public enum LoginRequestParam {
        USER_NAME("username"),
        PASSWORD("password");

        private final String param;

        private LoginRequestParam(String param) {
            this.param = param;
        }

        @Override
        public String toString() {
            return param;
        }
    }

    private static final String METHOD_NAME = "logIn";
    private static final String GRANT_TYPE = "password";

    public LoginRequest(String userName, String password) {
        super(GRANT_TYPE);
        addParam(new BasicNameValuePair(LoginRequestParam.USER_NAME.toString(), userName));
        addParam(new BasicNameValuePair(LoginRequestParam.PASSWORD.toString(), password));
    }

    public static String getMethodName() {
        return METHOD_NAME;
    }
}
