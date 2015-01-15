package im.zico.wingtwitter.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.ProfileActivity;
import im.zico.wingtwitter.ui.TweetDetailActivity;
import im.zico.wingtwitter.ui.view.HtmlTextView;
import im.zico.wingtwitter.ui.view.TweetListView;
import im.zico.wingtwitter.utils.PrefKey;
import im.zico.wingtwitter.utils.PreferencesManager;
import im.zico.wingtwitter.utils.TweetUtils;
import im.zico.wingtwitter.utils.Utils;

/**
 * Created by tinyao on 12/4/14.
 */
public class UserTimeLineAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private TweetListView mListView;
    private ArrayList<WingTweet> tweets;
    private Context mContext;

    public UserTimeLineAdapter(TweetListView mListView, ArrayList<WingTweet> tweets) {
        this.mListView = mListView;
        this.mContext = mListView.getContext();
        this.tweets = tweets;
        mLayoutInflater = ((Activity) mContext).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public WingTweet getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_tweet_card, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final WingTweet tweet = tweets.get(position);

        Picasso.with(mContext).load(tweet.avatar_url)
                .into(holder.avatar);

        if (tweet.retweet_id != -1) {
            holder.retweeted.setVisibility(View.VISIBLE);
            holder.retweeted.setText(tweet.retweeted_by_user_name + " retweeted");
        } else {
            holder.retweeted.setVisibility(View.GONE);
        }

        boolean isMyTweet =
                (tweet.user_id ==
                        Long.valueOf(PreferencesManager.getInstance(WingApp.getContext()).getLongValue(PrefKey.KEY_USERID)));
        if (isMyTweet) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.INVISIBLE);
        }

        if (tweet.favorited) {
            holder.favMark.setVisibility(View.VISIBLE);
        } else {
            holder.favMark.setVisibility(View.INVISIBLE);
        }

        holder.name.setText(tweet.user_name);
        holder.screenName.setText("@" + tweet.screen_name);
        holder.content.setText(String.valueOf(tweet.content));
        holder.time.setText("" + Utils.getTimeAgo(tweet.created_at));

        holder.content.setHtmlText(tweet.content_html);

        if (tweet.mediaUrls != null && tweet.mediaUrls.length > 0) {
            holder.gallery.removeAllViews();
            for (int i = 0; i < tweet.mediaUrls.length; i++) {
                Log.d("DEBUG", tweet.user_name + " media load: " + tweet.mediaUrls[i]);
                TweetUtils.insertPhoto(mContext, holder.gallery,
                        tweet.mediaUrls, i);
            }

            holder.gallery.setVisibility(View.VISIBLE);
        } else {
            holder.gallery.setVisibility(View.GONE);
        }

        holder.showDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.collapse();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, TweetDetailActivity.class);
                        intent.putExtra("tweet_id", tweet.tweet_id);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, holder.mainContent, "card");
                        mContext.startActivity(intent, options.toBundle());
                    }
                }, 200);

            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                profileIntent.putExtra(WingStore.TweetColumns.USER_AVATAR_URL, tweet.avatar_url);
                profileIntent.putExtra(WingStore.TweetColumns.USER_ID, tweet.user_id);
                profileIntent.putExtra(WingStore.TweetColumns.USER_NAME, tweet.user_name);
                profileIntent.putExtra(WingStore.TweetColumns.USER_SCREEN_NAME, tweet.screen_name);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) mContext,
                                Pair.create((View) holder.avatar, "avatar"));
                mContext.startActivity(profileIntent, options.toBundle());
            }
        });

        holder.actionRow1.setVisibility(View.VISIBLE);
        holder.actionRow2.setVisibility(View.GONE);

        holder.actionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Action More ... ");
                if (holder.actionRow1.getVisibility() == View.VISIBLE) {
                    Animation animOut = AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_top_out);
                    animOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.actionRow1.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    Animation animOut2 = AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_bottom_in);
                    holder.actionRow2.setVisibility(View.VISIBLE);
                    holder.actionRow1.startAnimation(animOut);
                    holder.actionRow2.startAnimation(animOut2);
                } else {
                    holder.actionRow1.setVisibility(View.VISIBLE);

                    Animation animOut = AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_top_in);
                    Animation animOut2 = AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_bottom_out);
                    animOut2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.actionRow2.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    holder.actionRow1.setVisibility(View.VISIBLE);
                    holder.actionRow1.startAnimation(animOut);
                    holder.actionRow2.startAnimation(animOut2);
                }
            }
        });

        return convertView;
    }

    private class Holder {

        public TextView retweeted;
        public CircleImageView avatar;
        public TextView name;
        public TextView screenName;
        public HtmlTextView content;
        public TextView time;
        public LinearLayout actionSlide;
        public View mainContent;
        public View cardMore;
        public LinearLayout gallery;
        public View delete;
        public View favMark;

        public View showDetail, actionRow1, actionRow2, actionMore;

        public Holder(View view) {
            retweeted = (TextView) view.findViewById(R.id.retweet_hint);
            avatar = (CircleImageView) view.findViewById(R.id.user_avatar);
            name = (TextView) view.findViewById(R.id.user_name);
            screenName = (TextView) view.findViewById(R.id.user_screen_name);
            content = (HtmlTextView) view.findViewById(R.id.tweet_content);
            time = (TextView) view.findViewById(R.id.tweet_time);
            actionSlide = (LinearLayout) view.findViewById(R.id.expandable);
            mainContent = view.findViewById(R.id.main_card_content);
            cardMore = view.findViewById(R.id.tweet_card_more);
            showDetail = view.findViewById(R.id.expand_action_detail);
            delete = view.findViewById(R.id.expand_action_delete);
            favMark = view.findViewById(R.id.tweet_fav_mark);

            actionRow1 = view.findViewById(R.id.expand_action_row_1);
            actionRow2 = view.findViewById(R.id.expand_action_row_2);
            actionMore = view.findViewById(R.id.expand_action_more);

            gallery = (LinearLayout) view.findViewById(R.id.tweet_gallery);
        }
    }
}
