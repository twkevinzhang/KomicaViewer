package self.nesl.komicaviewer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.StaticString;
import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.view.post.PostActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ReplylistViewHolder>  {
    private ArrayList<Post> postlist=new ArrayList<Post>();
    private Context context;
    private boolean isBased;

    public PostAdapter(Context context) {
        this.context=context;
    }

    // 建立ViewHolder
    public class ReplylistViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private ImageView imgPost;
        private TextView txtTitle;
        private TextView txtPostId;
        private TextView txtPostInd;
        private TextView txtPoster;
//        private TextView txtPostlistMsg;

        ReplylistViewHolder(View v) {
            super(v);
            imgPost = v.findViewById(R.id.imgReply);
            txtTitle = v.findViewById(R.id.txtReplyTitle);
            txtPostInd = v.findViewById(R.id.txtReplyInd);
            txtPostId = v.findViewById(R.id.txtReplyId);
            txtPoster=v.findViewById(R.id.txtReplyer);
//            txtPostlistMsg = v.findViewById(R.id.txtPostlistMsg);
        }
    }


    @NonNull
    @Override
    public ReplylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply, parent, false);
        return new ReplylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReplylistViewHolder holder, final int i) {
        Post post=postlist.get(i);
        holder.txtPostInd.setText(post.getQuote());
        holder.txtPostId.setText("No."+post.getId());
        holder.txtTitle.setText(post.getTitle());
//        holder.txtPoster.setText(post.getPoster_id());
        holder.txtPoster.setText(post.getTimeStr());

        // set pic_url
        String pic_url = post.getPicUrl();
        if (pic_url != null && pic_url.length() > 0) {
            String head = "https://";
            if (pic_url.substring(0, 1).equals("/") && !pic_url.substring(0, 2).equals("//")) {
                pic_url = head + post.getParentBoard().getDomainUrl() + pic_url;
            } else if (!pic_url.substring(0, head.length()).equals(head)) {
                pic_url = head + pic_url;
            }

            Glide.with(holder.imgPost.getContext())
                    .load(pic_url)
                    // 如果pic_url載入失敗 or pic_url == null
                    .placeholder(null)
                    .fitCenter()
                    .into(holder.imgPost);
        }else{
            Glide.with(holder.imgPost.getContext()).clear(holder.imgPost);
        }
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public void addMultiPostToList(ArrayList<Post> postlist) {
        this.postlist.addAll(postlist);
    }
    public void setPostlist(ArrayList<Post> postlist) {
        this.postlist=postlist;
    }

}