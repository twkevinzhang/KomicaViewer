package self.nesl.komicaviewer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class PostlistAdapter extends RecyclerView.Adapter<PostlistAdapter.PostlistViewHolder>  {
    private ArrayList<Post> postlist;
    private Fragment fragment;

    public PostlistAdapter(Fragment fragment) {
        this.fragment=fragment;
        this.postlist=new ArrayList<Post>();
    }

    // 建立ViewHolder
    public class PostlistViewHolder extends RecyclerView.ViewHolder{
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
        final Post post=postlist.get(i);
        holder.txtPostInd.setText(Html.fromHtml(post.getQuoteElement().html()));
        print(post.getQuoteElement().html());
        holder.txtPostId.setText("No."+post.getPostId());
        holder.txtReplyCount.setText("回應:"+post.getReplyCount());
        holder.txtPoster.setText(post.getPoster());
        holder.txtTime.setText(post.getTimeStr());

        // set pic_url
        String pic_url=post.getThumbnailUrl();
        if (pic_url!=null && !pic_url.equals("null") && pic_url.length() > 0) {
            pic_url= getHasHttpUrl(pic_url, post.getBoardUrl());
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
//                PostDB.addPost(postlist.get(i), StaticString.HISTORY_TABLE_NAME);
                Bundle bundle = new Bundle();
                if (IS_TEST)bundle.putString("postUrl", POST_URL);
                else bundle.putString("postUrl", post.getUrl());
                Navigation.findNavController(fragment.getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_komica_sora_to_nav_post,bundle);
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

    public void addAllPost(ArrayList<Post> postlist) {
        this.postlist.addAll(postlist);
    }

    public void addThreadpost(Post post) {
        this.postlist.add(0,post);
    }
    public void clear() {
        postlist.clear();
    }
}