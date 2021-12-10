package self.nesl.komicaviewer.ui.viewholder;

import static self.nesl.komicaviewer.ui.thread.ReplyDialog.COLUMN_POST;
import static self.nesl.komicaviewer.ui.thread.ReplyDialog.COLUMN_POST_LIST;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
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
    public FrameLayout contener;
    public ImageView img;
    private FragmentManager fragmentManager;

    public PostViewHolder(View v, FragmentManager FragmentManager) {
        super(v);
        txtTime = v.findViewById(R.id.txtTime);
        txtReplyCount = v.findViewById(R.id.txtReplyCount);
        txtPostId = v.findViewById(R.id.txtId);
        txtPoster = v.findViewById(R.id.txtPoster);
        txtPostInd = v.findViewById(R.id.txtPostInd);
        contener = v.findViewById(R.id.contener);
        img = v.findViewById(R.id.img);
        this.fragmentManager = FragmentManager;
    }

    public void bind(Post post, List<Post> posts){
        setDetail(post);

        Context context = itemView.getContext();
        PostRender render = new PostRender(context);
        render.setOnReplyToClickListener(new PostRender.OnPostClickListener() {
            @Override
            public void onReplyToClick(Post replyTo, List<Post> list) {
                Toast.makeText(context, replyTo.getId(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable(COLUMN_POST,replyTo);
                bundle.putParcelableArrayList(COLUMN_POST_LIST, new ArrayList<>(list));
                ReplyDialog.newInstance(bundle).show(fragmentManager, "ReplyDialog2");
            }

            @Override
            public void onLinkClick(String link) {

            }
        });
        contener.removeAllViews();
        contener.addView(render.render(post, posts));

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