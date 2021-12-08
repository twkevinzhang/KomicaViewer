package self.nesl.komicaviewer.ui.render;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Element;

public class LinkBuilder {
    private static final String LINK = "http://doSomething";
    private SpannableStringBuilder strBuilder;

    public LinkBuilder(SpannableStringBuilder strBuilder){
        this.strBuilder =strBuilder;
    }

    public void addLink(String text, OnClickListener onClickListener){
        Element linkable= new Element("a").attr("href", LINK).appendText(text);
        CharSequence sequence = Html.fromHtml(linkable.toString(), Html.FROM_HTML_MODE_COMPACT);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(strBuilder.length(), sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, onClickListener, span);
        }
        strBuilder.append("\n");
        this.strBuilder.append(strBuilder);
    }

    private void makeLinkClickable(SpannableStringBuilder strBuilder, OnClickListener onClickListener, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        strBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                onClickListener.onClick();
            }
        }, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public interface OnClickListener {
        void onClick();
    }
}