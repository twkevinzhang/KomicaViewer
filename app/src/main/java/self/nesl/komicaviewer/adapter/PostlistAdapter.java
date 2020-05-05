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
        holder.txtPostInd.setText(Html.fromHtml(post.getQuoteElement().html()));
        holder.txtPostId.setText("No." + post.getPostId());
        holder.txtReplyCount.setText("回應:" + post.getReplyCount());
        holder.txtPoster.setText(post.getPoster());
        holder.txtTime.setText(post.getTimeStr());
        holder.imgThumb.setVisibility(View.VISIBLE);
        holder.imgOri.setVisibility(View.GONE);

        // set picUrl
//        ArrayList<Picture> pics = post.getPics();
//        holder.imgThumb.setTag(R.id.imageid, post.getPostId());
//        if(pics.size() != 0) {
//            Picture pic=pics.get(0);
//            holder.imgThumb.setTag(R.id.imageid, pic.getThumbnailUrl());
//            if (holder.imgThumb.getTag(R.id.imageid).equals(pic.getThumbnailUrl())) {
//                Glide.with(holder.imgThumb.getContext())
//                        .load(pic.getThumbnailUrl())
//                        .fitCenter()
//                        .override(pic.getThumbWidth()*2,pic.getThumbHeight()*2)
//                        .into(holder.imgThumb);
//
//                holder.imgThumb.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.imgThumb.setVisibility(View.GONE);
//                        holder.imgOri.setVisibility(View.VISIBLE);
//
//                        DisplayMetrics metrics = new DisplayMetrics();
//                        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                        Glide.with(holder.imgOri.getContext())
//                                .load(pic.getOriginalUrl())
//                                .fitCenter()
//                                .into(holder.imgOri);
//                    }
//                });
//            }
//        }

        holder.imgThumb.setVisibility(View.VISIBLE);
        holder.imgOri.setVisibility(View.GONE);

        // set picUrl
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