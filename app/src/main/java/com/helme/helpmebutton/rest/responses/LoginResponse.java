package com.helme.helpmebutton.rest.responses;

public class LoginResponse {

    private String access_token;
    private Integer expires_in;
    private String token_type;
    private Object scope;
    private String refresh_token;

    /**
     *
     * @return
     * The access_token
     */
    public String getAccessToken() {
        return access_token;
    }

    /**
     *
     * @param accessToken
     * The access_token
     */
    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    /**
     *
     * @return
     * The expires_in
     */
    public Integer getExpiresIn() {
        return expires_in;
    }

    /**
     *
     * @param expiresIn
     * The expires_in
     */
    public void setExpiresIn(Integer expiresIn) {
        this.expires_in = expiresIn;
    }

    /**
     *
     * @return
     * The token_type
     */
    public String getTokenType() {
        return token_type;
    }

    /**
     *
     * @param tokenType
     * The token_type
     */
    public void setTokenType(String tokenType) {
        this.token_type = tokenType;
    }

    /**
     *
     * @return
     * The scope
     */
    public Object getScope() {
        return scope;
    }

    /**
     *
     * @param scope
     * The scope
     */
    public void setScope(Object scope) {
        this.scope = scope;
    }

    /**
     *
     * @return
     * The refresh_token
     */
    public String getRefreshToken() {
        return refresh_token;
    }

    /**
     *
     * @param refreshToken
     * The refresh_token
     */
    public void setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }
}
