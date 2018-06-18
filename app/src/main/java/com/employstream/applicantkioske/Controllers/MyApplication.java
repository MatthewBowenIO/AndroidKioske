package com.employstream.applicantkioske.Controllers;

import android.app.Application;

/**
 * Created by matthewbowen on 6/10/18.
 */

public class MyApplication extends Application {

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApplication getContext() {
        return mContext;
    }
}