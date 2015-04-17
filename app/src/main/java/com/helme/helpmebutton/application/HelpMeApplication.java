package com.helme.helpmebutton.application;

import android.app.Application;
import com.helme.helpmebutton.util.GSON;

public class HelpMeApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        GSON.getInstance().initialize();
        AppState.initialize(this);
    }
}
