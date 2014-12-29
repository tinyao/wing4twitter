package im.zico.wingtwitter.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;
import java.net.URI;

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
    private ImageView photoAdded;
    private TextView textCounter;

    AsyncTwitter asyncTwitter;

    private File photo;

    public static void showDialog(Activity activity, Bundle bundles) {
        Intent intent = new Intent(activity, TweetComposeActivity.class);
        intent.putExtras(bundles);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.grow_from_bottom_right, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_compose);

        photoV = (ImageView) findViewById(R.id.compose_add_photo);
        locationV = (ImageView) findViewById(R.id.compose_enable_location);
        mentionV = (ImageView) findViewById(R.id.compose_add_mention);
        topicV = (ImageView) findViewById(R.id.compose_add_topic);
        tweetEdt = (EditText) findViewById(R.id.compose_tweet_edt);
        photoAdded = (ImageView) findViewById(R.id.compose_photo_added);
        updateV = findViewById(R.id.compose_update_tweet);
        textCounter = (TextView) findViewById(R.id.compose_header_text_counter);

        if (getIntent().hasExtra("user")) {
            tweetEdt.append(getIntent().getStringExtra("user") + " ");
        }

        if (getIntent().hasExtra("quote")) {
            tweetEdt.setText(getIntent().getStringExtra("quote"));
        }

        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);

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

        tweetEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textCounter.setText((140 - s.toString().length()) + "");
                if (s.toString().length() > 140) {
                    textCounter.setTextColor(getResources().getColor(R.color.accent));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d("DEBUG", "click ... ");
        switch (v.getId()) {
            case R.id.compose_add_photo:
                AlertDialog.Builder builder = new AlertDialog.Builder(TweetComposeActivity.this)
                        .setItems(R.array.dialog_add_photos, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // Find the last picture
                                        String[] projection = new String[]{
                                                MediaStore.Images.ImageColumns._ID,
                                                MediaStore.Images.ImageColumns.DATA,
                                                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                                                MediaStore.Images.ImageColumns.DATE_TAKEN,
                                                MediaStore.Images.ImageColumns.MIME_TYPE
                                        };
                                        final Cursor cursor = getContentResolver()
                                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                                                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

                                        dialog.cancel();

                                        if(cursor.moveToFirst()) {
                                            String photoPath = cursor.getString(1);
                                            photo = new File(photoPath);
                                            if (photo.exists()) {
                                                BitmapFactory.Options options = new BitmapFactory.Options();
                                                options.inSampleSize = 4;
                                                Bitmap bm = BitmapFactory.decodeFile(photoPath, options);
                                                photoAdded.setImageBitmap(bm);
                                            }
                                        }
                                        break;
                                    case 1:

                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        });
                builder.create().show();
                break;
            case R.id.compose_enable_location:

                break;
            case R.id.compose_add_mention:
                int _cur = tweetEdt.getSelectionStart();
                String _currentS = tweetEdt.getText().toString();
                tweetEdt.setText(_currentS.substring(0, _cur)
                        + (_cur==0 ? "@" : " @")
                        + _currentS.substring(_cur));
                tweetEdt.setSelection(_cur + 2);


                break;
            case R.id.compose_add_topic:
                int curs = tweetEdt.getSelectionStart();
                String currentS = tweetEdt.getText().toString();
                tweetEdt.setText(currentS.substring(0, curs)
                        + (curs==0 ? "#" : " #")
                        + currentS.substring(curs));
                tweetEdt.setSelection(curs + 2);
                break;
            case R.id.compose_update_tweet:
                Log.d("DEBUG", "Update Status ... ");
                updateTweet();
                break;
        }
    }

    private void updateTweet() {
        StatusUpdate status = new StatusUpdate(tweetEdt.getText().toString());
        if(photo != null) status.setMedia(photo);
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
