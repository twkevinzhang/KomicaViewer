package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseFragment;

import static self.nesl.komicaviewer.Const.TREE;
import static self.nesl.komicaviewer.util.Utils.print;

// nav_post
public class PostFragment extends BaseFragment {
    private PostViewModel postViewModel;
    public static final String COLUMN_POST = "post";
    private Post mainThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (getArguments() != null) {
            mainThread=(Post)getArguments().getSerializable(COLUMN_POST);
            postViewModel.setPost(mainThread);
            postViewModel.load(0);
        }

        super.init(postViewModel, 0, new PostlistAdapter(this,new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                new ReplyDialog(post, getFragmentManager());
            }
        }));
    }

    @Override
    public void whenDataChange(PostlistAdapter adapter, ArrayList<Post> arr) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mainThread.getTitle(0));
        adapter.addThreadpost(mainThread);
    }
}
