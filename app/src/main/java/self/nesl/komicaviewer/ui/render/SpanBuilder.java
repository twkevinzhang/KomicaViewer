package self.nesl.komicaviewer.ui.render;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Element;

public class SpanBuilder extends SpannableStringBuilder{
    private static final String LINK = "http://doSomething";

    public static SpanBuilder create(String text, OnClickListener onClickListener){
        Element linkable= new Element("a").attr("href", LINK).appendText(text);
        CharSequence sequence = Html.fromHtml(linkable.toString(), Html.FROM_HTML_MODE_COMPACT);
        SpanBuilder strBuilder = new SpanBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(strBuilder.length(), sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            strBuilder.makeLinkClickable(onClickListener, span);
        }
        return strBuilder;
    }

    private SpanBuilder(CharSequence text){
        super(text);
    }

    private void makeLinkClickable(OnClickListener onClickListener, final URLSpan span){
        int start = getSpanStart(span);
        int end = getSpanEnd(span);
        int flags = getSpanFlags(span);
        setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if(onClickListener != null)
                    onClickListener.onClick();
            }
        }, start, end, flags);
        removeSpan(span);
    }

    public interface OnClickListener {
        void onClick();
    }
}