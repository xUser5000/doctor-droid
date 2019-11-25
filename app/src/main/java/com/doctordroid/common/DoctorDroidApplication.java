package com.doctordroid.common;

import android.app.Application;

import io.realm.Realm;

public class DoctorDroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
