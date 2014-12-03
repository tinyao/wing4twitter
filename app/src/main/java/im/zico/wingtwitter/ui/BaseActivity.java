package im.zico.wingtwitter.ui;

import android.app.Activity;

import im.zico.wingtwitter.WingApp;

/**
 * Created by tinyao on 11/30/14.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onStart() {
        WingApp.uiInForeground = true;
        super.onStart();
    }

    @Override
    protected void onPause() {
        WingApp.uiInForeground = false;
        super.onPause();
    }
}
