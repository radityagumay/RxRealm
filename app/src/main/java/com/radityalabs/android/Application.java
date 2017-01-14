package com.radityalabs.android;

import io.realm.Realm;

/**
 * Created by radityagumay on 1/14/17.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
