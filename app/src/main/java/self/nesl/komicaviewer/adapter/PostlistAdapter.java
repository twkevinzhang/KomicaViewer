package self.nesl.komicaviewer.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import self.nesl.komicaviewer.view.replylist.ReplylistActivity;

import static self.nesl.komicaviewer.StaticString.TEST_POST_ID;

public class PostlistAdapter extends RecyclerView.Adapter<PostlistAdapter.PostlistViewHolder>  {
    private ArrayList<Post> postlist=new ArrayList<Post>();
    private Context context;

    public PostlistAdapter(Context context) {
        this.context=context;
    }

    // 建立ViewHolder
    public class PostlistViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private ImageView imgPost;
        private TextView txtTitle;
        private TextView txtPostId;
        private TextView txtPostInd;
        private TextView txtReplyCount;
        private TextView txtPoster;
//        private TextView txtPostlistMsg;

        PostlistViewHolder(View v) {
            super(v);
            imgPost = v.findViewById(R.id.imgPost);
            txtTitle = v.findViewById(R.id.txtPostTitle);
            txtPostInd = v.findViewById(R.id.txtPostInd);
            txtReplyCount = v.findViewById(R.id.txtReplyCnt);
            txtPostId = v.findViewById(R.id.txtPostId);
            txtPoster=v.findViewById(R.id.txtPoster);
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
        Post post=postlist.get(i);
        holder.txtPostInd.setText(post.getIntroduction(100,null));
        holder.txtPostId.setText("No."+post.getId());
        holder.txtReplyCount.setText("回應:"+post.getReplyCount());
        holder.txtTitle.setText(post.getTitle(25));
//        holder.txtPoster.setText(post.getPoster_id());
        holder.txtPoster.setText(post.getTimeStr());


        // set pic_url
        String pic_url=post.getThumbnailUrl();

        if (pic_url!=null && !pic_url.equals("null") && pic_url.length() > 0) {
            if(pic_url.substring(0, 1).equals("/")){
                if(!pic_url.substring(0, 2).equals("//")){
                    pic_url = "https://" + postlist.get(i).getParentBoard().getDomainUrl() + pic_url;
                }else{
                    pic_url="https:"+pic_url;
                }
            }

            holder.imgPost.setTag(R.id.imageid,pic_url);
            // 通过 tag 来防止图片错位
            if (holder.imgPost.getTag(R.id.imageid)!=null&&holder.imgPost.getTag(R.id.imageid)==pic_url) {
                Glide.with(holder.imgPost.getContext())
                        .load(pic_url)
                        // 如果pic_url載入失敗 or pic_url == null
                        .placeholder(null)
                        .fitCenter()
                        .into(holder.imgPost);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDB.addPost(postlist.get(i), StaticString.HISTORY_TABLE_NAME);
                Intent intent = new Intent(context, ReplylistActivity.class);
                // todo test
//                intent.putExtra("post", postlist.get(i));
                Bundle bundle=new Bundle();
                bundle.putSerializable("post", postlist.get(i));
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        });
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