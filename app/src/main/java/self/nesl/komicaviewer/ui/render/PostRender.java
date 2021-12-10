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
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.viewholder.LinkPreview;

public class PostRender {
    private LinearLayout root;
    private OnPostClickListener onPostClickListener;

    public PostRender(Context context){
        this.root =new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
    }

    public View render(Post post, List<Post> list){
        if(post.getQuote() != null) addQuote(post.getQuote());
        if(post.getReplyTo() != null) addReplyFor(post.getReplyTo(), list);
        if(post.getText() != null) addLinkedArticle(post.getText());
        return root;
    }

    /**
     * 結果：<span color="gray"> >> 但是我覺得...</span>
     * @param quote 引言。例如：但是我覺得...
     */
    private void addQuote(String quote){
        String show = ">> "+quote;
        SpanBuilder builder=  SpanBuilder.create(show, ()->{
            Toast.makeText(root.getContext(), quote, Toast.LENGTH_SHORT).show();
        });
        root.addView(new RenderTool(root.getContext()).renderSpan(builder));
    }

    /**
     * 結果：<a href="http://doSomething"> > No.12345678 </a>
     * @param replyTo parent
     * @param posts 包含 [replyFor] 的 list
     */
    private void addReplyFor(String replyTo, List<Post> posts){
        Optional<Post> result = posts.stream().filter(p->p.getId().equals(replyTo)).findFirst();
        if(result.isPresent()){
            Post replyFor = result.get();
            String preview = MessageFormat.format("{0} ({1})", replyFor.getId(), replyFor.getDescription(10));
            SpanBuilder builder= SpanBuilder.create(preview, ()-> onPostClickListener.onReplyToClick(replyFor, posts));
            root.addView(new RenderTool(root.getContext()).renderSpan(builder));
        }
    }

    /**
     * 將文章中的連結在原處加上預覽圖
     * @param text 包含連結的文章
     */
    private void addLinkedArticle(String text){
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

    class RenderTool {
        private Context context;
        RenderTool(Context context){
            this.context=context;
        }

        private View renderPreview(ViewGroup view, String link){
            View preview = LayoutInflater.from(context)
                    .inflate(R.layout.link_preview, view, false);
            new LinkPreview(preview).bind(link);
            return preview;
        }

        private View renderLink(String link){
            SpanBuilder builder = SpanBuilder.create(link, ()->{
                onPostClickListener.onLinkClick(link);
                Toast.makeText(context, link, Toast.LENGTH_SHORT).show();
            });
            return renderSpan(builder);
        }

        private View renderText(String text){
            TextView textView = new TextView(context);
            textView.setText(text);
            return textView;
        }

        private View renderSpan(SpanBuilder text){
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            return textView;
        }
    }

    public void setOnReplyToClickListener(OnPostClickListener onPostClickListener){
        this.onPostClickListener = onPostClickListener;
    }

    public interface OnPostClickListener {
        void onReplyToClick(Post replyTo, List<Post> list);
        void onLinkClick(String link);
    }
}

