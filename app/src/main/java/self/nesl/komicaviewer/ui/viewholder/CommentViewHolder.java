package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;

import java.util.List;

import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.CommentRender;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class CommentViewHolder extends ViewHolderBinder {
    private CommentRender.OnReplyToClickListener OnReplyToClickListener;
    private CommentRender.OnLinkClickListener OnLinkClickListener;
    private CommentRender.OnAllReplyClickListener OnAllReplyClickListener;
    private List<Post> list;
    
    public CommentViewHolder(
            View v,
            CommentRender.OnReplyToClickListener OnReplyToClickListener,
            PostRender.OnLinkClickListener OnLinkClickListener,
            CommentRender.OnAllReplyClickListener OnAllReplyClickListener,
            List<Post> list
    ) {
        super(v);
        this.OnReplyToClickListener=OnReplyToClickListener;
        this.OnLinkClickListener=OnLinkClickListener;
        this.OnAllReplyClickListener=OnAllReplyClickListener;
        this.list=list;
    }

    public void bind(Layout layout){
        Post comment = (Post) layout;

        CommentRender render = new CommentRender(itemView.getContext(), comment, list);
        render.setOnReplyToClickListener(OnReplyToClickListener);
        render.setOnLinkClickListener(OnLinkClickListener);
        render.setOnAllReplyClickListener(OnAllReplyClickListener);

        PostViewBinder binder = new PostViewBinder(itemView, comment, render);
        binder.bind();
    }
}