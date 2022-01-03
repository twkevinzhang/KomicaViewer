package self.nesl.komicaviewer.ui.render;

import static self.nesl.komicaviewer.util.Utils.print;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.paragraph.Paragraph;
import self.nesl.komicaviewer.ui.gallery.Poster;
import self.nesl.komicaviewer.ui.viewbinder.LinkPreviewBinder;

public class PostRender implements Render {
    private static String LINK_REGEX = "(http(s?):/)(/[^/]+)+";
    private static Pattern IMAGE_LINK_PATTERN = Pattern.compile(LINK_REGEX + "\\.(?:jpg|gif|png|webm)");

    Post post;
    LinearLayout root;
    List<String> imageUrls;
    OnLinkClickListener onLinkClickListener;
    OnImageClickListener onImageClickListener;

    public PostRender(Context context, Post post){
        this.root =new LinearLayout(context);
        this.post=post;
        this.imageUrls = new ArrayList<>();
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
    }

    public View render(){
        if(!post.getContent().isEmpty()) {
            for (Paragraph paragraph :post.getContent()) {
                switch (paragraph.getType()){
                    case IMAGE:
                        addImage(paragraph.getContent());
                        break;
                    case String:
                        addText(paragraph.getContent());
                        break;
                    case LINK:
                        addLink(paragraph.getContent());
                        break;
                    default:
                        break;
                }
            }
        }
        if(post.getUrl() != null) addFrom(post.getUrl());
        return root;
    }

    void addLink(String url){
        RenderTool tool= new RenderTool(root.getContext());
        root.addView(tool.renderPreview(root, url));
    }

    void addImage(String url){
        RenderTool tool= new RenderTool(root.getContext());
        imageUrls.add(url);
        root.addView(tool.renderImage(imageUrls.size() -1));
    }

    void addText(String text){
        RenderTool tool= new RenderTool(root.getContext());
        root.addView(tool.renderText(text));
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

        private View renderPreview(ViewGroup parent, String link){
            View preview = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.link_preview, parent, false);
            new LinkPreviewBinder(preview, link).bind();
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

        View renderImage(int index){
            String url = imageUrls.get(index);
            ImageView imageView = new ImageView(context);
            List<Poster> infoList = imageUrls.stream().map(u-> new Poster(u, post)).collect(Collectors.toList());
            imageView.setOnClickListener(v -> onImageClickListener.onImageClick(v, infoList, index));
            new ImageRender(imageView, url).render();
            return imageView;
        }
    }

    public void setOnLinkClickListener(OnLinkClickListener onLinkClickListener){
        this.onLinkClickListener = onLinkClickListener;
    }

    public interface OnLinkClickListener {
        void onLinkClick(String link);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener){
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnImageClickListener {
        void onImageClick(View v, List<Poster> posterList, int startPosition);
    }
}

