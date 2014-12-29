package im.zico.wingtwitter.ui.view;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import im.zico.wingtwitter.utils.HackyMovementMethod;
import im.zico.wingtwitter.utils.SpannableStringUtils;

/**
 * Created by tinyao on 12/28/14.
 */
public class HtmlTextView extends TextView{

    public HtmlTextView(Context context) {
        super(context);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);

        MovementMethod method = getMovementMethod();
        if (method instanceof HackyMovementMethod) {
            return ((HackyMovementMethod) method).isLinkHit();
        }

        return ret;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setHtmlText(String text){
        this.setText(Html.fromHtml(text));
        this.setMovementMethod(HackyMovementMethod.getInstance());
        removeUnderlines((Spannable) this.getText());
//        setText(SpannableStringUtils.span(getText().toString()));
    }

    public static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for(URLSpan span:spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }

    static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String p_Url) {
            super(p_Url);
        }

        public void updateDrawState(TextPaint p_DrawState) {
            super.updateDrawState(p_DrawState);
            p_DrawState.setUnderlineText(false);
        }
    }
}
