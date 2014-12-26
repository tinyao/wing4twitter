package im.zico.wingtwitter.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import im.zico.wingtwitter.WingApp;
import twitter4j.AsyncTwitter;
import twitter4j.PagableResponseList;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.User;

/**
 * Created by tinyao on 12/24/14.
 */
public class TweetService extends IntentService {

    private AsyncTwitter asyncTwitter;

    public static final String INTENT_ACTION_GET_FOLLOWERS = "acton_user_follow";

    public TweetService() {
        super("tweet service");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);
        Log.d("DEBUG", "service onCreate");
    }

    private TwitterListener listener = new TwitterAdapter(){
        @Override
        public void gotFollowersList(PagableResponseList<User> users) {
            super.gotFollowersList(users);
            Log.d("DEBUG", "Followers: " + users.size() + "  - " + users);
            if (users.hasNext()) {
                asyncTwitter.getFollowersList(WingApp.getCurrentUserID(), users.getNextCursor());
            } else {
                asyncTwitter.getFriendsList(WingApp.getCurrentUserID(), -1);
            }
        }

        @Override
        public void gotFriendsList(PagableResponseList<User> users) {
            super.gotFriendsList(users);
            Log.d("DEBUG", "Following: " + users.size() + "  - " + users);
            if(users.hasNext()) {
                asyncTwitter.getFriendsList(WingApp.getCurrentUserID(), users.getNextCursor());
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null) return;
        String action = intent.getAction();
        if (action.equals(INTENT_ACTION_GET_FOLLOWERS)) {
            getUserFollow();
        }
    }

    private void getUserFollow() {
        asyncTwitter.getFollowersList(WingApp.getCurrentUserID(), -1);
    }

}
