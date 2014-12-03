package im.zico.wingtwitter;

import android.app.Application;
import android.content.Context;

/**
 * Created by tinyao on 11/30/14.
 */
public class WingApp extends Application {

    public static boolean uiInForeground = false;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
