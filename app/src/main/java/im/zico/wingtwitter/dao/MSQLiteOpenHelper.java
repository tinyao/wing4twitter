package im.zico.wingtwitter.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import im.zico.wingtwitter.dao.WingStore.*;
import im.zico.wingtwitter.utils.database.Column;
import im.zico.wingtwitter.utils.database.SQLiteTable;
import twitter4j.User;

/**
 * Created by tinyao on 12/5/14.
 */
public class MSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "wing.db";

    private static final int VERSION = 1;

    public MSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable(TweetColumns.TABLE_NAME, TweetColumns.COLUMNS,
                TweetColumns.TYPES, TweetColumns.CONSTRAINTS, true));
        db.execSQL(createTable(UserColumns.TABLE_NAME, UserColumns.COLUMNS,
                UserColumns.TYPES, UserColumns.CONSTRAINTS, true));
        db.execSQL(createTable(MentionedCollumns.TABLE_NAME, MentionedCollumns.COLUMNS,
                MentionedCollumns.TYPES, MentionedCollumns.CONSTRAINTS, true));
        db.execSQL(createTable(FavoriteCollumns.TABLE_NAME, FavoriteCollumns.COLUMNS,
                FavoriteCollumns.TYPES, FavoriteCollumns.CONSTRAINTS, true));
        db.execSQL(createTable(CommonTweetColumns.TABLE_NAME, CommonTweetColumns.COLUMNS,
                CommonTweetColumns.TYPES, CommonTweetColumns.CONSTRAINTS, true));
    }

    private static String createTable(final String tableName, final String[] columns,
                                      final Column.DataType[] types, final Column.Constraint[] constraints,
                                      final boolean createIfNotExists) {
        SQLiteTable TABLE = new SQLiteTable(tableName).addColumns(columns, types, constraints);
        Log.d("DEBUG", "create table: " + TABLE.buildTableSQL());
        return TABLE.buildTableSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
