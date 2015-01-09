package im.zico.wingtwitter.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.ui.activity.PhotoViewActivity;
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

    public static void insertPhoto(final Context context, LinearLayout gallery,
                     final String[] photo_urls, final int pos) {
        boolean isMulti = photo_urls.length > 1;
        boolean isFirst = (pos == 0);

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
        imageView.setTag(photo_urls[pos]);
        imageView.setTransitionName(photo_urls[pos]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(context, PhotoViewActivity.class);
                photoIntent.putExtra(WingStore.TweetColumns.MEDIAS, photo_urls);
//                DisplayMetrics metrics = new DisplayMetrics();
//                imageView.getDisplay().getMetrics(metrics);
                int[] location = new int[2];
                imageView.getLocationOnScreen(location);
                photoIntent.putExtra("pivotX", location[0]);
                photoIntent.putExtra("pivotY", location[1]);
                photoIntent.putExtra("position", pos);
                context.startActivity(photoIntent);
                ((Activity)context).overridePendingTransition(R.anim.photo_view_enter, 0);
            }
        });

        Picasso.with(context).load(photo_urls[pos])
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
