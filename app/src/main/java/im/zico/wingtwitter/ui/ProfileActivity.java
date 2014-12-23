package im.zico.wingtwitter.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.util.TypedValue;
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
import im.zico.wingtwitter.adapter.TimeLineAdapter;
import im.zico.wingtwitter.adapter.UserTweetsAdapter;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.type.WingUser;
import im.zico.wingtwitter.ui.view.NestedListView;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
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

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        bannerImage = (ImageView) findViewById(R.id.profile_bannder_image);
        getActionBar().setTitle("");
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);

        revealBannder();

        // previously invisible view
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_item, R.id.tv,
//            this.getResources().getStringArray(R.array.test));
//        listView.setAdapter(adapter);

//        int mPaddingOffset = getResources().getDimensionPixelSize(R.dimen.spacing_vertical_large);
//
//        // Set padding view for ListView. This is the flexible space.
//        View paddingView = new View(this);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
//                mParallaxImageHeight - mPaddingOffset);
//        paddingView.setLayoutParams(lp);
//
//        // This is required to disable header's list selector effect
//        paddingView.setClickable(true);
//
//        listView.addHeaderView(paddingView);
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        View v = inflater.inflate(R.layout.layout_profile_basic, null);
//        listView.addHeaderView(v);

        basicInfo = new BasicProfileCard(this);

        tweetList = (NestedListView) findViewById(R.id.user_profile_tweets_list);

        Intent intent = getIntent();
        userName = intent.getStringExtra(WingStore.TweetColumns.USER_NAME);
        Log.d("DEBUG", "avatar: " + intent.getStringExtra(WingStore.TweetColumns.USER_AVATAR_URL));
        Picasso.with(this).load(getIntent()
                .getStringExtra(WingStore.TweetColumns.USER_AVATAR_URL))
                .into(basicInfo.avatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        revealBannder();
                    }

                    @Override
                    public void onError() {

                    }
                });

        basicInfo.name.setText(intent.getStringExtra(WingStore.TweetColumns.USER_NAME));
        basicInfo.screenName.setText(intent.getStringExtra(WingStore.TweetColumns.USER_SCREEN_NAME));
        mUserId = intent.getLongExtra(WingStore.TweetColumns.USER_ID, -1);

        if (mUserId == -1) {
            finishAfterTransition();
        }

        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);

        // show User detail
        Log.d("DEBUG", "Get User Detail");
        asyncTwitter.showUser(mUserId);
        Log.d("DEBUG", "Get User Statuses ...");
        asyncTwitter.getUserTimeline(mUserId, new Paging().count(5));
        Log.d("DEBUG", "Get User Fav ...");
        asyncTwitter.getFavorites(mUserId);

        // mListBackgroundView makes ListView's background except header view.
//        mListBackgroundView = findViewById(R.id.list_background);
//        final View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
//        contentView.post(new Runnable() {
//            @Override
//            public void run() {
//                // mListBackgroundView's should fill its parent vertically
//                // but the height of the content view is 0 on 'onCreate'.
//                // So we should get it with post().
//                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
//            }
//        });


//        FadingActionBarHelper helper = new FadingActionBarHelper()
//                .actionBarBackground(R.drawable.img_user_cover_default)
//                .headerLayout(R.layout.layout_profile_basic)
//                .contentLayout(R.layout.activity_profile);
//        setContentView(helper.createView(this));
//        helper.initActionBar(this);
//
//        ListView listView = (ListView) findViewById(android.R.id.list);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void revealBannder() {

        bannerImage.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                if (!bannerImage.isAttachedToWindow()) return;

                int cx = bannerImage.getLeft(); //(bannerImage.getRight() + bannerImage.getLeft()) / 2;
                int cy = (bannerImage.getTop() + bannerImage.getBottom()) / 3;

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
            mUser = user;
            mHandler.sendEmptyMessage(GOT_USER_DETAIL);
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
    };

    private static final int GOT_USER_DETAIL = 0;
    private static final int GOT_USER_TWEETS = 1;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GOT_USER_DETAIL:
                    basicInfo.setUserInfo(mUser);
                    break;
                case GOT_USER_TWEETS:
                    UserTweetsAdapter tweetsAdapter = new UserTweetsAdapter(ProfileActivity.this, (ArrayList<WingTweet>) msg.obj);
                    tweetList.setAdapter(tweetsAdapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + listView.getPaddingTop() + listView.getPaddingBottom()
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

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

//        Log.d("DEBUG", "scrollY: " + scrollY);
        int baseColor = getResources().getColor(R.color.primary);

        if (scrollY > mParallaxImageHeight * 1.2) {
            getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));
            getActionBar().setTitle(userName);
            getActionBar().show();

        } else if(scrollY > mParallaxImageHeight * 0.2) {
            getActionBar().hide();
        } else {
            getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getActionBar().setTitle("");
            getActionBar().show();
        }


//        int baseColor = getResources().getColor(R.color.primary);
//        float alpha = Math.min(1, (scrollY - mParallaxImageHeight * 0.6f) / mParallaxImageHeight);
//
//        Log.d("DEBUG", "Alpha: " + alpha);
////                1 - (float) Math.max(0,
////                mParallaxImageHeight * 1.5 - scrollY) / (mParallaxImageHeight * 1.5f);
//        setBackgroundAlpha(mToolbarView, alpha, baseColor);
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
        Button follow;

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
        }

        public void setUserInfo(User user) {
            desc.setText(user.getDescription());
            address.setText(user.getLocation());
            website.setText(user.getURL());
            tweets.setText("" + user.getStatusesCount());
            followers.setText("" + user.getFollowersCount());
            followings.setText("" + user.getFriendsCount());

            if(user.getDescription()!=null && !user.getDescription().isEmpty())
                desc.setVisibility(View.VISIBLE);
            if(user.getLocation()!=null && !user.getLocation().isEmpty())
                address.setVisibility(View.VISIBLE);
            if(user.getURL()!=null && !user.getURL().isEmpty())
                website.setVisibility(View.VISIBLE);

            if (user.getProfileBannerMobileRetinaURL() != null) {
                Picasso.with(ProfileActivity.this)
                        .load(user.getProfileBannerMobileRetinaURL())
                        .error(R.color.primary)
                        .into(bannerImage);
            }
        }
    }


}
