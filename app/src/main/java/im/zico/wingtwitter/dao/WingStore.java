package im.zico.wingtwitter.dao;

import android.provider.BaseColumns;

import im.zico.wingtwitter.utils.database.Column.*;
import im.zico.wingtwitter.utils.database.SQLiteTable;

/**
 * Created by tinyao on 12/5/14.
 */
public class WingStore {

    public static final class TweetColumns implements BaseColumns {

        private TweetColumns() {}

        public static final String TABLE_NAME = "status";

        public static final String ACCOUNT_ID = "account_id";

        public static final String TWEET_ID = "tweet_id";

        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_SCREEN_NAME = "user_screen_name";
        public static final String USER_AVATAR_URL = "user_avatar_url";

        public static final String CREATED = "created_at";
        public static final String CONTENT = "content";
        public static final String SOURCE = "source";

        public static final String IN_REPLY_TO_STATUS_ID = "in_reply_to_status_id";
        public static final String IN_REPLY_TO_USER_ID = "in_reply_to_user_id";
        public static final String IN_REPLY_TO_USER_NAME = "in_reply_to_user_name";
        public static final String IN_REPLY_TO_USER_SCREEN_NAME = "in_reply_to_user_screen_name";

        public static final String RETWEET_COUNT = "retweet_count";
        public static final String FAVORITE_COUNT = "favorite_count";
        public static final String FAVORITED = "favorited";

        public static final String RETWEET_ID = "retweet_id";
        public static final String RETWEET_TIME = "retweet_time";
        public static final String RETWEETED_BY_USER_ID = "retweeted_by_user_id";
        public static final String RETWEETED_BY_USER_NAME = "retweeted_by_user_name";
        public static final String RETWEETED_BY_USER_SCREEN_NAME = "retweeted_by_user_screen_name";

        public static final String[] COLUMNS = new String[] {ACCOUNT_ID, TWEET_ID, USER_ID, USER_NAME, USER_SCREEN_NAME, USER_AVATAR_URL,
                CREATED, CONTENT, SOURCE,
                IN_REPLY_TO_STATUS_ID, IN_REPLY_TO_USER_ID, IN_REPLY_TO_USER_NAME, IN_REPLY_TO_USER_SCREEN_NAME,
                RETWEET_COUNT, FAVORITE_COUNT, FAVORITED,
                RETWEET_ID, RETWEETED_BY_USER_ID, RETWEETED_BY_USER_NAME, RETWEETED_BY_USER_SCREEN_NAME, RETWEET_TIME};

        public static final DataType[] TYPES = new DataType[] {DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.INTEGER, DataType.INTEGER_1,
                DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT, DataType.INTEGER};

        public static final Constraint[] CONSTRAINTS = new Constraint[] {Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING};

    }

}
