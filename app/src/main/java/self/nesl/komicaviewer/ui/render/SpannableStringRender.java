package self.nesl.komicaviewer.ui.render;

import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.models.Post;

abstract class SpannableStringRender {
    SpannableStringRender(TextView textView){
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    abstract SpannableStringBuilder render();
}
