package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseFragment;

import static self.nesl.komicaviewer.Const.TREE;
import static self.nesl.komicaviewer.util.Utils.print;

// nav_post
public class PostFragment extends BaseFragment {
    private PostViewModel postViewModel;
    public static final String COLUMN_POST_URL = "postUrl";

    PostlistAdapter adapter = new PostlistAdapter(null);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        super.init(postViewModel, true, false, new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                new ReplyDialog(post, getFragmentManager());
            }
        });
        if (getArguments() != null) {
            postViewModel.setPostUrl(getArguments().getString(COLUMN_POST_URL));
            postViewModel.load(0);
        }
    }

    @Override
    public void whenDataChange(PostlistAdapter adapter, Post post) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(post.getTitle(0));
        if(TREE){
            this.adapter.addThreadpost(post);
            adapter.addAllPost(post.getReplyTree());
        }else{
            adapter.addThreadpost(post);
            adapter.addAllPost(post.getReplies());
        }
    }


    @Override
    public View doSomething(View v) {
        final RecyclerView lst = v.findViewById(R.id.rcLst2);
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);

        return v;
    }
}
