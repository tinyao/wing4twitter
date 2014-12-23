package im.zico.wingtwitter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import im.zico.wingtwitter.R;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.view.HackyTextView;
import im.zico.wingtwitter.utils.HackyMovementMethod;
import im.zico.wingtwitter.utils.SpannableStringUtils;
import im.zico.wingtwitter.utils.Utils;

/**
 * Created by tinyao on 12/18/14.
 */
public class ConversationTweetAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<WingTweet> tweets;

    public ConversationTweetAdapter(Context context, ArrayList<WingTweet> tweets) {
        mContext = context;
        mLayoutInflater =  LayoutInflater.from(context);
        this.tweets = tweets;
    }


    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ReplyHoder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_reply_tweet, parent, false);
            holder = new ReplyHoder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ReplyHoder) convertView.getTag();
        }

        WingTweet item = tweets.get(position);

        Picasso.with(mContext).load(item.avatar_url)
                .fit()
                .into(holder.avatar);

        holder.name.setText(item.user_name);
        holder.screenName.setText(item.screen_name);
        holder.content.setText(SpannableStringUtils.span(item.content));
        holder.content.setMovementMethod(HackyMovementMethod.getInstance());
        holder.time.setText(Utils.getTimeAgo(item.created_at));

        return convertView;
    }

    private class ReplyHoder {
        private CircleImageView avatar;
        private TextView name, screenName;
        private HackyTextView content;
        private TextView time;

        public ReplyHoder(View view) {
            avatar = (CircleImageView) view.findViewById(R.id.reply_user_avatar);
            name = (TextView) view.findViewById(R.id.reply_user_name);
            screenName = (TextView) view.findViewById(R.id.reply_user_screen_name);
            content = (HackyTextView) view.findViewById(R.id.reply_tweet_content);
            time = (TextView) view.findViewById(R.id.reply_tweet_time);
        }
    }

}
