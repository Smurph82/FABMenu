package com.smurph82.fabmenu;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Ben Murphy on 4/22/17.
 *
 *
 */

public class FABMenuApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + ":" + element.getLineNumber();
            }
        });
    }
}
