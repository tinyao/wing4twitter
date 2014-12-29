package im.zico.wingtwitter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.type.WingUser;

/**
 * Created by tinyao on 12/5/14.
 */
public class WingDataHelper extends BaseDataHelper {

    public WingDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri(int type) {
        switch (type) {
            case WingStore.TYPE_TWEET:
                return WingDataProvider.STATUS_CONTENT_URI;
            case WingStore.TYPE_MENTION:
                return WingDataProvider.MENTION_CONTENT_URI;
            case WingStore.TYPE_USER:
                return WingDataProvider.USER_CONTENT_URI;
        }
        return null;
    }

    public WingTweet getTweet(long tweet_id) {
        WingTweet wingTweet = null;
        // Search the timeline cache
        Cursor cursor = query(WingStore.TYPE_TWEET, null, WingStore.TweetColumns.TWEET_ID + " = ?",
                new String[]{ "" + tweet_id }, null);
        if (cursor.moveToFirst()) {
            wingTweet = WingTweet.fromCursor(cursor);
        } else {
            // Search mentioned cache
            cursor = query(WingStore.TYPE_MENTION, null, WingStore.TweetColumns.TWEET_ID + " = ?",
                    new String[]{ "" + tweet_id }, null);
            if (cursor.moveToFirst()) {
                wingTweet = WingTweet.fromCursor(cursor);
            }
        }
        cursor.close();
        return wingTweet;
    }

    /**
     * Save Tweet List
     * @param wingTweets
     */
    public synchronized void saveAll(List<WingTweet> wingTweets) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();

        for (WingTweet wingTweet : wingTweets) {
            ContentValues values = wingTweet.toContentValues();
            contentValues.add(values);
        }

        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(WingStore.TYPE_TWEET, contentValues.toArray(valueArray));
    }

    /**
     * Save Tweet List
     * @param wingTweets
     */
    public synchronized void saveAllMention(List<WingTweet> wingTweets) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();

        for (WingTweet wingTweet : wingTweets) {
            ContentValues values = wingTweet.toContentValues();
            contentValues.add(values);
        }

        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(WingStore.TYPE_MENTION, contentValues.toArray(valueArray));
    }

    /**
     * Save single tweet
     * @param wingTweet
     */
    public void saveMention(WingTweet wingTweet) {
        if(!isTweetExisted(wingTweet.tweet_id)) {
            ContentValues values = wingTweet.toContentValues();
            insert(WingStore.TYPE_MENTION, values);
        }
    }


    /**
     * Save single tweet
     * @param wingTweet
     */
    public void save(WingTweet wingTweet) {
        if(!isTweetExisted(wingTweet.tweet_id)) {
            ContentValues values = wingTweet.toContentValues();
            insert(WingStore.TYPE_TWEET, values);
        }
    }

    /**
     * Save single user
     * @param wingUser
     */
    public synchronized void save(WingUser wingUser) {
        if(!isUserExsisted(wingUser.user_id)) {
            ContentValues values = wingUser.toContentValues();
            insert(WingStore.TYPE_USER, values);
        } else {
            ContentValues values = wingUser.toContentValues();
            update(WingStore.TYPE_USER, values,
                    WingStore.UserColumns.USER_ID + "=?", new String[]{ wingUser.user_id+"" });
        }
    }

    public WingUser getUser(long userId) {
        WingUser wingUser = null;
        Cursor cursor = query(WingStore.TYPE_USER, null, WingStore.UserColumns.USER_ID + " = ?",
                new String[]{ "" + userId }, null);
        if (cursor.getCount()>0 && cursor.moveToFirst()) {
            wingUser = new WingUser(cursor);
        }
        cursor.close();
        return wingUser;
    }

    public WingUser getUser(String screenName) {
        WingUser wingUser = null;
        Cursor cursor = query(WingStore.TYPE_USER, null, WingStore.UserColumns.SCREEN_NAME + " = ?",
                new String[]{ "" + screenName }, null);
        if (cursor.getCount()>0 && cursor.moveToFirst()) {
            wingUser = new WingUser(cursor);
        }
        cursor.close();
        return wingUser;
    }

    /**
     *
     * @param tweet
     */
    public void delete(WingTweet tweet) {
        delete(getContentUri(WingStore.TYPE_TWEET),
                WingStore.TweetColumns.TWEET_ID + " = ?", new String[]{ "" + tweet.tweet_id });
    }

    public int deletePreviousTweets(long tweet_id) {
        return delete(getContentUri(WingStore.TYPE_TWEET), WingStore.TweetColumns.TWEET_ID + " < ?", new String[]{ "" + tweet_id });
    }

    public int deleteAllTweets() {
        return delete(getContentUri(WingStore.TYPE_TWEET),null, null);
    }

//    public int clearTweets() {
//        // only keep 40 tweets
//
//    }

    public boolean isTweetExisted(long tweet_id) {
        Cursor c = query(getContentUri(WingStore.TYPE_TWEET), null, WingStore.TweetColumns.TWEET_ID + " = ?",
                new String[]{ "" + tweet_id}, null);
        boolean existed = c!=null && c.getCount() > 0;
        c.close();
        return existed;
    }

    private boolean isUserExsisted(long user_id) {
        Cursor c = query(getContentUri(WingStore.TYPE_USER), null, WingStore.UserColumns.USER_ID + " = ?",
                new String[]{ "" + user_id}, null);
        boolean existed = ( c!=null && c.getCount() > 0 );
        c.close();
        return existed;
    }

    public CursorLoader getCursorLoader(int type) {
        switch (type) {
            case WingStore.TYPE_TWEET:
                return new CursorLoader(getContext(), getContentUri(type), null, null,
                        null, WingStore.TweetColumns.TWEET_ID + " DESC");
            case WingStore.TYPE_MENTION:
                return new CursorLoader(getContext(), getContentUri(type), null, null,
                        null, WingStore.TweetColumns.TWEET_ID + " DESC");
            case WingStore.TYPE_USER:
                return new CursorLoader(getContext(), getContentUri(type), null, null,
                        null, WingStore.UserColumns.USER_ID + " DESC");
        }
        return null;
    }

}
