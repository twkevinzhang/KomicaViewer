package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class PostListFragment extends SampleListFragment<Post, Post> {
    private PostListViewModel postListViewModel;
    private PostListAdapter adapter;

    @Override
    protected SampleViewModel<Post, Post> getViewModel() {
        if(postListViewModel == null)
            postListViewModel = ViewModelProviders.of(this).get(PostListViewModel.class);
        return postListViewModel;
    }

    @Override
    protected SampleAdapter<Post, ? extends RecyclerView.ViewHolder> getAdapter() {
        if(adapter == null)
            adapter = new PostListAdapter();
        return adapter;
    }

    @Override
    protected int getMaxPage(){ return 0; }
}
