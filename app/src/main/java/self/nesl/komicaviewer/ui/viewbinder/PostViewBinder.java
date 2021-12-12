package self.nesl.komicaviewer.ui.viewbinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.PostRender;

public class PostViewBinder {
    public TextView txtPostId;
    public TextView txtPostInd;
    public TextView txtPoster;
    public TextView txtTime;
    public FrameLayout contener;
    public ImageView img;
    Post post;
    Context context;
    Activity activity;

    public PostViewBinder(View v, Post post) {
        context= v.getContext();
        this.post=post;
        txtTime = v.findViewById(R.id.txtTime);
        txtPostId = v.findViewById(R.id.txtId);
        txtPoster = v.findViewById(R.id.txtPoster);
        txtPostInd = v.findViewById(R.id.txtPostInd);
        contener = v.findViewById(R.id.contener);
        img = v.findViewById(R.id.img);
    }

    public void setActivity(Activity activity){
        this.activity=activity;
    }

    public void render(){
        setDetail();
        contener.removeAllViews();
        contener.addView(contentView(activity));
        setImage();
    }

    public void setDetail() {
        txtPostId.setText("No." + post.getId());
        txtPoster.setText(post.getPoster());
        txtTime.setText(post.getTimeStr());
    }

    public View contentView(Activity activity){
        PostRender render = new PostRender(context, post);
        render.setOnLinkClickListener(link -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            activity.startActivity(browserIntent);
        });
        return render.render();
    }

    public void setImage(){
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