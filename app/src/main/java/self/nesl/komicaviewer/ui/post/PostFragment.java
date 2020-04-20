package self.nesl.komicaviewer.ui.post;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

import static self.nesl.komicaviewer.util.util.getParseNameByUrl;
import static self.nesl.komicaviewer.util.util.print;

public class PostFragment extends Fragment {
    private PostViewModel postViewModel;
    private String postUrl;

    public static PostFragment newInstance(@NonNull String postUrl) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("postUrl", postUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (getArguments() != null) {
            postUrl = getArguments().getString("postUrl");
        }
        postViewModel.setPostUrl(postUrl);
        postViewModel.setFormat(getParseNameByUrl(postUrl,getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final PostlistAdapter adapter = new PostlistAdapter(this);
        final TextView txtMsg=v.findViewById(R.id.txtMsg);

        // lst
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        lst.setLayoutManager(layoutManager);
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
