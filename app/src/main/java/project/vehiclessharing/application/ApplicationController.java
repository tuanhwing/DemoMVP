package project.vehiclessharing.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

/**
 * Created by Tuan on 04/08/2017.
 */

public class ApplicationController extends Application {

    public static SharedPreferences sharedPreferences;
    private static ApplicationController sInstance;//A singleton instance of the application class for easy access in other places

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize sharedPreferences instance
        sharedPreferences = getSharedPreferences( getPackageName() + "_storage", MODE_PRIVATE);


        // initialize the singleton
        sInstance = this;
    }

    /**
     * @return co.pixelmatter.meme.ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }
}
