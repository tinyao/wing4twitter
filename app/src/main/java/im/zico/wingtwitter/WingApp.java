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

    public static boolean uiInForeground = false;
    private static Context sContext;

    private static AsyncTwitter asyncTwitter;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static void initializeTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(APIKey.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(APIKey.TWITTER_CONSUMER_SECRET);
        if (PreferencesManager.getInstance(sContext).hasKey(PrefKey.KEY_ACCESSTOKEN)) {
            String accessToken = PreferencesManager.getInstance(sContext).getValue(PrefKey.KEY_ACCESSTOKEN);
            String tokenSecret = PreferencesManager.getInstance(sContext).getValue(PrefKey.KEY_TOKENSECRET);
            builder.setOAuthAccessToken(accessToken);
            builder.setOAuthAccessTokenSecret(tokenSecret);
        }
        Configuration configuration = builder.build();
        AsyncTwitterFactory factory = new AsyncTwitterFactory(configuration);
        asyncTwitter = factory.getInstance();
    }

    public static AsyncTwitter getTwitterInstance() {
        if (asyncTwitter == null) {
            initializeTwitter();
        }
        return asyncTwitter;
    }

    public static void resetAsyncTwitter() {
        asyncTwitter = null;
    }

}
