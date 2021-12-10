package self.nesl.komicaviewer.ui.viewholder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.thread.ReplyDialog;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView txtPostId;
    public TextView txtPostInd;
    public TextView txtReplyCount;
    public TextView txtPoster;
    public TextView txtTime;
    public TextView txtPost;
    public ImageView img;
    private PostRender.OnReplyToClickListener onReplyToClickListener;

    public PostViewHolder(View v, PostRender.OnReplyToClickListener onReplyToClickListener) {
        super(v);
        txtTime = v.findViewById(R.id.txtTime);
        txtReplyCount = v.findViewById(R.id.txtReplyCount);
        txtPostId = v.findViewById(R.id.txtId);
        txtPoster = v.findViewById(R.id.txtPoster);
        txtPostInd = v.findViewById(R.id.txtPostInd);
        txtPost = v.findViewById(R.id.txtPost);
        img = v.findViewById(R.id.img);
        this.onReplyToClickListener = onReplyToClickListener;
    }

    public void bind(Post post, List<Post> posts){
        setDetail(post);

        PostRender render = new PostRender(post, posts, txtPost);
        render.setReplyToOnClickListener(onReplyToClickListener);
        txtPost.setText(render.render());

        if (post.getPictureUrl() != null){
            img.setVisibility(View.VISIBLE);
            Glide.with(img.getContext())
                    .load(post.getPictureUrl())
                    .placeholder(R.drawable.bg_background)
                    .error(R.drawable.ic_error_404)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img);
        }else{
            img.setVisibility(View.GONE);
        }
    }

    private void setDetail(Post data) {
        txtPostId.setText("No." + data.getId());
        txtReplyCount.setText("回應:" + data.getReplyCount());
        txtPoster.setText(data.getPoster());
        txtTime.setText(data.getTimeStr());
    }
}