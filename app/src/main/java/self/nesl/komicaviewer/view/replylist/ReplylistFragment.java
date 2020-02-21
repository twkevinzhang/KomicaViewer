package self.nesl.komicaviewer.view.replylist;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;

import static android.app.Activity.RESULT_OK;

public class ReplylistFragment extends Fragment {

    private ReplylistViewModel replylistViewModel;
    private Post post;
    private RecyclerView lst;
    private FloatingActionMenu fab_menu;
    private LinearLayout commentBar;
    private ImageView send;
    private TextView txtComment;
    private FloatingActionButton fab_openUrl;
    private FloatingActionButton fab_addToFavorite;
    private FloatingActionButton fab_reply;
    private FloatingActionButton fab_p_to_top;
    private FloatingActionButton fab_p_to_last;
    private SwipeRefreshLayout cateSwipeRefreshLayout;
    private ImageView btnClose;
    private ImageView btnReply;
    private ImageView btnSelectPic;
    private Drawable defaultSelectImg;

    public ReplylistFragment() {
        // Required empty public constructor
    }

    public static ReplylistFragment newInstance(Bundle args) {
        ReplylistFragment fragment = new ReplylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable("post");
            replylistViewModel = ViewModelProviders.of(this).get(ReplylistViewModel.class);
            replylistViewModel.setPost(post);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_postlist, container, false);
        replylistViewModel.loadReplylist(post.getParentBoard());

        lst = v.findViewById(R.id.lst);
        fab_menu = v.findViewById(R.id.fab_menu_list);
        commentBar = v.findViewById(R.id.commentBar);
        send = v.findViewById(R.id.btnSend);
        txtComment = v.findViewById(R.id.txtComment);
        btnClose = v.findViewById(R.id.btnClose);
        btnReply = v.findViewById(R.id.btnReply);
        btnSelectPic = v.findViewById(R.id.btnSelectPic);
        defaultSelectImg = btnSelectPic.getDrawable();

        // fab openUrl
        fab_openUrl = new FloatingActionButton(getActivity());
        fab_openUrl.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_openUrl.setLabelText(getString(R.string.fab_open_url));
        fab_openUrl.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_openUrl);
        fab_openUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLink()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    getContext().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    getContext().startActivity(intent);
                }
            }
        });

        // fab addToFavorite
        fab_addToFavorite = new FloatingActionButton(getActivity());
        fab_addToFavorite.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_addToFavorite.setLabelText(getString(R.string.fab_add_to_favorite));
        fab_addToFavorite.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_addToFavorite);
//        fab_addToFavorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fab_menu.close(true);
//                PostDB.switchTable(StaticString.FAVORITE_TABLE_NAME);
//                PostDB.addPost(post);
//            }
//        });

        // fab reply
        fab_reply = new FloatingActionButton(getActivity());
        fab_reply.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_reply.setLabelText(getString(R.string.fab_reply));
        fab_reply.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_reply);
        fab_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
//                Intent intent=new Intent(getContext(), PostActivity.class);
//                Bundle bundle=new Bundle();
//                // todo test
//                bundle.putString("masterPostId",TEST_POST_ID);
////                intent.putExtra("masterPostId",post.getId());
//                bundle.putSerializable("board",post.getParentBoard());
//                intent.putExtra("bundle",bundle);
//                startActivity(intent);
                commentBar.setVisibility(View.VISIBLE);
            }
        });

        // fab to_top
        fab_p_to_top = new FloatingActionButton(getActivity());
        fab_p_to_top.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_p_to_top.setLabelText(getString(R.string.fab_to_top));
        fab_p_to_top.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_p_to_top);
        fab_p_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                lst.scrollToPosition(0);
            }
        });

        // fab to_last
        fab_p_to_last = new FloatingActionButton(getActivity());
        fab_p_to_last.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab_p_to_last.setLabelText(getString(R.string.fab_to_last));
        fab_p_to_last.setImageResource(R.drawable.ic_edit);
        fab_menu.addMenuButton(fab_p_to_last);
        fab_p_to_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                lst.scrollToPosition(lst.getAdapter().getItemCount() - 1);
            }
        });

        // lst
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
        final ReplylistAdapter adapter = new ReplylistAdapter(getContext());
        lst.setAdapter(adapter);

        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(dy<0){
//                    fab_menu.showMenuButton(true);
//                }else{
//                    fab_menu.hideMenuButton(true);
//                }
//                if(!recyclerView.canScrollVertically(-1)){
//                    // 如果不能向上滑動，到頂了
//                    fab_menu.showMenuButton(true);
//                }
            }

        });

        // data and adapter
        replylistViewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(@Nullable final Post post) {
                assert post != null;
                ArrayList<Post> arr2 = post.getReplyAll();
                arr2.add(0, post);
                adapter.setPostlist(arr2);
                adapter.notifyDataSetChanged();
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setPostlist(new ArrayList<Post>());
                replylistViewModel.loadReplylist(post.getParentBoard());
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });


        // comment bar: send btn
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Post().setTitle("無題")
                        .setQuote(txtComment.getText().toString())
                        .setParentBoard(post.getParentBoard())
                        .setMasterPostId(post.getId())
                        .pushToKomica();
                Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                finishCommentBar();
            }
        });

        //comment bar: cancel btn
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishCommentBar();
            }
        });

        //comment bar: SelectPic btn
        btnSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        return v;
    }

    private void finishCommentBar() {
        commentBar.setVisibility(View.GONE);
        txtComment.setText("");
        btnSelectPic.setImageDrawable(defaultSelectImg);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (resultCode) {
            case RESULT_OK:
                Uri selectedImage = imageReturnedIntent.getData();
                Log.e("RlF,onActivityResult()", selectedImage.toString());
                btnSelectPic.setImageURI(selectedImage);
                break;
        }
    }

    class ReplylistAdapter extends RecyclerView.Adapter<ReplylistAdapter.PostViewHolder> {
        private ArrayList<Post> postlist = new ArrayList<Post>();
        private Context context;

        public ReplylistAdapter(Context context) {
            this.context = context;
        }

        // 建立ViewHolder
        public class PostViewHolder extends RecyclerView.ViewHolder {
            // 宣告元件
            private ImageView imgPost;
            private TextView txtPostId;
            private TextView txtReply;
            private TextView txtReplyDate;
            private ImageView btnReply;
            private ImageView btnSelectPic;

            PostViewHolder(View v) {
                super(v);
                imgPost = v.findViewById(R.id.imgReply);
                txtReply = v.findViewById(R.id.txtReply);
                txtPostId = v.findViewById(R.id.txtReplyId);
                txtReplyDate = v.findViewById(R.id.txtReplyDate);
                btnReply = v.findViewById(R.id.btnReply);
                btnSelectPic = v.findViewById(R.id.btnSelectPic);
            }
        }


        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reply, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PostViewHolder holder, final int i) {
            final Post post = postlist.get(i);
            holder.txtReply.setText(Html.fromHtml(post.getQuoteHtml()));
            holder.txtPostId.setText("No." + post.getId());
            holder.txtReplyDate.setText(post.getTimeStr());

            // set pic_url
            String pic_url = post.getPicUrl();
            if (pic_url != null && pic_url.length() > 0) {
                if(pic_url.substring(0, 1).equals("/")){
                    if(!pic_url.substring(0, 2).equals("//")){
                        pic_url = "https://" + post.getParentBoard().getDomainUrl() + pic_url;
                    }else{
                        pic_url="https:"+pic_url;
                    }
                }

                Glide.with(holder.imgPost.getContext())
                        .load(pic_url)
                        // 如果pic_url載入失敗 or pic_url == null
                        .placeholder(null)
                        .fitCenter()
                        .into(holder.imgPost);
            } else {
                Glide.with(holder.imgPost.getContext()).clear(holder.imgPost);
            }

            // btnReply
            holder.btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentBar.setVisibility(View.VISIBLE);
                    String targetTxt = ">>" + post.getId() + "\n";
                    txtComment.setText(txtComment.getText() + targetTxt);
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

        public void addMultiPostToList(ArrayList<Post> postlist) {
            this.postlist.addAll(postlist);
        }

        public void setPostlist(ArrayList<Post> postlist) {
            this.postlist = postlist;
        }

    }
}
