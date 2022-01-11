package self.nesl.komicaviewer.ui.viewholder;

import android.app.Activity;
import android.view.View;

import java.util.Collections;

import self.nesl.komicaviewer.ui.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.thread.ReplyListAdapter;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class HeadPostViewHolder extends ViewHolderBinder {
    private Activity activity;

    public HeadPostViewHolder(View v, Activity activity) {
        super(v);
        this.activity=activity;
    }

    public void bind(Layout layout){
        Post post = (Post) layout;
        PostRender render = new PostRender(itemView.getContext(), post);
        render.setOnLinkClickListener(ReplyListAdapter.onLinkClickListener(activity));
        render.setOnImageClickListener(ReplyListAdapter.onImageClickListener(itemView.getContext(), Collections.emptyList()));
        PostViewBinder binder = new PostViewBinder(itemView, post, render);
        binder.bind();
    }
}