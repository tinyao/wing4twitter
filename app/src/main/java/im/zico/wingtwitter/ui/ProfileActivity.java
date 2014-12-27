package im.zico.wingtwitter.ui;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.adapter.UserTweetsAdapter;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.type.WingUser;
import im.zico.wingtwitter.ui.view.NestedListView;
import im.zico.wingtwitter.utils.SpannableStringUtils;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.User;

public class ProfileActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    ImageView bannerImage;
    private int mParallaxImageHeight;
    private BasicProfileCard basicInfo;
    private AsyncTwitter asyncTwitter;
    private long mUserId;
    private String userName = "";

    private NestedListView tweetList;
    private WingDataHelper DBHelper;

    private WingUser mUser;

    private ObservableScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        bannerImage = (ImageView) findViewById(R.id.profile_bannder_image);
        getActionBar().setTitle("");
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);

        revealBanner();

        DBHelper = new WingDataHelper(this);
        basicInfo = new BasicProfileCard(this);
        tweetList = (NestedListView) findViewById(R.id.user_profile_tweets_list);

        Intent intent = getIntent();
        mUserId = intent.getLongExtra(WingStore.TweetColumns.USER_ID, -1);
        if (mUserId == -1) {
            finishAfterTransition();
        } else {
            mUser = DBHelper.getUser(mUserId);
            if (mUser == null) {
                mUser = new WingUser();
                mUser.user_id = intent.getLongExtra(WingStore.TweetColumns.USER_ID, -1);
                mUser.name = intent.getStringExtra(WingStore.TweetColumns.USER_NAME);
                mUser.screenName = intent.getStringExtra(WingStore.TweetColumns.USER_SCREEN_NAME);
                mUser.avatar = intent.getStringExtra(WingStore.TweetColumns.USER_AVATAR_URL);
            } else {
                // check if banner set
                if(mUser.banner==null || mUser.banner.isEmpty()) {
                    findViewById(R.id.anchor).setVisibility(View.GONE);
                }
            }
            basicInfo.setUserInfo(mUser);
        }

        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);

        // show User detail
        Log.d("DEBUG", "Get User Detail ... ");
        asyncTwitter.showUser(mUserId);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("DEBUG", "Get User Statuses ...");
                asyncTwitter.getUserTimeline(mUserId, new Paging().count(5));
            }
        }, 1000);

     }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void revealBanner() {

        bannerImage.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                if (!bannerImage.isAttachedToWindow()) return;

                int cx = (bannerImage.getRight() + bannerImage.getLeft()) / 4;
                int cy = (bannerImage.getTop() + bannerImage.getBottom()) * 2 / 3;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(bannerImage.getWidth(), bannerImage.getHeight());

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(bannerImage, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                bannerImage.setVisibility(View.VISIBLE);
                anim.start();
            }
        }, 100);

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotUserDetail(User user) {
            Log.d("DEBUG", "Got User Detail: " + user);
            WingUser tmpUser = new WingUser(user);
            tmpUser.isFollowing = mUser.isFollowing;
            tmpUser.isFollowMe = mUser.isFollowMe;
            mUser = tmpUser;
            mHandler.sendEmptyMessage(GOT_USER_DETAIL);

            Log.d("DEBUG", "Get User Relation ... ");
            asyncTwitter.showFriendship(WingApp.getCurrentUserID(), mUser.user_id);
            super.gotUserDetail(user);
        }

        @Override
        public void gotUserTimeline(ResponseList<Status> statuses) {
            super.gotUserTimeline(statuses);
            Log.d("DEBUG", "Got User Statuses: " + statuses.size());
            ArrayList<WingTweet> utweets = new ArrayList<>();
            Message msg = new Message();
            for (Status ss : statuses) {
                WingTweet tweet = new WingTweet(ss);
                utweets.add(tweet);
            }
            msg.obj = utweets;
            msg.what = GOT_USER_TWEETS;
            mHandler.sendMessage(msg);
        }

        @Override
        public void gotFavorites(ResponseList<Status> statuses) {
            super.gotFavorites(statuses);
            Log.d("DEBUG", "Got User Favs: " + statuses.size());
        }

        @Override
        public void gotShowFriendship(Relationship relationship) {
            super.gotShowFriendship(relationship);
            Log.d("DEBUG", "Got Relationship " + relationship.isSourceFollowingTarget());
            mUser.isFollowing = relationship.isSourceFollowingTarget();
            mUser.isFollowMe = relationship.isTargetFollowingSource();
            DBHelper.save(mUser);
            mHandler.sendEmptyMessage(GOT_RELATIONSHIP);
        }

        @Override
        public void createdFriendship(User user) {
            super.createdFriendship(user);
            Log.d("DEBUG", "FOLLOWED");
            mUser.isFollowing = true;
            DBHelper.save(mUser);
            mHandler.sendEmptyMessage(CREATED_RELATIONSHIP);
        }

        @Override
        public void destroyedFriendship(User user) {
            super.destroyedFriendship(user);
            mUser.isFollowing = false;
            DBHelper.save(mUser);
            mHandler.sendEmptyMessage(DESTROYED_RELATIONSHIP);
        }
    };

    private static final int GOT_USER_DETAIL = 0;
    private static final int GOT_USER_TWEETS = 1;
    private static final int GOT_RELATIONSHIP = 2;
    private static final int CREATED_RELATIONSHIP = 3;
    private static final int DESTROYED_RELATIONSHIP = 4;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOT_USER_DETAIL:
                    basicInfo.setUserInfo(mUser);
                    break;
                case GOT_USER_TWEETS:
                    findViewById(R.id.progressBarView).setVisibility(View.INVISIBLE);
//                    findViewById(R.id.user_profile_tweets_view_all).setVisibility(View.VISIBLE);
                    UserTweetsAdapter tweetsAdapter = new UserTweetsAdapter(ProfileActivity.this, (ArrayList<WingTweet>) msg.obj);
                    tweetList.setAdapter(tweetsAdapter);
                    break;
                case GOT_RELATIONSHIP:
                    basicInfo.toFollow.setText(mUser.isFollowing ? "Following" : "Follow");
                    break;
                case DESTROYED_RELATIONSHIP:
                case CREATED_RELATIONSHIP:
                    basicInfo.toFollowProgress.setVisibility(View.GONE);
                    basicInfo.toFollow.setText(mUser.isFollowing ? "Following" : "Follow");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        ViewHelper.setTranslationY(bannerImage, scrollY / 2);

        if (scrollY > mParallaxImageHeight * 1.2) {
            getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
            getActionBar().setTitle(mUser.name);
            getActionBar().show();

        } else if (scrollY > mParallaxImageHeight * 0.2) {
            getActionBar().hide();
        } else {
            getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getActionBar().setTitle("");
            getActionBar().show();
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private void setBackgroundAlpha(View view, float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        view.setBackgroundColor(a + rgb);

        if (alpha == 1) {
            getActionBar().setTitle("tinyao");
        } else {
            getActionBar().setTitle("");
        }
    }

    @Override
    protected void onDestroy() {
        asyncTwitter.shutdown();
        super.onDestroy();
    }


    /**
     * View Group for Basic UserInfo
     */
    private class BasicProfileCard {
        CircleImageView avatar;
        TextView name, screenName, desc, address, website;
        TextView tweets, followers, followings;
        TextView toTweet, toFollow;
        View toFollowV, toFollowProgress;

        public BasicProfileCard(Activity context) {
            avatar = (CircleImageView) context.findViewById(R.id.user_avatar);
            name = (TextView) context.findViewById(R.id.user_name);
            screenName = (TextView) context.findViewById(R.id.user_screen_name);
            desc = (TextView) context.findViewById(R.id.user_desc);
            address = (TextView) context.findViewById(R.id.user_addr);
            website = (TextView) context.findViewById(R.id.user_web);
            tweets = (TextView) context.findViewById(R.id.user_tweet_count);
            followers = (TextView) context.findViewById(R.id.user_follower_count);
            followings = (TextView) context.findViewById(R.id.user_following_count);
            toTweet = (TextView) context.findViewById(R.id.user_to_tweet);
            toFollow = (TextView) context.findViewById(R.id.user_to_follow);
            toFollowV = context.findViewById(R.id.user_to_follow_v);
            toFollowProgress =  context.findViewById(R.id.user_to_follow_progressbar);

            toFollowV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUser.isFollowing) {
                        // show dialog to confirm unfollow

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this)
                                .setMessage("Stop following " + mUser.name + " ?")
                                .setTitle("Unfollow")
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        toFollowProgress.setVisibility(View.VISIBLE);
                                        Log.d("DEBUG", "UNFOLLOW ... " + mUser.screenName + " - " + mUser.user_id);
                                        asyncTwitter.destroyFriendship(mUser.user_id);
                                    }
                                })
                                .setNegativeButton(R.string.no, null);
                        builder.create().show();
                    } else {
                        // follow
                        toFollowProgress.setVisibility(View.VISIBLE);
                        Log.d("DEBUG", "FOLLOW ... " + mUser.screenName + " - " + mUser.user_id);
                        asyncTwitter.createFriendship(mUser.user_id);
                    }
                }
            });

            toTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setUserInfo(WingUser user) {
            name.setText(user.name);
            screenName.setText(user.screenName);

            desc.setText(user.desc);
            address.setText(user.location);
            website.setText(SpannableStringUtils.span(user.website!=null ? user.website : ""));
            tweets.setText("" + user.tweetCount);
            followers.setText("" + user.followerCount);
            followings.setText("" + user.followingCount);

            toFollow.setText(user.isFollowing ? "Following" : "Follow");

            if (user.desc != null && !user.desc.isEmpty())
                desc.setVisibility(View.VISIBLE);
            if (user.location != null && !user.location.isEmpty())
                address.setVisibility(View.VISIBLE);
            if (user.website != null && !user.website.isEmpty())
                website.setVisibility(View.VISIBLE);

            Picasso.with(ProfileActivity.this)
                    .load(user.avatar)
                    .into(avatar);

            if (user.banner != null) {
                Picasso.with(ProfileActivity.this)
                        .load(user.banner)
                        .error(R.color.primary)
                        .into(bannerImage);
            } else {
                Log.d("DEBUG", "currentY: " + scrollView.getCurrentScrollY()
                    + " -- Y: " + scrollView.getScrollY());
                if (scrollView.getCurrentScrollY() == 0 && user.bannerColor!=null && !user.bannerColor.isEmpty()) {
                    Log.d("DEBUG", "banner color: " + user.bannerColor);
                    bannerImage.setBackgroundColor(Color.parseColor("#FF" + user.bannerColor));
                }
            }
        }

//        private void stripUnderlines(TextView textView) {
//            Spannable s = (Spannable)textView.getText();
//            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
//            for (URLSpan span: spans) {
//                int start = s.getSpanStart(span);
//                int end = s.getSpanEnd(span);
//                s.removeSpan(span);
//                span = new URLSpanNoUnderline(span.getURL());
//                s.setSpan(span, start, end, 0);
//            }
//            textView.setText(s);
//        }
//
//        class URLSpanNoUnderline extends URLSpan {
//            public URLSpanNoUnderline(String url) {
//                super(url);
//            }
//            @Override public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // ToDo: When the avatar View not in screen, cancel the element-shared-transition on it
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            asyncTwitter.shutdown();
//            if (scrollView.getCurrentScrollY() > (getResources().getDimensionPixelSize(R.dimen.parallax_image_height) * 0.5) ) {
//                getWindow().setTransition
//            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
