package com.myleads.android;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;


public class MyApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    private static Context context;

    public static Resources getResourcesStatic() {
        return context.getResources();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

}