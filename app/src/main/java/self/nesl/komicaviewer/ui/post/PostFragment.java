package self.nesl.komicaviewer.ui.post;
import android.os.Build;
import android.os.Bundle;
import java.text.MessageFormat;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.ui.BaseFragment;

import static self.nesl.komicaviewer.util.Utils.print;

// nav_post
public class PostFragment extends BaseFragment {
    private PostViewModel postViewModel;
    public static final String COLUMN_POST_URL = "post";
    private String postUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (getArguments() != null) {
            postUrl=getArguments().getString(COLUMN_POST_URL);
            postViewModel.setPostUrl(postUrl);
            if(postViewModel.getPost().getValue()==null){
                postViewModel.load(0);
            }
        }

        super.init(postViewModel, 0, new PostlistAdapter(this, new PostlistAdapter.ItemOnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void itemOnClick(Post post) {
                if (post.getReplyCount() != 0 &&
                        !post.getUrl().equals(postUrl)) {
                    ReplyDialog dialog=ReplyDialog.newInstance(post);
                    dialog.show(getFragmentManager(), post.getPostId() + "dialog");
                }

            }
        }));
    }

    @Override
    public void whenDataChange(PostlistAdapter adapter, Post post) {
        if(post.getShow()!=null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(post.getTitle(0));
            Post mainThreadVO = post.clone();
            mainThreadVO.getShow().append(MessageFormat.format("<br><a href=\"{0}\">原文連結: {0}</a>", post.getUrl()));
            adapter.addThreadpost(mainThreadVO);
        }
    }
}
