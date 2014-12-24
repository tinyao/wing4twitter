package im.zico.wingtwitter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import twitter4j.AsyncTwitter;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;

public class TweetComposeActivity extends Activity implements View.OnClickListener {

    private ImageView photoV, locationV, mentionV, topicV;
    private View updateV;
    private EditText tweetEdt;

    public static void showDialog(Activity activity) {
        activity.startActivity(new Intent(activity, TweetComposeActivity.class));
        activity.overridePendingTransition(R.anim.grow_from_bottom_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Slide());

        setContentView(R.layout.activity_compose);

        photoV = (ImageView) findViewById(R.id.compose_add_photo);
        locationV = (ImageView) findViewById(R.id.compose_enable_location);
        mentionV = (ImageView) findViewById(R.id.compose_add_mention);
        topicV = (ImageView) findViewById(R.id.compose_add_topic);
        tweetEdt = (EditText) findViewById(R.id.compose_tweet_edt);
        updateV = findViewById(R.id.compose_update_tweet);

        photoV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "photo click ... ");
            }
        });

        photoV.setOnClickListener(this);
        locationV.setOnClickListener(this);
        mentionV.setOnClickListener(this);
        topicV.setOnClickListener(this);
        updateV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("DEBUG", "click ... ");
        switch (v.getId()) {
            case R.id.compose_add_photo:
                break;
            case R.id.compose_enable_location:
                break;
            case R.id.compose_add_mention:
                break;
            case R.id.compose_add_topic:
                break;
            case R.id.compose_update_tweet:
                Log.d("DEBUG", "Update Status ... ");
                updateTweet();
                break;
        }
    }

    private void updateTweet() {
        AsyncTwitter asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);
        StatusUpdate status = new StatusUpdate(tweetEdt.getText().toString());
        status.setLocation(new GeoLocation(40, 120));
        asyncTwitter.updateStatus(status);
    }

    private TwitterListener listener = new TwitterAdapter(){
        @Override
        public void updatedStatus(Status status) {
            super.updatedStatus(status);
            Log.d("DEBUG", "Update Successful");
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.back_to_bottom_right);
    }
}
