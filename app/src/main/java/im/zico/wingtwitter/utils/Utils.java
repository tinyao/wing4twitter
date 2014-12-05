package im.zico.wingtwitter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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


}
