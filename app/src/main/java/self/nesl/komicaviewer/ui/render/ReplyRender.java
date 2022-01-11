package self.nesl.komicaviewer.ui.render;

import static self.nesl.komicaviewer.util.ProjectUtils.find;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.paragraph.Paragraph;

public class ReplyRender extends PostRender{
    private OnReplyToClickListener onReplyToClickListener;
    private OnAllReplyClickListener OnAllReplyClickListener;
    private List<Post> list;

    public ReplyRender(Context context, Post post, List<Post> list){
        super(context, post);
        this.list=list;
    }

    public View render(){
        if(!post.getContent().isEmpty()) {
            for (Paragraph paragraph :post.getContent()) {
                switch (paragraph.getType()){
                    case IMAGE:
                        super.addImage(paragraph.getContent());
                        break;
                    case String:
                        super.addText(paragraph.getContent());
                        break;
                    case LINK:
                        super.addLink(paragraph.getContent());
                        break;
                    case ReplyTo:
                        addReplyFor(paragraph.getContent());
                        break;
                    case Quote:
                        addQuote(paragraph.getContent());
                        break;
                }
            }
        }
        addReplyTreeLink();
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
        Post replyFor = find(replyTo, list);
        if(replyFor != null){
            String preview = MessageFormat.format("{0} ({1})", replyFor.getId(), replyFor.getDesc(10));
            SpanBuilder builder= SpanBuilder.create(preview, ()-> onReplyToClickListener.onReplyToClick(replyFor, list));
            root.addView(new RenderTool(root.getContext()).renderSpan(builder));
        }
    }

    private void addReplyTreeLink(){
        if(post.getReplies() != 0){
            String preview = MessageFormat.format("查看全部回應 ({0})", post.getReplies());
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

