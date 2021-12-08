package self.nesl.komicaviewer.ui.board;

import android.content.Context;
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
import self.nesl.komicaviewer.ui.viewholder.PostViewHolder;
import self.nesl.komicaviewer.ui.viewholder.ThreadViewHolder;

public class ThreadsAdapter extends SampleAdapter<Post, ThreadViewHolder> {
    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, final int i) {
        super.onBindViewHolder(holder, i);
        final Post post = list.get(i);
        Context context = holder.img.getContext();
        setDetail(holder, post);

        holder.txtPostInd.setText(post.getDescription(23));

        String url = post.getPictureUrl();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.bg_background)
                    .centerCrop()
                    .error(R.drawable.ic_error_404)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img);
        } else {
            Glide.with(context).clear(holder.img);
       }
    }

    private void setDetail(PostViewHolder holder, Post data) {
        holder.txtPostId.setText("No." + data.getId());
        holder.txtReplyCount.setText("回應:" + data.getReplyCount());
        holder.txtPoster.setText(data.getPoster());
        holder.txtTime.setText(data.getTimeStr());
    }

}
