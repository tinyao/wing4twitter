package im.zico.wingtwitter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import im.zico.wingtwitter.type.Tweet;

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

    public Tweet query(long id) {
        Tweet tweet = null;
        Cursor cursor = query(null, null, null, null);
        if (cursor.moveToFirst()) {
            tweet = Tweet.fromCursor(cursor);
        }
        cursor.close();
        return tweet;
    }

    public void bulkInsert(List<Tweet> tweets) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();

        for (Tweet tweet : tweets) {
            ContentValues values = tweet.toContentValues();
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public void insert(Tweet tweet) {
        ContentValues values = tweet.toContentValues();
        insert(values);
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null,
                null, WingStore.Statuses._ID + " ASC");
    }

}
