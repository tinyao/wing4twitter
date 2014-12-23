package im.zico.wingtwitter.ui;

import im.zico.wingtwitter.WingApp;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by tinyao on 12/23/14.
 */
public class SwipeBaseActivity extends SwipeBackActivity {

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
