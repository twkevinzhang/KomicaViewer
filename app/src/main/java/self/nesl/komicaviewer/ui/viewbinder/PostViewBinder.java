package self.nesl.komicaviewer.ui.viewbinder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.Render;

public class PostViewBinder {
    public TextView txtPostId;
    public TextView txtPostInd;
    public TextView txtPoster;
    public TextView txtTime;
    public FrameLayout contener;
    public ImageView img;
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
        contener = v.findViewById(R.id.contener);
        img = v.findViewById(R.id.img);
    }

    public void bind(){
        setDetail();
        contener.removeAllViews();
        contener.addView(render.render());
        setImage();
    }

    private void setDetail() {
        txtPostId.setText("No." + post.getId());
        txtPoster.setText(post.getPoster());
        txtTime.setText(post.getTimeStr());
    }

    private void setImage(){
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
}