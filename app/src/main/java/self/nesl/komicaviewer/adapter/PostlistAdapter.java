package self.nesl.komicaviewer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.post.PostFragment;

import static self.nesl.komicaviewer.Const.IS_TEST;
import static self.nesl.komicaviewer.Const.POST_URL;
import static self.nesl.komicaviewer.util.util.getHasHttpUrl;
import static self.nesl.komicaviewer.util.util.print;

public class PostlistAdapter extends RecyclerView.Adapter<PostlistAdapter.PostlistViewHolder> {
    private ArrayList<Post> postlist;
    private Fragment fragment;
    private CallBack callBack;

    public PostlistAdapter(Fragment fragment, CallBack callBack) {
        this.fragment = fragment;
        this.postlist = new ArrayList<Post>();
        this.callBack = callBack;
    }

    // 建立ViewHolder
    public class PostlistViewHolder extends RecyclerView.ViewHolder {
        // 宣告元件
        private ImageView imgPost;
        private TextView txtPostId;
        private TextView txtPostInd;
        private TextView txtReplyCount;
        private TextView txtPoster;
        private TextView txtTime;

        PostlistViewHolder(View v) {
            super(v);
            imgPost = v.findViewById(R.id.img);
            txtTime = v.findViewById(R.id.txtTime);
            txtPostInd = v.findViewById(R.id.txtInd);
            txtReplyCount = v.findViewById(R.id.txtReplyCount);
            txtPostId = v.findViewById(R.id.txtId);
            txtPoster = v.findViewById(R.id.txtPoster);
        }
    }

    @NonNull
    @Override
    public PostlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostlistViewHolder holder, final int i) {
        final Post post = postlist.get(i);
        holder.txtPostInd.setText(Html.fromHtml(post.getQuoteElement().html()));
        holder.txtPostId.setText("No." + post.getPostId());
        holder.txtReplyCount.setText("回應:" + post.getReplyCount());
        holder.txtPoster.setText(post.getPoster());
        holder.txtTime.setText(post.getTimeStr());

        // set pic_url
        String pic_url = post.getThumbnailUrl();
        pic_url=pic_url != null? getHasHttpUrl(pic_url, post.getBoardUrl()): "";
        holder.imgPost.setTag(R.id.imageid, pic_url);
        // 通过 tag 来防止图片错位
        if (holder.imgPost.getTag(R.id.imageid).equals(pic_url)) {
            Glide.with(holder.imgPost.getContext())
                    .load(pic_url)
                    .fitCenter()
                    .into(holder.imgPost);
        }
        holder.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(fragment.getActivity(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PostDB.addPost(postlist.get(i), StaticString.HISTORY_TABLE_NAME);
                callBack.itemOnClick(post);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public void addAllPost(ArrayList<Post> postlist) {
        this.postlist.addAll(postlist);
    }

    public void addThreadpost(Post post) {
        this.postlist.add(0, post);
    }

    public void clear() {
        postlist.clear();
    }

    public interface CallBack {
        public void itemOnClick(Post post);
    }
}