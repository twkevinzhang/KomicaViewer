package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;

public class ThreadViewHolder extends PostViewHolder {

    public ThreadViewHolder(View v) {
        super(v, null);
    }

    public void bind(Post data, List<Post> posts){
        txtPostId.setText("No." + data.getId());
        txtReplyCount.setText("回應:" + data.getReplyCount());
        txtPoster.setText(data.getPoster());
        txtTime.setText(data.getTimeStr());
        txtPostInd.setText(data.getDescription(23));

        String url = data.getPictureUrl();
        if (url != null) {
            Glide.with(img.getContext())
                    .load(url)
                    .placeholder(R.drawable.bg_background)
                    .centerCrop()
                    .error(R.drawable.ic_error_404)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img);
        } else {
            Glide.with(img.getContext()).clear(img);
        }
    }
}