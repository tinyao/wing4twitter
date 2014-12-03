package im.zico.wingtwitter.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import im.zico.wingtwitter.WingApp;

/**
 * Created by tinyao on 11/30/14.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean status = Utils.isNetworkAvailable(context);
        if (WingApp.uiInForeground && !status) {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

}
