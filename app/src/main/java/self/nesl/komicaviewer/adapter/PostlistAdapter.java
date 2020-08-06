package self.nesl.komicaviewer.adapter;

import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Utils.print;

public class PostlistAdapter extends RecyclerView.Adapter<PostlistAdapter.PostlistViewHolder> {
    private ArrayList<Post> postlist;
    private ItemOnClickListener callBack;
    private Fragment fragment;

    public PostlistAdapter( Fragment fragment,ItemOnClickListener callBack) {
        this.postlist =  new ArrayList<Post>();
        this.callBack = callBack;
        this.fragment=fragment;
    }

    public class PostlistViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgThumb;
        private ImageView imgOri;
        private TextView txtPostId;
        private TextView txtPostInd;
        private TextView txtReplyCount;
        private TextView txtPoster;
        private TextView txtTime;

        PostlistViewHolder(View v) {
            super(v);
            imgThumb = v.findViewById(R.id.imgThumb);
            imgOri = v.findViewById(R.id.imgOri);
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

        // other
        holder.txtPostId.setText("No." + post.getPostId());
        holder.txtReplyCount.setText("回應:" + post.getReplyCount());
        holder.txtPoster.setText(post.getPoster());
        holder.txtTime.setText(post.getTimeStr());
        holder.imgThumb.setVisibility(View.VISIBLE);
        holder.imgOri.setVisibility(View.GONE);

        // imgThumb & imgOri
        ArrayList<Picture> pics = post.getPics();
        String thumbUrl="",picUrl="";
        if(pics.size() != 0){
            Picture pic=pics.get(0);
            thumbUrl = pic.getThumbnailUrl();
            picUrl =pic.getOriginalUrl();
        }

        // 通過tag來防止錯位、忽大忽小
        holder.imgThumb.setTag(R.id.imageid, thumbUrl);
        if (holder.imgThumb.getTag(R.id.imageid).equals(thumbUrl)) {
            Glide.with(holder.imgThumb.getContext())
                    .load(thumbUrl)
                    .fitCenter()
                    .into(holder.imgThumb);

            String finalPicUrl = picUrl;
            holder.imgThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imgThumb.setVisibility(View.GONE);
                    holder.imgOri.setVisibility(View.VISIBLE);

                    Glide.with(holder.imgOri.getContext())
                            .load(finalPicUrl)
                            .fitCenter()
                            .into(holder.imgOri);
                }
            });
        }

        // itemView
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null)callBack.itemOnClick(post);
            }
        };
        holder.itemView.setOnClickListener(onClickListener);

        // txtPostInd
        holder.txtPostInd.setText(Html.fromHtml(post.getQuoteElement().html()));
        holder.txtPostInd.setOnClickListener(onClickListener);

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
        LinkedHashSet set=  new LinkedHashSet<>();
        set.addAll(this.postlist);
        set.addAll(postlist);
        this.postlist.addAll(set);
    }

    public void setPostlist(ArrayList<Post> postlist) {
        this.postlist=postlist;
    }

    public void addThreadpost(Post post) {
        if(!postlist.contains(post))postlist.add(0, post);
    }

    public void clear() {
        postlist.clear();
    }

    public interface ItemOnClickListener {
        public void itemOnClick(Post post);
    }
}