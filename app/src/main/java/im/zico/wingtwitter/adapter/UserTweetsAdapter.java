package im.zico.wingtwitter.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.TweetDetailActivity;
import im.zico.wingtwitter.ui.view.HtmlTextView;
import im.zico.wingtwitter.utils.HackyMovementMethod;
import im.zico.wingtwitter.utils.SpannableStringUtils;
import im.zico.wingtwitter.utils.Utils;

/**
 * Created by tinyao on 12/4/14.
 */
public class UserTweetsAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private ListView mListView;
    private ArrayList<WingTweet> tweets;
    private Context mContext;

    public UserTweetsAdapter(Context context, ArrayList<WingTweet> tweets) {
        this.mContext = context;
        mLayoutInflater = ((Activity) context).getLayoutInflater();
        this.tweets = tweets;
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
            convertView = mLayoutInflater.inflate(R.layout.layout_profile_tweet_item, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        WingTweet tweet = tweets.get(position);

        Picasso.with(mContext).load(tweet.avatar_url)
                .fit()
                .into(holder.avatar);

//        if (tweet.retweet_id != -1) {
//            holder.retweeted.setVisibility(View.VISIBLE);
//            holder.retweeted.setText(tweet.retweeted_by_user_name + " retweeted");
//        } else {
//            holder.retweeted.setVisibility(View.GONE);
//        }

        holder.name.setText(tweet.user_name);
        holder.screenName.setText("@" + tweet.screen_name);
        holder.time.setText("" + Utils.getTimeAgo(tweet.created_at));
        holder.content.setHtmlText(tweet.content_html);
//        holder.content.setText(SpannableStringUtils.span(tweet.content));
//        holder.content.setMovementMethod(HackyMovementMethod.getInstance());

        return convertView;
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class Holder {

//        public TextView retweeted;
        public CircleImageView avatar;
        public TextView name;
        public TextView screenName;
        public HtmlTextView content;
        public TextView time;
//        public LinearLayout actionSlide;
//        public View mainContent;

        public Holder(View view) {
//            retweeted = (TextView) view.findViewById(R.id.retweet_hint);
            avatar = (CircleImageView) view.findViewById(R.id.user_avatar);
            name = (TextView) view.findViewById(R.id.user_name);
            screenName = (TextView) view.findViewById(R.id.user_screen_name);
            content = (HtmlTextView) view.findViewById(R.id.tweet_content);
            time = (TextView) view.findViewById(R.id.tweet_time);
//            actionSlide = (LinearLayout) view.findViewById(R.id.expandable);
//            mainContent = view.findViewById(R.id.main_card_content);
        }
    }

//    private void setLinkable(TextView textView) {
//        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
//            public final String transformUrl(final Matcher match, String url) {
//                return match.group();
//            }
//        };
//
//        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
//        String mentionScheme = "http://www.twitter.com/";
//        Linkify.addLinks(textView, mentionPattern, mentionScheme, null, filter);
//
//        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
//        String hashtagScheme = "http://www.twitter.com/search/";
//        Linkify.addLinks(textView, hashtagPattern, hashtagScheme, null, filter);
//
//        Pattern urlPattern = Patterns.WEB_URL;
//        Linkify.addLinks(textView, urlPattern, null, null, filter);
//    }

//    public static void expand(final View v) {
//        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        final int targetHeight = v.getMeasuredHeight();
//
//        v.getLayoutParams().height = 0;
//        v.setVisibility(View.VISIBLE);
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                v.getLayoutParams().height = interpolatedTime == 1
//                        ? LinearLayout.LayoutParams.WRAP_CONTENT
//                        : (int) (targetHeight * interpolatedTime);
//                v.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        // 1dp/ms
//        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
//        v.startAnimation(a);
//    }
//
//    public static void collapse(final View v) {
//        final int initialHeight = v.getMeasuredHeight();
//
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1) {
//                    v.setVisibility(View.GONE);
//                } else {
//                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
//                    v.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        // 1dp/ms
//        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
//        v.startAnimation(a);
//    }
}
