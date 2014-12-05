package im.zico.wingtwitter.utils;

import android.content.ContentValues;
import android.database.Cursor;

import twitter4j.GeoLocation;
import twitter4j.Status;
import im.zico.wingtwitter.dao.WingStore.*;
import twitter4j.User;

/**
 * Created by tinyao on 12/5/14.
 */
public class ContentVaulesUtils {

//    public static ContentValues makeStatusContentValues(final Status orig, final long account_id) {
//        if (orig == null || orig.getId() <= 0) return null;
//        final ContentValues values = new ContentValues();
//
//        values.put(Statuses.ACCOUNT_ID, account_id);
//        values.put(Statuses.STATUS_ID, orig.getId());
//        values.put(Statuses.MY_RETWEET_ID, orig.getCurrentUserRetweetId());
//        final boolean is_retweet = orig.isRetweet();
//        final Status status;
//        final Status retweeted_status = is_retweet ? orig.getRetweetedStatus() : null;
//
//        if (retweeted_status != null) {
//            final User retweet_user = orig.getUser();
//            values.put(Statuses.RETWEET_ID, retweeted_status.getId());
//            values.put(Statuses.RETWEETED_BY_USER_ID, retweet_user.getId());
//            values.put(Statuses.RETWEETED_BY_USER_NAME, retweet_user.getName());
//            values.put(Statuses.RETWEETED_BY_USER_SCREEN_NAME, retweet_user.getScreenName());
//            status = retweeted_status;  // retweet status body
//        } else {
//            status = orig;
//        }
//        final User user = status.getUser();
//        if (user != null) {
//            final long userId = user.getId();
//            final String profileImageUrl = user.getProfileImageURL();
//            final String name = user.getName(), screenName = user.getScreenName();
//            values.put(Statuses.USER_ID, userId);
//            values.put(Statuses.USER_NAME, name);
//            values.put(Statuses.USER_SCREEN_NAME, screenName);
//            values.put(Statuses.IS_PROTECTED, user.isProtected());
//            values.put(Statuses.IS_VERIFIED, user.isVerified());
//            values.put(Statuses.USER_PROFILE_AVATAR_URL, profileImageUrl);
////            values.put(CachedUsers.IS_FOLLOWING, user.isFollowing());
//        }
//        if (status.getCreatedAt() != null) {
//            values.put(Statuses.STATUS_TIME, status.getCreatedAt().getTime());
//        }
////        final String text_html = Utils.formatStatusText(status);
////        values.put(Statuses.BODY_HTML, text_html);
//        values.put(Statuses.BODY_PLAIN, status.getText());
////        values.put(Statuses.BODY_UNESCAPED, toPlainText(text_html));
//        values.put(Statuses.RETWEET_COUNT, status.getRetweetCount());
//        values.put(Statuses.IN_REPLY_TO_STATUS_ID, status.getInReplyToStatusId());
//        values.put(Statuses.IN_REPLY_TO_USER_ID, status.getInReplyToUserId());
//        values.put(Statuses.IN_REPLY_TO_USER_NAME, Utils.getInReplyToName(status));
//        values.put(Statuses.IN_REPLY_TO_USER_SCREEN_NAME, status.getInReplyToScreenName());
//        values.put(Statuses.SOURCE, status.getSource());
//        values.put(Statuses.IS_POSSIBLY_SENSITIVE, status.isPossiblySensitive());
//        final GeoLocation location = status.getGeoLocation();
//        if (location != null) {
//            values.put(Statuses.LOCATION, location.getLatitude() + "," + location.getLongitude());
//        }
//        values.put(Statuses.IS_RETWEET, is_retweet);
//        values.put(Statuses.IS_FAVORITE, status.isFavorited());
////        final ParcelableMedia[] medias = ParcelableMedia.fromEntities(status);
////        if (medias != null) {
////            values.put(Statuses.MEDIAS, JSONSerializer.toJSONArrayString(medias));
////            values.put(Statuses.FIRST_MEDIA, medias[0].url);
////        }
////        final ParcelableUserMention[] mentions = ParcelableUserMention.fromStatus(status);
////        if (mentions != null) {
////            values.put(Statuses.MENTIONS, JSONSerializer.toJSONArrayString(mentions));
////        }
//        return values;
//    }

    public static boolean getAsBoolean(final ContentValues values, final String key, final boolean def) {
        if (values == null || key == null) return def;
        final Object value = values.get(key);
        if (value == null) return def;
        return Boolean.valueOf(value.toString());
    }

    public static long getAsInteger(final ContentValues values, final String key, final int def) {
        if (values == null || key == null) return def;
        final Object value = values.get(key);
        if (value == null) return def;
        return Integer.valueOf(value.toString());
    }

    public static long getAsLong(final ContentValues values, final String key, final long def) {
        if (values == null || key == null) return def;
        final Object value = values.get(key);
        if (value == null) return def;
        return Long.valueOf(value.toString());
    }

}
