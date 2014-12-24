package im.zico.wingtwitter.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * Created by tinyao on 12/23/14.
 */
public class ComposeDialog extends Dialog{

    private ImageView photoV, locationV, mentionV, topicV;

    private View updateV;

    private EditText tweetEdt;


    public ComposeDialog(Context context) {
        super(context, R.style.TweetDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        this.setCanceledOnTouchOutside(true);

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

//        photoV.setOnClickListener(clickListener);
//        locationV.setOnClickListener(clickListener);
//        mentionV.setOnClickListener(clickListener);
//        topicV.setOnClickListener(clickListener);
        updateV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Update Status ... ");
                updateTweet();
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
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
    };


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
            cancel();
        }
    };


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(R.string.dialog_fire_missiles)
//                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // FIRE ZE MISSILES!
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }

}
