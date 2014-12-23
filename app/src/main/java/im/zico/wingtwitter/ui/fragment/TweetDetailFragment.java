package im.zico.wingtwitter.ui.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingDataHelper;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.ProfileActivity;
import im.zico.wingtwitter.ui.TweetDetailActivity;
import im.zico.wingtwitter.utils.HackyMovementMethod;
import im.zico.wingtwitter.utils.SpannableStringUtils;
import im.zico.wingtwitter.utils.Utils;

import im.zico.wingtwitter.dao.WingStore.*;
import twitter4j.AsyncTwitter;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.User;

/**
 * Created by tinyao on 12/10/14.
 */
public class TweetDetailFragment extends Fragment {

    private WingTweet tweet;
    private AsyncTwitter asyncTwitter;


    public TweetDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        long tweet_id = intent.getLongExtra("tweet_id", 0);

        WingDataHelper dbHelper = new WingDataHelper(getActivity());
        tweet = dbHelper.getTweet(tweet_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_detail, container, false);

        final Holder holder = new Holder(rootView);
        Picasso.with(getActivity()).load(tweet.avatar_url).into(holder.avatar);

        if (tweet.retweet_id != -1) {
            holder.retweeted.setVisibility(View.VISIBLE);
            holder.retweeted.setText(tweet.retweeted_by_user_name + " retweeted");
        } else {
            holder.retweeted.setVisibility(View.GONE);
        }

        holder.name.setText(tweet.user_name);
        holder.screenName.setText("@" + tweet.screen_name);
        holder.content.setText(String.valueOf(tweet.content));
        holder.time.setText(Utils.getTimeAgo(tweet.created_at));
        holder.content.setText(SpannableStringUtils.span(tweet.content));
        holder.content.setMovementMethod(HackyMovementMethod.getInstance());

        Spannable s = (Spannable) Html.fromHtml(tweet.source);
        for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }

        if (tweet.source.equals(WingApp.TWITTER_APP_NAME)) {
            holder.tvia.setText(Html.fromHtml(WingApp.TWITTER_APP_SOURCE));
        } else {
            holder.tvia.setText(Html.escapeHtml(tweet.source));
        }

        holder.counts.setText("" + tweet.retweet_count);
        holder.favorites.setText("" + tweet.favorite_count);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                profileIntent.putExtra(TweetColumns.USER_AVATAR_URL, tweet.avatar_url);
                profileIntent.putExtra(TweetColumns.USER_ID, tweet.user_id);
                profileIntent.putExtra(TweetColumns.USER_NAME, tweet.user_name);
                profileIntent.putExtra(TweetColumns.USER_SCREEN_NAME, tweet.screen_name);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(),
                                Pair.create((View) holder.avatar, "avatar"));
                startActivity(profileIntent, options.toBundle());
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchConversation();
    }

    private void fetchConversation() {

        asyncTwitter = WingApp.newTwitterInstance();
        asyncTwitter.addListener(listener);

        Log.d("DEBUG", "1. Fetch conversation");
        if (tweet.in_reply_status_id != 0) {
            // has conversation before this
            asyncTwitter.showStatus(tweet.in_reply_status_id);
        } else {
            // conversation all after this
            searchAfterReplies();
        }
    }

    private void searchAfterReplies() {
        Query query = new Query("@" + tweet.screen_name).sinceId(tweet.tweet_id);
        asyncTwitter.search(query);
    }

    private TwitterListener listener = new TwitterAdapter() {
        @Override
        public void gotRetweets(ResponseList<Status> retweets) {
            Log.d("DEBUG", "retweets: " + retweets);
            super.gotRetweets(retweets);
        }

        @Override
        public void gotUserDetail(User user) {
            Log.d("DEBUG", "users: " + user);
            super.gotUserDetail(user);
        }

        @Override
        public void gotShowStatus(Status status) {
            if (status == null) return;

            conversations.add(0, new WingTweet(status));

            if (status.getInReplyToStatusId() != 0) {
                asyncTwitter.showStatus(status.getInReplyToStatusId());
            } else {
                mHandler.sendEmptyMessage(GOT_BEFORE_REPLIES);
            }

            super.gotShowStatus(status);
        }

        @Override
        public void searched(QueryResult queryResult) {
            ArrayList<WingTweet> afterReplies = new ArrayList<WingTweet>();

            List<Status> statuses = queryResult.getTweets();
            Log.d("DEBUG", "Replies Searched: " + statuses.size());
            for (Status status : statuses) {
                if (status.getInReplyToStatusId() == tweet.tweet_id) {
                    afterReplies.add(new WingTweet(status));
                }
            }
            conversations.addAll(afterReplies);

            mHandler.sendEmptyMessage(GOT_AFTER_REPLIES);
            super.searched(queryResult);
        }

    };

    private static final int GOT_BEFORE_REPLIES = 1;
    private static final int GOT_AFTER_REPLIES = 2;

    private ArrayList<WingTweet> conversations = new ArrayList<>();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GOT_AFTER_REPLIES:
                    Log.d("DEBUG", "GOT AFTER REPLIES, SHOW UI");
                    Log.d("DEBUG", "Conversation (" + conversations.size() + "): " + conversations);

                    break;
                case GOT_BEFORE_REPLIES:
                    Log.d("DEBUG", "GOT BEFORE REPLIES");
                    searchAfterReplies();
                    break;
            }
        }
    };

    private class Holder {

        public TextView retweeted;
        public CircleImageView avatar;
        public TextView name;
        public TextView screenName;
        public TextView content;
        public TextView time;
        public TextView tvia;
        public TextView counts;
        public TextView favorites;

        public Holder(View view) {
            retweeted = (TextView) view.findViewById(R.id.retweet_hint);
            avatar = (CircleImageView) view.findViewById(R.id.user_avatar);
            name = (TextView) view.findViewById(R.id.user_name);
            screenName = (TextView) view.findViewById(R.id.user_screen_name);
            content = (TextView) view.findViewById(R.id.tweet_content);
            time = (TextView) view.findViewById(R.id.tweet_time);
            tvia = (TextView) view.findViewById(R.id.tweet_via);
            counts = (TextView) view.findViewById(R.id.retweet_count);
            favorites = (TextView) view.findViewById(R.id.favorite_count);
        }
    }

    @Override
    public void onDetach() {
        asyncTwitter.shutdown();
        super.onDetach();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
