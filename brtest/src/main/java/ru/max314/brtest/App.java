package ru.max314.brtest;

import android.app.Application;

import java.security.PublicKey;

/**
 * Created by max on 16.02.2015.
 */
public class App extends Application {

    private static App self;
    public StringBuffer buffer = new StringBuffer();

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
    }

    public static App getInstance(){
        return self;
    }


}
