package self.nesl.komicaviewer.ui.home;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.repository.CategoryListRepository;
import self.nesl.komicaviewer.repository.Repository;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;
import self.nesl.komicaviewer.ui.board.ThreadListFragment;

public class BoardListFragment extends SampleListFragment<Category, Board> {
    public static final String COLUMN_CATEGORY = "CATEGORY";
    private BoardListViewModel boardListViewModel;
    private BoardListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardListViewModel = ViewModelProviders.of(this).get(BoardListViewModel.class);
        if (getArguments() != null){
            Category category = (Category) getArguments().getSerializable(COLUMN_CATEGORY);
            boardListViewModel.setCategory(category);
        }else{
            Repository<List<Category>> repo = new CategoryListRepository();
            repo.get(categories -> {
                boardListViewModel.setCategory(categories.get(0));
            });
        }
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        NavController navController = Navigation.findNavController(root);
        adapter.setOnClickListener((view, board) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ThreadListFragment.COLUMN_BOARD, board);
            navController.navigate(R.id.action_nav_home_to_nav_board, bundle);
        });
    }

    @Override
    protected SampleViewModel<Category, Board> getViewModel() {
        return boardListViewModel;
    }

    @Override
    protected SampleAdapter<Board> getAdapter() {
        if(adapter == null)
            adapter = new BoardListAdapter();
        return adapter;
    }
}
