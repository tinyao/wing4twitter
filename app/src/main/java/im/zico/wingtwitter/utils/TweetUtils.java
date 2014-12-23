package im.zico.wingtwitter.utils;

/**
 * Created by tinyao on 12/18/14.
 */
public class TweetUtils {

    public static String getLargeAvatarUrl(String normalUrl) {
        return normalUrl.replace("_normal.", "_200x200.");
    }

    public static String getSmallAvatarUrl(String normalUrl) {
        return normalUrl.replace("_normal.", "_120x120.");
    }

}
