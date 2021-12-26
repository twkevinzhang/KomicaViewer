package self.nesl.komicaviewer.ui.local;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.repository.CategoryListRepository;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.ui.PagingListFragment;
import self.nesl.komicaviewer.ui.PagingViewModel;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.board.ThreadListAdapter;
import self.nesl.komicaviewer.ui.home.BoardListViewModel;

public class HistoryFragment extends PagingListFragment<Post> {
    private HistoryViewModel mViewModel;
    private ThreadListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
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
