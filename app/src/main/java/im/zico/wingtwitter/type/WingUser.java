package im.zico.wingtwitter.type;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import im.zico.wingtwitter.WingApp;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.utils.TweetUtils;
import twitter4j.Relationship;
import twitter4j.URLEntity;
import twitter4j.User;

/**
 * Created by tinyao on 12/1/14.
 */
public class WingUser {

    public long user_id;

    public String name;
    public String screenName;
    public String desc;
    public String location;
    public String website;

    public String avatar;
    public String banner;
    public String bannerColor;

    public int tweetCount;
    public int favCount;
    public int followerCount;
    public int followingCount;

    public boolean isFollowing;
    public boolean isFollowMe;

    public WingUser(){}

    public WingUser(User user) {
        user_id = user.getId();
        name = user.getName();
        screenName = user.getScreenName();

        desc = formatDescription(user);

        location = user.getLocation();

        website = user.getURLEntity().getExpandedURL();

        avatar = TweetUtils.getLargeAvatarUrl(user.getProfileImageURL());
        banner = user.getProfileBannerMobileRetinaURL();
        bannerColor = user.getProfileLinkColor();
        tweetCount = user.getStatusesCount();
        favCount = user.getFavouritesCount();
        followerCount = user.getFollowersCount();
        followingCount = user.getFriendsCount();
        isFollowing = false;
        isFollowMe = false;
    }

    private String formatDescription(User user) {
        String originDesc = user.getDescription();
        URLEntity[] entities = user.getDescriptionURLEntities();
        if(entities != null) {
            for (URLEntity url : entities) {
                originDesc = originDesc.replace(url.getText(),
                        "<a href='" + url.getExpandedURL() + "'>" + url.getDisplayURL() + "</a>" );

//                        "<a href='" + url.getExpandedURL() + "'>" + url.getDisplayURL() + "</a>" );
            }
        }
        Log.d("DEBUG", "out: " + originDesc);
        return originDesc;
    }

    public WingUser(Cursor cursor) {
        user_id = cursor.getLong(cursor.getColumnIndex(WingStore.UserColumns.USER_ID));
        name = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.NAME));
        screenName = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.SCREEN_NAME));
        desc = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.DESCRIPTION));
        location = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.LOCATION));
        website = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.WEBSITE));
        avatar = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.AVATAR));
        banner = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.BANNER));
        bannerColor = cursor.getString(cursor.getColumnIndex(WingStore.UserColumns.BANNER_COLOR));

        tweetCount = cursor.getInt(cursor.getColumnIndex(WingStore.UserColumns.TWEET_COUNT));
        followingCount = cursor.getInt(cursor.getColumnIndex(WingStore.UserColumns.FOLLOWING_COUNT));
        followerCount = cursor.getInt(cursor.getColumnIndex(WingStore.UserColumns.FOLLOWER_COUNT));
        favCount = cursor.getInt(cursor.getColumnIndex(WingStore.UserColumns.FAV_COUNT));
        isFollowing = cursor.getInt(cursor.getColumnIndex(WingStore.UserColumns.IS_FOLLOWING)) == 1;
        isFollowMe = cursor.getInt(cursor.getColumnIndex(WingStore.UserColumns.IS_FOLLOWING_ME)) == 1;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(WingStore.UserColumns.USER_ID, user_id);
        values.put(WingStore.UserColumns.NAME, name);
        values.put(WingStore.UserColumns.SCREEN_NAME, screenName);
        values.put(WingStore.UserColumns.DESCRIPTION, desc);
        values.put(WingStore.UserColumns.LOCATION, location);
        values.put(WingStore.UserColumns.WEBSITE, website);
        values.put(WingStore.UserColumns.AVATAR, avatar);
        values.put(WingStore.UserColumns.BANNER, banner);
        values.put(WingStore.UserColumns.BANNER_COLOR, bannerColor);
        values.put(WingStore.UserColumns.TWEET_COUNT, tweetCount);
        values.put(WingStore.UserColumns.FAV_COUNT, favCount);
        values.put(WingStore.UserColumns.FOLLOWER_COUNT, followerCount);
        values.put(WingStore.UserColumns.FOLLOWING_COUNT, followingCount);
        values.put(WingStore.UserColumns.IS_FOLLOWING, isFollowing);
        values.put(WingStore.UserColumns.IS_FOLLOWING_ME, isFollowMe);

        return values;
    }
}
