package self.nesl.komicaviewer.ui.viewbinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Comment;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.CommentContentRender;
import self.nesl.komicaviewer.ui.render.Render;

public class PostViewBinder {
    public TextView txtPostId;
    public TextView txtPostInd;
    public TextView txtPoster;
    public TextView txtTime;
    public FrameLayout container;
    public LinearLayout comments;
    Post post;
    Context context;
    Render render;

    public PostViewBinder(View v, Post post, Render render) {
        context= v.getContext();
        this.post=post;
        this.render=render;
        txtTime = v.findViewById(R.id.txtTime);
        txtPostId = v.findViewById(R.id.txtId);
        txtPoster = v.findViewById(R.id.txtPoster);
        txtPostInd = v.findViewById(R.id.txtPostInd);
        container = v.findViewById(R.id.container);
        comments = v.findViewById(R.id.comments);

    }

    public void bind(){
        setDetail();
        container.removeAllViews();
        container.addView(render.render());
    }

    private void setDetail() {
        txtPostId.setText("No." + post.getId());
        txtPoster.setText(post.getPoster());
        txtTime.setText(post.getTimeStr());
        for (Comment comment : post.getComments()) {
            View commentView =  LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            FrameLayout container = commentView.findViewById(R.id.container);
            Render renderer= new CommentContentRender(context, comment);
            container.addView(renderer.render());
            comments.addView(commentView);
        }
    }
}