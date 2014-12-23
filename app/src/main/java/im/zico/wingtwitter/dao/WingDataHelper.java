package im.zico.wingtwitter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import im.zico.wingtwitter.type.WingTweet;

/**
 * Created by tinyao on 12/5/14.
 */
public class WingDataHelper extends BaseDataHelper {

    public WingDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return WingDataProvider.STATUS_CONTENT_URI;
    }

    public WingTweet query(long id) {
        WingTweet wingTweet = null;
        Cursor cursor = query(null, null, null, null);
        if (cursor.moveToFirst()) {
            wingTweet = WingTweet.fromCursor(cursor);
        }
        cursor.close();
        return wingTweet;
    }

    public WingTweet getTweet(long tweet_id) {
        WingTweet wingTweet = null;
        Cursor cursor = query(null, WingStore.TweetColumns.TWEET_ID + " = ?",
                new String[]{ "" + tweet_id }, null);
        if (cursor.moveToFirst()) {
            wingTweet = WingTweet.fromCursor(cursor);
        }
        cursor.close();
        return wingTweet;
    }

    public void bulkInsert(List<WingTweet> wingTweets) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();

        for (WingTweet wingTweet : wingTweets) {
            ContentValues values = wingTweet.toContentValues();
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public void insert(WingTweet wingTweet) {
        if(!isExisted(wingTweet.tweet_id)) {
            ContentValues values = wingTweet.toContentValues();
            insert(values);
        }
    }

    public void delete(long id) {

    }

    public int deletePrevious(long tweet_id) {
        return delete(getContentUri(), WingStore.TweetColumns.TWEET_ID + " < ?", new String[]{ "" + tweet_id });
    }

    public boolean isExisted(long tweet_id) {
        Cursor c = query(getContentUri(), null, WingStore.TweetColumns.TWEET_ID + " = ?",
                new String[]{ "" + tweet_id}, null);
        boolean existed = c!=null && c.getCount() > 0;
        c.close();
        return existed;
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null,
                null, WingStore.TweetColumns.TWEET_ID + " DESC" + " LIMIT 20");
    }

}
