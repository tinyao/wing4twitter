
package im.zico.wingtwitter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

public abstract class BaseDataHelper {
    private Context mContext;

    public BaseDataHelper(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract Uri getContentUri(int type);

    public void notifyChange(int type) {
        mContext.getContentResolver().notifyChange(getContentUri(type), null);
    }

    protected final Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);
    }

    protected final Cursor query(int type, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(type), projection, selection,
                selectionArgs, sortOrder);
    }

    protected final Uri insert(int type, ContentValues values) {
        return mContext.getContentResolver().insert(getContentUri(type), values);
    }

    protected final int bulkInsert(int type, ContentValues[] values) {
        return mContext.getContentResolver().bulkInsert(getContentUri(type), values);
    }

    protected final int update(int type, ContentValues values, String where, String[] whereArgs) {
        return mContext.getContentResolver().update(getContentUri(type), values, where, whereArgs);
    }

    protected final int delete(Uri uri, String selection, String[] selectionArgs) {
        return mContext.getContentResolver().delete(uri, selection, selectionArgs);
    }

    protected final Cursor getList(int type, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(type), projection, selection,
                selectionArgs, sortOrder);
    }

//    public CursorLoader getCursorLoader(Context context) {
//        return getCursorLoader(context, null, null, null, null);
//    }

//    protected final CursorLoader getCursorLoader(Context context, String[] projection,
//            String selection, String[] selectionArgs, String sortOrder) {
//        return new CursorLoader(context, getContentUri(type), projection, selection, selectionArgs,
//                sortOrder);
//    }

}
