package im.zico.wingtwitter.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import im.zico.wingtwitter.APIKey;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * Created by tinyao on 11/29/14.
 */
public class OAuthActivity extends BaseActivity {

    private AsyncTwitter asyncTwitter;
    private WebView mWebView;
    private String requestUrl;

    SmoothProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (SmoothProgressBar) findViewById(R.id.loadProgressBar);

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(APIKey.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(APIKey.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        AsyncTwitterFactory factory = new AsyncTwitterFactory(configuration);
        asyncTwitter = factory.getInstance();
        asyncTwitter.addListener(listener);

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        mWebView = (WebView) findViewById(R.id.webview_login);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setSaveFormData(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.v("talon_login", "url: " + url);
                if (url != null && url.startsWith("oauth:///zico.im")) {
                    handleTwitterCallback(url);
                } else if (url.equals("https://twitter.com/")) {
                    webView.loadUrl(requestUrl);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

        });

        asyncTwitter.getOAuthRequestTokenAsync("oauth:///zico.im");

    }

    TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotOAuthAccessToken(AccessToken token) {
            super.gotOAuthAccessToken(accessToken);
            Log.v("logging_in", "this is what the token should be: " + token.getToken());
            accessToken = token;
            mHandler.sendEmptyMessage(1);

            PreferencesManager.initializeInstance(OAuthActivity.this);
            PreferencesManager prefManager = PreferencesManager.getInstance();
            prefManager.setValue(PrefKey.KEY_ACCESSTOKEN, token.getToken());
            prefManager.setValue(PrefKey.KEY_TOKENSECRET, token.getTokenSecret());
            prefManager.setValue(PrefKey.KEY_USERID, token.getUserId());
            prefManager.setValue(PrefKey.KEY_SCREENNAME, token.getScreenName());

        }

        @Override
        public void gotOAuthRequestToken(RequestToken token) {
            if (token != null) {
                requestToken = token;
                mHandler.sendEmptyMessage(0);
            }
            super.gotOAuthRequestToken(token);
        }

        @Override
        public void gotHomeTimeline(ResponseList<Status> statuses) {
            super.gotHomeTimeline(statuses);
            Log.d("DEBUG", "got tweets: " + statuses.size());
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                requestUrl = requestToken.getAuthenticationURL();
                mWebView.loadUrl(requestUrl);
                mWebView.requestFocus(View.FOCUS_UP | View.FOCUS_RIGHT);
            } else if (msg.what == 1) {
                Toast.makeText(OAuthActivity.this, "Login success, your name is " + accessToken.getScreenName(),
                        Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(OAuthActivity.this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
            super.handleMessage(msg);
        }
    };

    private String verifier;
    private RequestToken requestToken;
    private AccessToken accessToken;

    public void handleTwitterCallback(String url) {
        Log.v("twitter_login_activity", "oauth");
        verifier = Uri.parse(url).getQueryParameter("oauth_verifier");
        asyncTwitter.getOAuthAccessTokenAsync(requestToken, verifier);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finishAfterTransition();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
