package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;

import java.util.List;

import self.nesl.komicaviewer.ui.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.ReplyRender;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class ReplyViewHolder extends ViewHolderBinder {
    private ReplyRender.OnReplyToClickListener OnReplyToClickListener;
    private PostRender.OnLinkClickListener OnLinkClickListener;
    private ReplyRender.OnAllReplyClickListener OnAllReplyClickListener;
    private PostRender.OnImageClickListener OnImageClickListener;
    private List<Post> list;
    
    public ReplyViewHolder(
            View v,
            ReplyRender.OnReplyToClickListener OnReplyToClickListener,
            PostRender.OnLinkClickListener OnLinkClickListener,
            ReplyRender.OnAllReplyClickListener OnAllReplyClickListener,
            PostRender.OnImageClickListener OnImageClickListener,
            List<Post> list
    ) {
        super(v);
        this.OnReplyToClickListener=OnReplyToClickListener;
        this.OnLinkClickListener=OnLinkClickListener;
        this.OnAllReplyClickListener=OnAllReplyClickListener;
        this.OnImageClickListener=OnImageClickListener;
        this.list=list;
    }

    public void bind(Layout layout){
        Post reply = (Post) layout;

        ReplyRender render = new ReplyRender(itemView.getContext(), reply, list);
        render.setOnReplyToClickListener(OnReplyToClickListener);
        render.setOnLinkClickListener(OnLinkClickListener);
        render.setOnAllReplyClickListener(OnAllReplyClickListener);
        render.setOnImageClickListener(OnImageClickListener);

        PostViewBinder binder = new PostViewBinder(itemView, reply, render);
        binder.bind();
    }
}