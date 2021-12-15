package self.nesl.komicaviewer.ui.viewholder;

import android.app.Activity;
import android.view.View;

import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.thread.CommentListAdapter;
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
        render.setOnLinkClickListener(CommentListAdapter.onLinkClickListener(activity));
        PostViewBinder binder = new PostViewBinder(itemView, post, render);
        binder.bind();
    }
}