package ru.homecatering;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class Homecatering extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}