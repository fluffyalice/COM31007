package uk.ac.shef.oak.com4510;

import android.app.Application;

import uk.ac.shef.oak.com4510.mydatabase.DatabaseInitialize;

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        DatabaseInitialize.init(this);

    }
}