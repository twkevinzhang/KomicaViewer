package self.nesl.komicaviewer.ui.collection;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.PagingListFragment;
import self.nesl.komicaviewer.ui.PagingViewModel;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.board.ThreadListAdapter;
import self.nesl.komicaviewer.ui.thread.ThreadFragment;

public class CollectionFragment extends PagingListFragment<Post> {
    private CollectionViewModel mViewModel;
    private ThreadListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        NavController navController = Navigation.findNavController(root);
        adapter.setOnClickListener((view, thread) -> {
            Bundle bundle = new Bundle();
            bundle.putString(ThreadFragment.COLUMN_POST_URL, thread.getUrl());
            navController.navigate(R.id.action_nav_collection_to_nav_post, bundle);
        });
    }

    @Override
    protected PagingViewModel<Post> getViewModel() {
        return mViewModel;
    }

    @Override
    protected SampleAdapter<Post> getAdapter() {
        if(adapter == null)
            adapter = new ThreadListAdapter();
        return adapter;
    }
}
