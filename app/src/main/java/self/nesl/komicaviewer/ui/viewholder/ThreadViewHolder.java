package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;



import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class ThreadViewHolder extends ViewHolderBinder {
    public ThreadViewHolder(View v) {
        super(v);
    }

    public void bind(Layout layout){
        Post data = (Post) layout;
        PostViewBinder binder= new PostViewBinder(itemView, data);
        binder.txtPostId.setText("No." + data.getId());
        binder.txtPoster.setText(data.getPoster());
        binder.txtTime.setText(data.getTimeStr());
        binder.txtPostInd.setText(data.getDescription(23));

        TextView txtReplyCount = itemView.findViewById(R.id.txtReplyCount);
        txtReplyCount.setText("回應:" + data.getReplyCount());

        String url = data.getPictureUrl();
        if (url != null) {
            Glide.with(binder.img.getContext())
                    .load(url)
                    .placeholder(R.drawable.bg_background)
                    .centerCrop()
                    .error(R.drawable.ic_error_404)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binder.img);
        } else {
            Glide.with(binder.img.getContext()).clear(binder.img);
        }
    }
}