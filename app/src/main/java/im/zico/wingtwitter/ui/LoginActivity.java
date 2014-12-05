package im.zico.wingtwitter.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;
import twitter4j.AsyncTwitter;
import twitter4j.auth.RequestToken;

/**
 * Created by tinyao on 11/30/14.
 */
public class LoginActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != PreferencesManager.getInstance(this).getValue(PrefKey.KEY_ACCESSTOKEN)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
    }

    public void signin(View view) {

        Intent intent = new Intent(this, OAuthActivity.class);
        // create the transition animation - the images in the layouts
        // of both activities are defined with android:transitionName="robot"
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, toolbar, "actionBar");
        // start the new activity
        startActivity(intent, options.toBundle());
    }
}
