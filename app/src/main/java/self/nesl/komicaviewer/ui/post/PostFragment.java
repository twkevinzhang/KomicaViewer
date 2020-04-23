package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.util.getParserByUrl;

public class PostFragment extends Fragment {
    private PostViewModel postViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (getArguments() != null) {
            postViewModel.setPostUrl(getArguments().getString("postUrl"));
            postViewModel.setFormat((Post)getArguments().getSerializable("format"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg = v.findViewById(R.id.txtMsg);
        final PostlistAdapter adapter = new PostlistAdapter(this, new PostlistAdapter.CallBack() {
            @Override
            public void itemOnClick(Post post) {
                new ReplyDialog(post,getFragmentManager());
            }
        });


        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);

        // data and adapter
        postViewModel.update();
        postViewModel.getPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                assert post != null;
                adapter.addAllPost(post.getReplies());
                adapter.addThreadpost(post);
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }
}
