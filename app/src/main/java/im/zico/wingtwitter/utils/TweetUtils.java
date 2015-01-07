package im.zico.wingtwitter.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import im.zico.wingtwitter.R;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

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

    public static void insertPhoto(Context context, LinearLayout gallery,
                     String photo_url, boolean isMulti, boolean isFirst) {
        final ImageView imageView = new ImageView(context);
        int width = context.getResources().getDisplayMetrics().widthPixels
                - context.getResources().getDimensionPixelSize(R.dimen.spacing_keyline_1)
                - context.getResources().getDimensionPixelSize(R.dimen.spacing_keyline_2);
        LinearLayout.LayoutParams params;
        if (isMulti) {
            width -= context.getResources().getDimensionPixelSize(R.dimen.tweet_gallery_multi_cut);
            params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
            if (!isFirst) {
                params.setMargins(context.getResources().getDimensionPixelSize(R.dimen.tweet_gallery_gap), 0, 0, 0);
            }
        } else {
            params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
        imageView.setTag(photo_url);

        Picasso.with(context).load(photo_url)
                .fit()
                .centerCrop()
                .into(imageView);

        gallery.addView(imageView);
    }

    public static String parseException(TwitterException te, TwitterMethod method) {
        if (te.getStatusCode() == -1) {

        }
        switch (te.getStatusCode()) {
            case -1:
                return "Oops, perhaps something wrong with your network.";
            case 429:
                return "Oops, request too frequently.";
        }
        return te.getLocalizedMessage();
    }
}
