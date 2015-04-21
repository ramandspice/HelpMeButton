package com.helme.helpmebutton.rest.requests;

import org.apache.http.message.BasicNameValuePair;

public class SignUpRequest extends BaseRequest {

    public enum SignUpRequestParam {
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        PHONE_NUMBER("phoneNumber"),
        DOB("dateOfBirth"),
        PASSWORD("password");

        private final String param;

        private SignUpRequestParam(String param) {
            this.param = param;
        }

        @Override
        public String toString() {
            return param;
        }
    }

    private static final String GRANT_TYPE = "client_credentials";
    private static final String METHOD_NAME = "addUser";

    public SignUpRequest(String firstName, String lastName, String email, String phoneNumber,
                         String dob, String password) {
        super(GRANT_TYPE);
        addParam(new BasicNameValuePair(SignUpRequestParam.FIRST_NAME.toString(), firstName));
        addParam(new BasicNameValuePair(SignUpRequestParam.LAST_NAME.toString(), lastName));
        addParam(new BasicNameValuePair(SignUpRequestParam.EMAIL.toString(), email));
        addParam(new BasicNameValuePair(SignUpRequestParam.PHONE_NUMBER.toString(), phoneNumber));
        addParam(new BasicNameValuePair(SignUpRequestParam.DOB.toString(), dob));
        addParam(new BasicNameValuePair(SignUpRequestParam.PASSWORD.toString(), password));
    }

    public static String getMethodName() {
        return METHOD_NAME;
    }
}
