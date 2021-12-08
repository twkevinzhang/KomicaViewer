package self.nesl.komicaviewer.ui.post;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewholder.PostViewHolder;

public class PostListAdapter extends SampleAdapter<Post, PostViewHolder> {
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int i) {
        super.onBindViewHolder(holder, i);
        final Post post = list.get(i);

        setDetail(holder, post);

        PostRender render = new PostRender(post, list, holder.txtPost);;
        holder.txtPost.setText(render.render());

        if (post.getPictureUrl() != null){
            holder.img.setVisibility(View.VISIBLE);
            Glide.with(holder.img.getContext())
                    .load(post.getPictureUrl())
                    .placeholder(R.drawable.bg_background)
                    .error(R.drawable.ic_error_404)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);
        }else{
            holder.img.setVisibility(View.GONE);
        }
    }

    private void setDetail(PostViewHolder holder, Post data) {
        holder.txtPostId.setText("No." + data.getId());
        holder.txtReplyCount.setText("回應:" + data.getReplyCount());
        holder.txtPoster.setText(data.getPoster());
        holder.txtTime.setText(data.getTimeStr());
    }
}