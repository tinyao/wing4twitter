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
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.util.Linkify;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.ProfileActivity;
import im.zico.wingtwitter.ui.TweetDetailActivity;
import im.zico.wingtwitter.ui.view.HackyTextView;
import im.zico.wingtwitter.ui.view.HtmlTextView;
import im.zico.wingtwitter.ui.view.TweetListView;
import im.zico.wingtwitter.utils.HackyMovementMethod;
import im.zico.wingtwitter.utils.SpannableStringUtils;
import im.zico.wingtwitter.utils.Utils;

/**
 * Created by tinyao on 12/4/14.
 */
public class TimeLineAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    private TweetListView mListView;

    private BitmapDrawable mDefaultAvatarBitmap = (BitmapDrawable) WingApp.getContext()
            .getResources().getDrawable(R.drawable.ic_launcher);

    private Drawable mDefaultImageDrawable = new ColorDrawable(Color.argb(255, 201, 201, 201));

    public TimeLineAdapter(Context context, TweetListView mListView) {
        super(context, null, false);
        this.mListView = mListView;
        mLayoutInflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public WingTweet getItem(int position) {
        getCursor().moveToPosition(position);
        return WingTweet.fromCursor(getCursor());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflater.inflate(R.layout.row_tweet_card, null);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Holder holder = getHolder(view);

//        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
//                + mListView.getHeaderViewsCount()));

        final WingTweet tweet = WingTweet.fromCursor(cursor);

        Picasso.with(context).load(tweet.avatar_url)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .fit()
                .into(holder.avatar);

        if (tweet.retweet_id != -1) {
            holder.retweeted.setVisibility(View.VISIBLE);
            holder.retweeted.setText(tweet.retweeted_by_user_name + " retweeted");
        } else {
            holder.retweeted.setVisibility(View.GONE);
        }

        holder.name.setText(tweet.user_name);
        holder.screenName.setText("@" + tweet.screen_name);
        holder.content.setText(String.valueOf(tweet.content));
        holder.time.setText("" + Utils.getTimeAgo(tweet.created_at));

        holder.content.setHtmlText(tweet.content_html);

        if(tweet.mediaUrls != null && tweet.mediaUrls.length >0 ) {
            Picasso.with(context)
                    .load(tweet.mediaUrls[0])

                    .into(holder.tweetPhoto);
            holder.tweetPhoto.setVisibility(View.VISIBLE);
        } else {
            holder.tweetPhoto.setVisibility(View.GONE);
        }

        holder.cardMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        holder.mainContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, TweetDetailActivity.class);
//                intent.putExtra("tweet_id", tweet.tweet_id);
//
////                ActivityOptions options;
////                if( tweet.retweet_id == -1 ) {
////                    options = ActivityOptions
////                            .makeSceneTransitionAnimation((Activity) context,
////                                    Pair.create((View) holder.name, "name"),
////                                    Pair.create((View) holder.avatar, "avatar"),
////                                    Pair.create((View) holder.screenName, "screenName"),
////                                    Pair.create((View) holder.content, "content"),
////                                    Pair.create((View) holder.time, "time"));
////                } else {
////                    options = ActivityOptions
////                            .makeSceneTransitionAnimation((Activity) context,
////                                    Pair.create((View) holder.name, "name"),
////                                    Pair.create((View) holder.avatar, "avatar"),
////                                    Pair.create((View) holder.screenName, "screenName"),
////                                    Pair.create((View) holder.content, "content"),
////                                    Pair.create((View) holder.retweeted, "retweet"),
////                                    Pair.create((View) holder.time, "time"));
////                }
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.mainContent, "card");
//                context.startActivity(intent, options.toBundle());
//
////                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());
//            }
//        });

        holder.showDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.collapse();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, TweetDetailActivity.class);
                        intent.putExtra("tweet_id", tweet.tweet_id);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.mainContent, "card");
                        context.startActivity(intent, options.toBundle());
                    }
                }, 200);

            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(context, ProfileActivity.class);
                profileIntent.putExtra(WingStore.TweetColumns.USER_AVATAR_URL, tweet.avatar_url);
                profileIntent.putExtra(WingStore.TweetColumns.USER_ID, tweet.user_id);
                profileIntent.putExtra(WingStore.TweetColumns.USER_NAME, tweet.user_name);
                profileIntent.putExtra(WingStore.TweetColumns.USER_SCREEN_NAME, tweet.screen_name);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) context,
                                Pair.create((View) holder.avatar, "avatar"));
                context.startActivity(profileIntent, options.toBundle());
            }
        });

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

        public TextView retweeted;
        public CircleImageView avatar;
        public TextView name;
        public TextView screenName;
        public HtmlTextView content;
        public TextView time;
        public LinearLayout actionSlide;
        public View mainContent;
        public ImageView tweetPhoto;
        public View cardMore;

        public View showDetail;

        public Holder(View view) {
            retweeted = (TextView) view.findViewById(R.id.retweet_hint);
            avatar = (CircleImageView) view.findViewById(R.id.user_avatar);
            name = (TextView) view.findViewById(R.id.user_name);
            screenName = (TextView) view.findViewById(R.id.user_screen_name);
            content = (HtmlTextView) view.findViewById(R.id.tweet_content);
            time = (TextView) view.findViewById(R.id.tweet_time);
            tweetPhoto = (ImageView) view.findViewById(R.id.tweet_photo);
            actionSlide = (LinearLayout) view.findViewById(R.id.expandable);
            mainContent = view.findViewById(R.id.main_card_content);
            cardMore = view.findViewById(R.id.tweet_card_more);
            showDetail = view.findViewById(R.id.expand_action_detail);
        }
    }

//    public boolean isDuplicated(long tweet_id) {
//        this.getItem(getCount()-1).
//
//    }

    private void setLinkable(TextView textView) {
        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        String mentionScheme = "http://www.twitter.com/";
        Linkify.addLinks(textView, mentionPattern, mentionScheme, null, filter);

        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
        String hashtagScheme = "http://www.twitter.com/search/";
        Linkify.addLinks(textView, hashtagPattern, hashtagScheme, null, filter);

        Pattern urlPattern = Patterns.WEB_URL;
        Linkify.addLinks(textView, urlPattern, null, null, filter);
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
