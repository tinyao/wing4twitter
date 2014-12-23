package im.zico.wingtwitter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;

import im.zico.wingtwitter.WingApp;

/**
 * Created by tinyao on 11/30/14.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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
