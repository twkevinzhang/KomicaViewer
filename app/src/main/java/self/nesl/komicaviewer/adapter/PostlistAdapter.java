package self.nesl.komicaviewer.adapter;

import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.util.MyURL;

import static self.nesl.komicaviewer.util.Util.print;

public class PostlistAdapter extends RecyclerView.Adapter<PostlistAdapter.PostlistViewHolder> {
    private ArrayList<Post> postlist;
    private Fragment fragment;
    private ItemOnClickListener callBack;

    public PostlistAdapter(Fragment fragment, ItemOnClickListener callBack) {
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

        // set picUrl
        ArrayList<Picture> pics = post.getPics();
        String thumbUrl="";
        String picUrl="";
        if(pics.size() != 0){
            thumbUrl = pics.get(0).getThumbnailUrl();
            picUrl =pics.get(0).getOriginalUrl();
        }

        // 通過tag來防止錯位、忽大忽小
        holder.imgPost.setTag(R.id.imageid, thumbUrl);
        if (holder.imgPost.getTag(R.id.imageid).equals(thumbUrl)) {
            Glide.with(holder.imgPost.getContext())
                    .load(thumbUrl)
                    .fitCenter()
                    .into(holder.imgPost);

            String finalPicUrl = picUrl;
            holder.imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics( metrics);
                    Glide.with(holder.imgPost.getContext())
                            .load(finalPicUrl)
                            .fitCenter()
                            .into(holder.imgPost)
                            .getSize(new SizeReadyCallback() {
                                @Override
                                public void onSizeReady(int width, int height) {
                                    holder.imgPost.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            metrics.widthPixels / width * height
                                    ));
                                }
                            });
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public interface ItemOnClickListener {
        public void itemOnClick(Post post);
    }
}