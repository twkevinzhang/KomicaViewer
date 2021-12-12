package self.nesl.komicaviewer.ui.viewholder;

import static self.nesl.komicaviewer.ui.thread.QuoteDialog.COLUMN_POST;
import static self.nesl.komicaviewer.ui.thread.QuoteDialog.COLUMN_POST_LIST;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.CommentRender;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.thread.QuoteDialog;
import self.nesl.komicaviewer.ui.thread.RepliesDialog;
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
        PostViewBinder binder = new PostViewBinder(itemView, comment);
        binder.setDetail();

        binder.contener.removeAllViews();
        binder.contener.addView(contentView(comment));
        binder.setImage();
    }

    private View contentView(Post comment) {
        CommentRender render = new CommentRender(itemView.getContext(), comment, list);
        render.setOnReplyToClickListener(OnReplyToClickListener);
        render.setOnLinkClickListener(OnLinkClickListener);
        render.setOnAllReplyClickListener(OnAllReplyClickListener);
        return render.render();
    }
}