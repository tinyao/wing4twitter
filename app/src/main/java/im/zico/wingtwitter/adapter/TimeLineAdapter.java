package im.zico.wingtwitter.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.type.WingTweet;

/**
 * Created by tinyao on 12/4/14.
 */
public class TimeLineAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    private ListView mListView;

    private BitmapDrawable mDefaultAvatarBitmap = (BitmapDrawable) WingApp.getContext()
            .getResources().getDrawable(R.drawable.ic_launcher);

    private Drawable mDefaultImageDrawable = new ColorDrawable(Color.argb(255, 201, 201, 201));

    public TimeLineAdapter(Context context) {
        super(context, null, false);
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
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = getHolder(view);

//        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
//                + mListView.getHeaderViewsCount()));

        WingTweet tweet = WingTweet.fromCursor(cursor);

        Picasso.with(context).load(tweet.avatar_url).into(holder.avatar);

        holder.name.setText(tweet.user_name);
        holder.screenName.setText(tweet.screen_name);
        holder.content.setText(String.valueOf(tweet.content));
        holder.time.setText(String.valueOf(tweet.created_at));
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

        public ImageView avatar;
        public TextView name;
        public TextView screenName;
        public TextView content;
        public TextView time;

        public Holder(View view) {
            avatar = (ImageView) view.findViewById(R.id.user_avatar);
            name = (TextView) view.findViewById(R.id.user_name);
            screenName = (TextView) view.findViewById(R.id.user_screen_name);
            content = (TextView) view.findViewById(R.id.tweet_content);
            time = (TextView) view.findViewById(R.id.tweet_time);
        }
    }
}
