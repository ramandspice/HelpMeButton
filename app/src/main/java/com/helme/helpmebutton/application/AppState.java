package com.helme.helpmebutton.application;

import android.content.Context;
import android.content.SharedPreferences;

public class AppState
{
    private static volatile AppState mInstance;
    private static final String SHARED_PREF_NAME = "HelpMePrefs";

    private SharedPreferences mSharedPrefs;
    private String mAccessToken;
    private String mRefreshToken;

    /********************* Constructors *********************/

    private AppState(Context context) {
        mSharedPrefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        mAccessToken = mSharedPrefs.getString(PrefKey.ACCESS_TOKEN, "");
        mRefreshToken = mSharedPrefs.getString(PrefKey.REFRESH_TOKEN, "");
    }

    public static synchronized void initialize(Context context) {
        if (mInstance == null) {
            mInstance = new AppState(context);
        }
    }

    /********************************************************/

    /********************* Public Methods *********************/

    public static synchronized AppState getInstance() {
        return mInstance;
    }

    public void clearTokens() {
        setAccessToken("");
        setRefreshToken("");
    }

    /**********************************************************/

    /******************** Getters & Setters **********************/

    public String getAccessToken()
    {
        return mAccessToken;
    }

    public void setAccessToken(String mAccessToken)
    {
        this.mAccessToken = mAccessToken;
        mSharedPrefs.edit().putString(PrefKey.ACCESS_TOKEN, mAccessToken).apply();
    }

    public String getRefreshToken()
    {
        return mRefreshToken;
    }

    public void setRefreshToken(String mRefreshToken)
    {
        this.mRefreshToken = mRefreshToken;
        mSharedPrefs.edit().putString(PrefKey.REFRESH_TOKEN, mRefreshToken).apply();
    }

    /******************************************/
}
