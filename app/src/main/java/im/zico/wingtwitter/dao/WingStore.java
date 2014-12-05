package im.zico.wingtwitter.dao;

import android.provider.BaseColumns;

import im.zico.wingtwitter.utils.database.Column.*;
import im.zico.wingtwitter.utils.database.SQLiteTable;

/**
 * Created by tinyao on 12/5/14.
 */
public class WingStore {

    public static final String TYPE_PRIMARY_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_INT = "INTEGER";
    public static final String TYPE_INT_UNIQUE = "INTEGER UNIQUE";
    public static final String TYPE_BOOLEAN = "INTEGER(1)";
    public static final String TYPE_BOOLEAN_DEFAULT_TRUE = "INTEGER(1) DEFAULT 1";
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_TEXT_NOT_NULL = "TEXT NOT NULL";
    public static final String TYPE_TEXT_NOT_NULL_UNIQUE = "TEXT NOT NULL UNIQUE";

    public static final class Statuses implements BaseColumns {

        private Statuses() {}

        public static final String TABLE_NAME = "status";

        //================

        public static final String ACCOUNT_ID = "account_id";

        public static final String STATUS_ID = "status_id";

        public static final String USER_ID = "user_id";

        public static final String CREATED = "created_at";

//        public static final String CONTENT = "content";

//        public static final String BODY_HTML = "body_html";
//
//        public static final String BODY_PLAIN = "body_plain";
//
//        public static final String BODY_UNESCAPED = "body_unescaped";
//
        public static final String USER_NAME = "name";
//
//        public static final String USER_SCREEN_NAME = "screen_name";
//
//        public static final String USER_PROFILE_AVATAR_URL = "profile_avatar_url";
//
//        public static final String IN_REPLY_TO_STATUS_ID = "in_reply_to_status_id";
//
//        public static final String IN_REPLY_TO_USER_ID = "in_reply_to_user_id";
//
//        public static final String IN_REPLY_TO_USER_NAME = "in_reply_to_user_name";
//
//        public static final String IN_REPLY_TO_USER_SCREEN_NAME = "in_reply_to_user_screen_name";
//
//        public static final String SOURCE = "source";
//
//        public static final String LOCATION = "location";
//
//        public static final String RETWEET_COUNT = "retweet_count";
//
//        public static final String FAVORITE_COUNT = "favorite_count";
//
//        public static final String RETWEET_ID = "retweet_id";
//
//        public static final String RETWEET_TIME = "retweet_time";
//
//        public static final String RETWEETED_BY_USER_ID = "retweeted_by_user_id";
//
//        public static final String RETWEETED_BY_USER_NAME = "retweeted_by_user_name";
//
//        public static final String RETWEETED_BY_USER_SCREEN_NAME = "retweeted_by_user_screen_name";
//
//        public static final String MY_RETWEET_ID = "my_retweet_id";
//
//        public static final String IS_RETWEET = "is_retweet";
//
//        public static final String IS_FAVORITE = "is_favorite";
//
//        public static final String IS_PROTECTED = "is_protected";
//
//        public static final String IS_VERIFIED = "is_verified";
//
//        public static final String IS_FOLLOWING = "is_following";
//
//        public static final String IS_GAP = "is_gap";
//
//        public static final String IS_POSSIBLY_SENSITIVE = "is_prossibly_sensitive";
//
//        public static final String MEDIA = "media";
//
//        public static final String FIRST_MEDIA = "first_media";
//
//        public static final String MENTIONS = "mentions";

//        public static final String[] COLUMNS = new String[] { ACCOUNT_ID, STATUS_ID, USER_ID, STATUS_TIME }
////                BODY_HTML, BODY_PLAIN, BODY_UNESCAPED, USER_NAME, USER_SCREEN_NAME, USER_PROFILE_AVATAR_URL,
////                IN_REPLY_TO_STATUS_ID, IN_REPLY_TO_USER_ID, IN_REPLY_TO_USER_NAME, IN_REPLY_TO_USER_SCREEN_NAME,
////                SOURCE, LOCATION, RETWEET_COUNT, FAVORITE_COUNT, RETWEET_ID, RETWEETED_BY_USER_ID,
////                RETWEETED_BY_USER_NAME, RETWEETED_BY_USER_SCREEN_NAME, MY_RETWEET_ID, IS_RETWEET, IS_FAVORITE,
////                IS_PROTECTED, IS_VERIFIED, IS_FOLLOWING, IS_GAP, IS_POSSIBLY_SENSITIVE, MEDIA, FIRST_MEDIA, MENTIONS };
//
//        public static final DataType[] TYPES = new DataType[] { DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER,
//                DataType.TEXT, DataType.TEXT, DataType.TEXT, DataType.TEXT, DataType.TEXT, DataType.TEXT, DataType.INTEGER, DataType.INTEGER, DataType.TEXT,
//                DataType.TEXT, DataType.TEXT, DataType.TEXT, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT,
//                DataType.INTEGER, DataType.INTEGER_1, DataType.INTEGER_1, DataType.INTEGER_1, DataType.INTEGER_1, DataType.INTEGER_1, DataType.INTEGER_1,
//                DataType.INTEGER_1, DataType.TEXT, DataType.TEXT, DataType.TEXT };

        public static final String[] COLUMNS = new String[] {ACCOUNT_ID, STATUS_ID, USER_ID, CREATED, USER_NAME};

        public static final DataType[] TYPES = new DataType[] {DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.TEXT};

//        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME).addColumns(COLUMNS, TYPES);
    }


//    private static String createTable(final String tableName, final String[] columns, final String[] types,
//                                      final boolean createIfNotExists) {
//        final SQLCreateTableQuery.Builder qb = SQLQueryBuilder.createTable(createIfNotExists, tableName);
//        qb.columns(NewColumn.createNewColumns(columns, types));
//        return qb.buildSQL();
//    }



}
