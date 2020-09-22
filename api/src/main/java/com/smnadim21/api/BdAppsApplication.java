package com.smnadim21.api;

import android.app.Application;
import android.content.Context;

public class BdAppsApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context =  getApplicationContext();

    }


    public static Context getContext(){
        return context;
    }



}
