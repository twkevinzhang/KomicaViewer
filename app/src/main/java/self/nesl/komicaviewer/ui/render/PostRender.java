package self.nesl.komicaviewer.ui.render;

import static self.nesl.komicaviewer.util.Utils.print;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.viewbinder.LinkPreviewBinder;

public class PostRender {
    Post post;
    LinearLayout root;
    OnLinkClickListener onLinkClickListener;

    public PostRender(Context context, Post post){
        this.root =new LinearLayout(context);
        this.post=post;
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
    }

    public View render(){
        if(post.getText() != null) addLinkedArticle(post.getText());
        if(post.getUrl() != null) addFrom(post.getUrl());
        return root;
    }

    /**
     * 將文章中的連結在原處加上預覽圖
     * @param text 包含連結的文章
     */
    void addLinkedArticle(String text){
        RenderTool tool= new RenderTool(root.getContext());
        Matcher m = Patterns.WEB_URL.matcher(text);

        int index=0;
        while (m.find()){
            String url = m.group();
            String preParagraph = text.substring(index, m.start());
            root.addView(tool.renderText(preParagraph));
            root.addView(tool.renderLink(url));
            root.addView(tool.renderPreview(root, url));

            index = m.end();
        }
        String lastParagraph = text.substring(index);
        root.addView(tool.renderText(lastParagraph));
    }

    private void addFrom(String url){
        RenderTool tool = new RenderTool(root.getContext());
        root.addView(tool.renderText("原文連結："));
        root.addView(tool.renderLink(url));
    }

    class RenderTool {
        private Context context;
        RenderTool(Context context){
            this.context=context;
        }

        private View renderPreview(ViewGroup view, String link){
            View preview = LayoutInflater.from(context)
                    .inflate(R.layout.link_preview, view, false);
            new LinkPreviewBinder(preview, link).render();
            preview.setOnClickListener(v -> {
                onLinkClickListener.onLinkClick(link);
            });
            return preview;
        }

        private View renderLink(String link){
            SpanBuilder builder = SpanBuilder.create(link, ()->{
                onLinkClickListener.onLinkClick(link);
            });
            return renderSpan(builder);
        }

        private View renderText(String text){
            TextView textView = new TextView(context);
            textView.setText(text);
            return textView;
        }

        View renderSpan(SpanBuilder text){
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            return textView;
        }
    }

    public void setOnLinkClickListener(OnLinkClickListener onLinkClickListener){
        this.onLinkClickListener = onLinkClickListener;
    }

    public interface OnLinkClickListener {
        void onLinkClick(String link);
    }
}

