
package im.zico.wingtwitter.dao;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import im.zico.wingtwitter.WingApp;

/**
 * Created by Issac on 7/18/13.
 */
public class WingDataProvider extends ContentProvider {

    private static final String TAG = "WingDataProvider";
    static final Object DBLock = new Object();

    public static final String AUTHORITY = "im.zico.wing.provider";
    public static final String SCHEME = "content://";

    public static final String PATH_STATUSES = "/" + WingStore.TweetColumns.TABLE_NAME;
    public static final String PATH_USERS = "/" + WingStore.UserColumns.TABLE_NAME;
    public static final String PATH_MENTIONS = "/" + WingStore.MentionedCollumns.TABLE_NAME;
    public static final String PATH_FAVORITES = "/" + WingStore.FavoriteCollumns.TABLE_NAME;

    public static final Uri STATUS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_STATUSES);
    public static final Uri USER_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_USERS);
    public static final Uri MENTION_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_MENTIONS);
    public static final Uri FAVORITE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_FAVORITES);

    /*
     * MIME type definitions
     */
    public static final String STATUS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zico.wing.status";
    public static final String MENTION_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zico.wing.mention";
    public static final String FAVORITE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zico.wing.favorite";
    public static final String USER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zico.wing.user";

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, WingStore.TweetColumns.TABLE_NAME, WingStore.TYPE_TWEET);
        sUriMatcher.addURI(AUTHORITY, WingStore.MentionedCollumns.TABLE_NAME, WingStore.TYPE_MENTION);
        sUriMatcher.addURI(AUTHORITY, WingStore.FavoriteCollumns.TABLE_NAME,  WingStore.TYPE_FAVORITE);
        sUriMatcher.addURI(AUTHORITY, WingStore.UserColumns.TABLE_NAME,  WingStore.TYPE_USER);
    }

    private static MSQLiteOpenHelper mDBHelper;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (DBLock) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table = matchTable(uri);
            queryBuilder.setTables(table);

            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            Cursor cursor = queryBuilder.query(db, // The database to
                                                   // queryFromDB
                    projection, // The columns to return from the queryFromDB
                    selection, // The columns for the where clause
                    selectionArgs, // The values for the where clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    sortOrder // The sort order
                    );

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case WingStore.TYPE_TWEET:
                return STATUS_CONTENT_TYPE;
            case WingStore.TYPE_MENTION:
                return MENTION_CONTENT_TYPE;
            case WingStore.TYPE_USER:
                return USER_CONTENT_TYPE;
            case WingStore.TYPE_FAVORITE:
                return FAVORITE_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (DBLock) {
            String table = matchTable(uri);
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            long rowId = 0;
            db.beginTransaction();
            try {
                rowId = db.insert(table, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                db.endTransaction();
            }
            if (rowId > 0) {
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();

            int count = 0;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.delete(table, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count;
            String table = matchTable(uri);
            db.beginTransaction();
            try {
                count = db.update(table, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }

    public static MSQLiteOpenHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = new MSQLiteOpenHelper(WingApp.getContext());
        }
        return mDBHelper;
    }

    private String matchTable(Uri uri) {
        String table;
        switch (sUriMatcher.match(uri)) {
            case WingStore.TYPE_TWEET:
                table = WingStore.TweetColumns.TABLE_NAME;
                break;
            case WingStore.TYPE_MENTION:
                table = WingStore.MentionedCollumns.TABLE_NAME;
                break;
            case WingStore.TYPE_USER:
                table = WingStore.UserColumns.TABLE_NAME;
                break;
            case WingStore.TYPE_FAVORITE:
                table = WingStore.FavoriteCollumns.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table;
    }

}
