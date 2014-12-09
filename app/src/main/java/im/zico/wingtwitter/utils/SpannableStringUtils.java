package im.zico.wingtwitter.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Pattern;

public class SpannableStringUtils {

    private static final Pattern PATTERN_WEB = Patterns.WEB_URL;

//	private static final Pattern PATTERN_TOPIC = Pattern.compile(Regex.HASHTAG_PATTERN);
//	private static final Pattern PATTERN_MENTION = Pattern.compile("@([A-Za-z0-9_-]+)");

    private static final String HTTP_SCHEME = "http://";
    private static final String TOPIC_SCHEME = "im.zico.wingtwittert.topic://";
    private static final String MENTION_SCHEME = "im.zico.wingtwitter.user://";

    public static SpannableString span(String text) {

        SpannableString ss = SpannableString.valueOf(text);
        Linkify.addLinks(ss, PATTERN_WEB, HTTP_SCHEME);
        Linkify.addLinks(ss, Regex.HASHTAG_PATTERN, TOPIC_SCHEME);
        Linkify.addLinks(ss, Regex.MENTION_PATTERN, MENTION_SCHEME);

        // Convert to our own span
        URLSpan[] spans = ss.getSpans(0, ss.length(), URLSpan.class);
        for (URLSpan span : spans) {
            WeiboSpan s = new WeiboSpan(span.getURL());
            int start = ss.getSpanStart(span);
            int end = ss.getSpanEnd(span);
            ss.removeSpan(span);
            if(ss.charAt(end-1) == ')') {
                // Fix when url ended by ')'
                Log.d("DEBUG", ss + " --- span end --- " + ss.charAt(end-1));
                s = new WeiboSpan(span.getURL().substring(0, span.getURL().length()-1));
                end--;
            }
            ss.setSpan(s, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return ss;
    }

}
