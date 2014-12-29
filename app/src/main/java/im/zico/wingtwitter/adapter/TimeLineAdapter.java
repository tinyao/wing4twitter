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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.ProfileActivity;
import im.zico.wingtwitter.ui.TweetDetailActivity;
import im.zico.wingtwitter.ui.view.HtmlTextView;
import im.zico.wingtwitter.ui.view.TweetListView;
import im.zico.wingtwitter.utils.Utils;

/**
 * Created by tinyao on 12/4/14.
 */
public class TimeLineAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private TweetListView mListView;

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
        final WingTweet tweet = WingTweet.fromCursor(cursor);

        Picasso.with(context).load(tweet.avatar_url)
//                .placeholder(R.drawable.ic_avatar_placeholder)
//                .fit()
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

        holder.actionRow1.setVisibility(View.VISIBLE);
        holder.actionRow2.setVisibility(View.GONE);

        holder.actionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "Action More ... ");
                if(holder.actionRow1.getVisibility() == View.VISIBLE) {
                    Animation animOut = AnimationUtils.loadAnimation(context,
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
                    Animation animOut2 = AnimationUtils.loadAnimation(context,
                            R.anim.slide_bottom_in);
                    holder.actionRow2.setVisibility(View.VISIBLE);
                    holder.actionRow1.startAnimation(animOut);
                    holder.actionRow2.startAnimation(animOut2);
                } else {
                    holder.actionRow1.setVisibility(View.VISIBLE);

                    Animation animOut = AnimationUtils.loadAnimation(context,
                            R.anim.slide_top_in);
                    Animation animOut2 = AnimationUtils.loadAnimation(context,
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

        boolean isActionMore  = false;

        public View showDetail, actionRow1, actionRow2 ,actionMore;


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

            actionRow1 = view.findViewById(R.id.expand_action_row_1);
            actionRow2 = view.findViewById(R.id.expand_action_row_2);
            actionMore = view.findViewById(R.id.expand_action_more);
        }
    }
}
