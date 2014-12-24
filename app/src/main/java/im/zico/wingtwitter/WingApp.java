package im.zico.wingtwitter;

import android.app.Application;
import android.content.Context;

import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by tinyao on 11/30/14.
 */
public class WingApp extends Application {

    public static final String TWITTER_APP_NAME = "Wing for Twitter";
    public static final String TWITTER_APP_SOURCE = "<a href=http://zico.im'>Wing</a>";

    public static boolean uiInForeground = false;
    private static Context sContext;

    private static AsyncTwitterFactory mFactory = null;
//    private static AsyncTwitter asyncTwitter;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    private static AsyncTwitterFactory initializeTwitterFactory() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(APIKey.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(APIKey.TWITTER_CONSUMER_SECRET);
        builder.setAsyncNumThreads(5);
        if (PreferencesManager.getInstance(sContext).hasKey(PrefKey.KEY_ACCESSTOKEN)) {
            String accessToken = PreferencesManager.getInstance(sContext).getValue(PrefKey.KEY_ACCESSTOKEN);
            String tokenSecret = PreferencesManager.getInstance(sContext).getValue(PrefKey.KEY_TOKENSECRET);
            builder.setOAuthAccessToken(accessToken);
            builder.setOAuthAccessTokenSecret(tokenSecret);
        }
        Configuration configuration = builder.build();
        return mFactory = new AsyncTwitterFactory(configuration);
    }

    public static AsyncTwitter newTwitterInstance() {
        if (mFactory == null) {
            mFactory = initializeTwitterFactory();
        }
        return mFactory.getInstance();
    }

    public static long getCurrentUserID() {
        if (PreferencesManager.getInstance(sContext).hasKey(PrefKey.KEY_USERID)) {
            return PreferencesManager.getInstance(sContext).getLongValue(PrefKey.KEY_USERID);
        }
        return -1;
    }

//    public static AsyncTwitter getTwitterNew() {
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        builder.setOAuthConsumerKey(APIKey.TWITTER_CONSUMER_KEY);
//        builder.setOAuthConsumerSecret(APIKey.TWITTER_CONSUMER_SECRET);
//        builder.setAsyncNumThreads(5);
//        if (PreferencesManager.getInstance(sContext).hasKey(PrefKey.KEY_ACCESSTOKEN)) {
//            String accessToken = PreferencesManager.getInstance(sContext).getValue(PrefKey.KEY_ACCESSTOKEN);
//            String tokenSecret = PreferencesManager.getInstance(sContext).getValue(PrefKey.KEY_TOKENSECRET);
//            builder.setOAuthAccessToken(accessToken);
//            builder.setOAuthAccessTokenSecret(tokenSecret);
//        }
//        Configuration configuration = builder.build();
//        return new AsyncTwitterFactory(configuration).getInstance();
//    }

    public static void resetAsyncTwitter() {
        mFactory = null;
    }

}
