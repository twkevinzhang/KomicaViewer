package self.nesl.komicaviewer.ui.render;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.models.Post;

public class CommentRender extends PostRender{
    private OnReplyToClickListener onReplyToClickListener;
    private OnAllReplyClickListener OnAllReplyClickListener;
    private List<Post> list;

    public CommentRender(Context context, Post post, List<Post> list){
        super(context, post);
        this.list=list;
    }

    public View render(){
        if(post.getReplyTo() != null) addReplyFor(post.getReplyTo());
        if(post.getQuote() != null) addQuote(post.getQuote());
        if(post.getText() != null) super.addLinkedArticle(post.getText());
        addTree();
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
     */
    private void addReplyFor(String replyTo){
        Optional<Post> result = list.stream().filter(p->p.getId().equals(replyTo)).findFirst();
        if(result.isPresent()){
            Post replyFor = result.get();
            String preview = MessageFormat.format("{0} ({1})", replyFor.getId(), replyFor.getDescription(10));
            SpanBuilder builder= SpanBuilder.create(preview, ()-> onReplyToClickListener.onReplyToClick(replyFor, list));
            root.addView(new RenderTool(root.getContext()).renderSpan(builder));
        }
    }

    private void addTree(){
        if(post.getReplyCount() != 0){
            String preview = MessageFormat.format("查看全部回應 ({0})", post.getReplyCount());
            SpanBuilder builder= SpanBuilder.create(preview, ()-> OnAllReplyClickListener.onAllReplyClick(post, list));
            root.addView(new RenderTool(root.getContext()).renderSpan(builder));
        }
    }

    public void setOnReplyToClickListener(OnReplyToClickListener onReplyToClickListener){
        this.onReplyToClickListener = onReplyToClickListener;
    }

    public void setOnAllReplyClickListener(OnAllReplyClickListener OnAllReplyClickListener){
        this.OnAllReplyClickListener = OnAllReplyClickListener;
    }

    public interface OnReplyToClickListener {
        void onReplyToClick(Post replyTo, List<Post> all);
    }

    public interface OnAllReplyClickListener {
        void onAllReplyClick(Post thread, List<Post> all);
    }

}

