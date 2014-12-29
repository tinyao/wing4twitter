/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package im.zico.wingtwitter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import im.zico.wingtwitter.ui.ProfileActivity;

import static im.zico.wingtwitter.BuildConfig.DEBUG;

public class WeiboSpan extends ClickableSpan {
    private static String TAG = WeiboSpan.class.getSimpleName();

    private String mUrl;
    private Uri mUri;

    public WeiboSpan(String url) {
        mUrl = url;
        mUri = Uri.parse(mUrl);
    }

    public String getURL() {
        return mUrl;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        if (mUri.getScheme().startsWith("http")) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(mUri);
            context.startActivity(i);
        } else {
            if (mUri.getScheme().startsWith("im.zico.wingtwitter.user")) {
                String name = mUrl.substring(mUrl.lastIndexOf("@") + 1, mUrl.length());
                if (DEBUG) {
                    Log.d(TAG, "Mention user link detected: " + name);
                }
                Toast.makeText(context, "user: " + name, Toast.LENGTH_SHORT).show();
                ProfileActivity.startActivity((Activity) context, name);
            } else if (mUri.getScheme().startsWith("im.zico.wingtwitter.topic")) {
                String name = mUrl.substring(mUrl.indexOf("#") + 1);
                Toast.makeText(context, "topic: " + name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

}
