package self.nesl.komicaviewer.ui.sora;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.util.getHasHttpUrl;

public class SoraFragment extends Fragment {

    private SoraViewModel soraViewModel;
    private static String boardUrl="https://sora.komica.org/00";
    private boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soraViewModel = ViewModelProviders.of(this).get(SoraViewModel.class);
        if (getArguments() != null) {
            boardUrl = getArguments().getString("boardUrl");
        }
        soraViewModel.setBoardUrl(boardUrl);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final PostlistAdapter adapter = new PostlistAdapter(getContext());
        final TextView txtMsg=v.findViewById(R.id.txtMsg);

        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);

        lst.setAdapter(adapter);

        // data and adapter
        soraViewModel.load(0);
        soraViewModel.getPostlist().observe(this, new Observer<ArrayList<Post>>() {
            int start, end = 0;
            @Override
            public void onChanged(ArrayList<Post> posts) {
                assert posts != null;
                adapter.addAllPost(posts);
                start = end;
                end = start + posts.size();
                adapter.notifyItemRangeInserted(start, end);
                isLoading = false;
            }
        });

        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (isLoading) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    isLoading = true;
                    page += 1;
                    txtMsg.setText("載入中" + page);
                    soraViewModel.load(page);


                } else if (!recyclerView.canScrollVertically(-1)) {
                    // 如果不能向上滑動，到頂了
                    txtMsg.setText("到頂了(不能向上滑動)");

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if(dy<0){
//                    fab_menu_list.showMenuButton(true);
//                }else{
//                    fab_menu_list.hideMenuButton(true);
//                }
//                if(!recyclerView.canScrollVertically(-1)){
//                    // 如果不能向上滑動，到頂了
//                    fab_menu_list.showMenuButton(true);
//                }
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                isLoading=true;
                soraViewModel.load(0);
                adapter.notifyDataSetChanged();
                isLoading=false;
                cateSwipeRefreshLayout.setRefreshing(isLoading);
            }
        });

        return v;
    }


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
            private TextView txtTime;
//        private TextView txtPostlistMsg;

            PostlistViewHolder(View v) {
                super(v);
                imgPost = v.findViewById(R.id.img);
                txtTitle = v.findViewById(R.id.txtTitle);
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
            Post post=postlist.get(i);
            holder.txtPostInd.setText(post.getIntroduction(100,null));
            holder.txtPostId.setText("No."+post.getPostId());
            holder.txtReplyCount.setText("回應:"+post.getReplyCount());
            holder.txtTitle.setText(post.getTitle(25));
//        holder.txtPoster.setText(post.getPoster_id());
            holder.txtPoster.setText(post.getPoster());
            holder.txtTime.setText(post.getTimeStr());



            // set pic_url
            String pic_url=post.getThumbnailUrl();

            if (pic_url!=null && !pic_url.equals("null") && pic_url.length() > 0) {
                String head = "https://";
                if (pic_url.substring(0, 1).equals("/") && !pic_url.substring(0, 2).equals("//")) {
                    pic_url = head + boardUrl + pic_url;
                } else if (!pic_url.substring(0, head.length()).equals(head)) {
                    pic_url = head + pic_url;
                }
                pic_url= getHasHttpUrl(pic_url, boardUrl);


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

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PostDB.addPost(postlist.get(i), StaticString.HISTORY_TABLE_NAME);
//                Intent intent = new Intent(context, ReplylistActivity.class);
//                intent.putExtra("post", postlist.get(i));
//                context.startActivity(intent);
//            }
//        });
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
        public void clear() {
            postlist.clear();
        }
    }
}
