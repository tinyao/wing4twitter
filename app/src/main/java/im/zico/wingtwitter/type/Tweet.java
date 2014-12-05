package im.zico.wingtwitter.type;

import android.content.ContentValues;
import android.database.Cursor;

import im.zico.wingtwitter.dao.WingStore.*;
import twitter4j.Status;

/**
 * Created by tinyao on 12/1/14.
 */
public class Tweet {

    public long account_id;
    public long id;
    public long user_id;
    public long created_at;
    public String user_name;

    public Tweet() {
    }

    public Tweet(Status status) {
        account_id = 111;
        id = status.getId();
        user_id = status.getUser().getId();
        created_at = status.getCreatedAt().getDate();
        user_name = status.getUser().getName();
    }

    public static Tweet fromCursor(final Cursor cursor) {
        Tweet tweet = new Tweet();
        tweet.account_id = cursor.getLong(cursor.getColumnIndex(Statuses.ACCOUNT_ID));
        tweet.id = cursor.getLong(cursor.getColumnIndex(Statuses.STATUS_ID));
        tweet.user_id = cursor.getLong(cursor.getColumnIndex(Statuses.USER_ID));
        tweet.created_at = cursor.getLong(cursor.getColumnIndex(Statuses.CREATED));
        tweet.user_name = cursor.getString(cursor.getColumnIndex(Statuses.USER_NAME));
        return tweet;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Statuses.ACCOUNT_ID, account_id);
        values.put(Statuses.STATUS_ID, id);
        values.put(Statuses.USER_ID, user_id);
        values.put(Statuses.CREATED, created_at);
        values.put(Statuses.USER_NAME, user_name);
        return values;
    }

//    public static final class CursorIndices {
//
//        private final int _id, account_id, status_id, status_timestamp, user_name, user_screen_name, text_html,
//                text_plain, text_unescaped, user_profile_image_url, is_favorite, is_retweet, is_gap, location,
//                is_protected, is_verified, in_reply_to_status_id, in_reply_to_user_id, in_reply_to_user_name,
//                in_reply_to_user_screen_name, my_retweet_id, retweeted_by_user_name, retweeted_by_user_screen_name,
//                retweet_id, retweeted_by_user_id, user_id, source, retweet_count, favorite_count,
//                is_possibly_sensitive, is_following, medias, first_media, mentions;
//
//        public CursorIndices(final Cursor cursor) {
//            _id = cursor.getColumnIndex(Statuses._ID);
//            account_id = cursor.getColumnIndex(Statuses.ACCOUNT_ID);
//            status_id = cursor.getColumnIndex(Statuses.STATUS_ID);
//            status_timestamp = cursor.getColumnIndex(Statuses.STATUS_TIME);
//            user_name = cursor.getColumnIndex(Statuses.USER_NAME);
//            user_screen_name = cursor.getColumnIndex(Statuses.USER_SCREEN_NAME);
//            text_html = cursor.getColumnIndex(Statuses.BODY_HTML);
//            text_plain = cursor.getColumnIndex(Statuses.BODY_PLAIN);
//            text_unescaped = cursor.getColumnIndex(Statuses.BODY_UNESCAPED);
//            user_profile_image_url = cursor.getColumnIndex(Statuses.USER_PROFILE_AVATAR_URL);
//            is_favorite = cursor.getColumnIndex(Statuses.IS_FAVORITE);
//            is_retweet = cursor.getColumnIndex(Statuses.IS_RETWEET);
//            is_gap = cursor.getColumnIndex(Statuses.IS_GAP);
//            location = cursor.getColumnIndex(Statuses.LOCATION);
//            is_protected = cursor.getColumnIndex(Statuses.IS_PROTECTED);
//            is_verified = cursor.getColumnIndex(Statuses.IS_VERIFIED);
//            in_reply_to_status_id = cursor.getColumnIndex(Statuses.IN_REPLY_TO_STATUS_ID);
//            in_reply_to_user_id = cursor.getColumnIndex(Statuses.IN_REPLY_TO_USER_ID);
//            in_reply_to_user_name = cursor.getColumnIndex(Statuses.IN_REPLY_TO_USER_NAME);
//            in_reply_to_user_screen_name = cursor.getColumnIndex(Statuses.IN_REPLY_TO_USER_SCREEN_NAME);
//            my_retweet_id = cursor.getColumnIndex(Statuses.MY_RETWEET_ID);
//            retweet_id = cursor.getColumnIndex(Statuses.RETWEET_ID);
//            retweeted_by_user_id = cursor.getColumnIndex(Statuses.RETWEETED_BY_USER_ID);
//            retweeted_by_user_name = cursor.getColumnIndex(Statuses.RETWEETED_BY_USER_NAME);
//            retweeted_by_user_screen_name = cursor.getColumnIndex(Statuses.RETWEETED_BY_USER_SCREEN_NAME);
//            user_id = cursor.getColumnIndex(Statuses.USER_ID);
//            source = cursor.getColumnIndex(Statuses.SOURCE);
//            retweet_count = cursor.getColumnIndex(Statuses.RETWEET_COUNT);
//            favorite_count = cursor.getColumnIndex(Statuses.FAVORITE_COUNT);
//            is_possibly_sensitive = cursor.getColumnIndex(Statuses.IS_POSSIBLY_SENSITIVE);
//            is_following = cursor.getColumnIndex(Statuses.IS_FOLLOWING);
//            medias = cursor.getColumnIndex(Statuses.MEDIA);
//            first_media = cursor.getColumnIndex(Statuses.FIRST_MEDIA);
//            mentions = cursor.getColumnIndex(Statuses.MENTIONS);
//        }
//
//        @Override
//        public String toString() {
//            return "CursorIndices{_id=" + _id + ", account_id=" + account_id + ", status_id=" + status_id
//                    + ", status_timestamp=" + status_timestamp + ", user_name=" + user_name + ", user_screen_name="
//                    + user_screen_name + ", text_html=" + text_html + ", text_plain=" + text_plain
//                    + ", text_unescaped=" + text_unescaped + ", user_profile_image_url=" + user_profile_image_url
//                    + ", is_favorite=" + is_favorite + ", is_retweet=" + is_retweet + ", is_gap=" + is_gap
//                    + ", location=" + location + ", is_protected=" + is_protected + ", is_verified=" + is_verified
//                    + ", in_reply_to_status_id=" + in_reply_to_status_id + ", in_reply_to_user_id="
//                    + in_reply_to_user_id + ", in_reply_to_user_name=" + in_reply_to_user_name
//                    + ", in_reply_to_user_screen_name=" + in_reply_to_user_screen_name + ", my_retweet_id="
//                    + my_retweet_id + ", retweeted_by_user_name=" + retweeted_by_user_name
//                    + ", retweeted_by_user_screen_name=" + retweeted_by_user_screen_name + ", retweet_id=" + retweet_id
//                    + ", retweeted_by_user_id=" + retweeted_by_user_id + ", user_id=" + user_id + ", source=" + source
//                    + ", retweet_count=" + retweet_count + ", favorite_count=" + favorite_count
//                    + ", is_possibly_sensitive=" + is_possibly_sensitive + ", is_following=" + is_following
//                    + ", medias=" + medias + ", first_media=" + first_media + ", mentions=" + mentions + "}";
//        }
//    }

}
