package im.zico.wingtwitter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 * Created by tinyao on 11/30/14.
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
//
//    public static String formatStatusText(final Status status) {
//        if (status == null) return null;
//        final String text = status.getText();
//        if (text == null) return null;
//        final HtmlBuilder builder = new HtmlBuilder(text, false, true, true);
//        parseEntities(builder, status);
//        return builder.build().replace("\n", "<br/>");
//    }

    public static String getInReplyToName(final Status status) {
        if (status == null) return null;
        final Status orig = status.isRetweet() ? status.getRetweetedStatus() : status;
        final long in_reply_to_user_id = status.getInReplyToUserId();
        final UserMentionEntity[] entities = status.getUserMentionEntities();
        if (entities == null) return orig.getInReplyToScreenName();
        for (final UserMentionEntity entity : entities) {
            if (in_reply_to_user_id == entity.getId()) return entity.getName();
        }
        return orig.getInReplyToScreenName();
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = Calendar.getInstance().getTimeInMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1m";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1h";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + "h";
        } else {
            String format = "MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            return sdf.format(new Date(time));
        }
    }


}
