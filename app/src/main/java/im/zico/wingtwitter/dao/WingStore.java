package im.zico.wingtwitter.dao;

import android.provider.BaseColumns;

import im.zico.wingtwitter.utils.database.Column.*;
import im.zico.wingtwitter.utils.database.SQLiteTable;

/**
 * Created by tinyao on 12/5/14.
 */
public class WingStore {

    public static final int TYPE_TWEET = 0;
    public static final int TYPE_MENTION = 1;
    public static final int TYPE_DM = 2;
    public static final int TYPE_USER = 5;

    public static final class TweetColumns implements BaseColumns {

        private TweetColumns() {
        }

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

        public static final String[] COLUMNS = new String[]{ACCOUNT_ID, TWEET_ID, USER_ID, USER_NAME, USER_SCREEN_NAME, USER_AVATAR_URL,
                CREATED, CONTENT, SOURCE,
                IN_REPLY_TO_STATUS_ID, IN_REPLY_TO_USER_ID, IN_REPLY_TO_USER_NAME, IN_REPLY_TO_USER_SCREEN_NAME,
                RETWEET_COUNT, FAVORITE_COUNT, FAVORITED,
                RETWEET_ID, RETWEETED_BY_USER_ID, RETWEETED_BY_USER_NAME, RETWEETED_BY_USER_SCREEN_NAME, RETWEET_TIME};

        public static final DataType[] TYPES = new DataType[]{DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.INTEGER, DataType.INTEGER_1,
                DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT, DataType.INTEGER};

        public static final Constraint[] CONSTRAINTS = new Constraint[]{Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING};

    }

    public static final class UserColumns implements BaseColumns {

        private UserColumns() {
        }

        public static final String TABLE_NAME = "user";
        public static final String ACCOUNT_ID = "account_id";

        public static final String USER_ID = "user_id";
        public static final String NAME = "name";
        public static final String SCREEN_NAME = "screen_name";
        public static final String AVATAR = "avatar";
        public static final String BANNER = "banner";
        public static final String DESCRIPTION = "description";
        public static final String LOCATION = "location";
        public static final String WEBSITE = "website";
        public static final String TWEET_COUNT = "tweet_count";
        public static final String FAV_COUNT = "fav_count";
        public static final String FOLLOWING_COUNT = "following_count";
        public static final String FOLLOWER_COUNT = "follower_count";

        public static final String IS_FOLLOWING = "is_following";
        public static final String IS_FOLLOWING_ME = "is_following_me";

        public static final String[] COLUMNS = new String[]{
                ACCOUNT_ID, USER_ID, NAME, SCREEN_NAME, AVATAR, BANNER,
                DESCRIPTION, LOCATION, WEBSITE,
                TWEET_COUNT, FAV_COUNT, FOLLOWING_COUNT, FOLLOWER_COUNT,
                IS_FOLLOWING, IS_FOLLOWING_ME
        };

        public static final DataType[] TYPES = new DataType[]{
                DataType.INTEGER, DataType.INTEGER, DataType.TEXT, DataType.TEXT, DataType.TEXT, DataType.TEXT,
                DataType.TEXT, DataType.TEXT, DataType.TEXT,
                DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER,
                DataType.INTEGER_1, DataType.INTEGER_1
        };

        public static final Constraint[] CONSTRAINTS = new Constraint[]{
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING, Constraint.NOTHING,
                Constraint.NOTHING, Constraint.NOTHING};

    }

}
