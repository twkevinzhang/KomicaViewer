package self.nesl.komicaviewer.ui.home;

import static self.nesl.komicaviewer.ui.board.ThreadListViewModel.COLUMN_THREAD_LIST;
import static self.nesl.komicaviewer.ui.home.BoardListViewModel.COLUMN_HOST;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.repository.CategoryRepository;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.SampleListFragment;
import self.nesl.komicaviewer.ui.SampleViewModel;

public class BoardListFragment extends SampleListFragment<Category, Board> {
    private BoardListViewModel boardListViewModel;
    private BoardListAdapter adapter;

    @Override
    protected void initAdapter() {
        super.initAdapter();
        NavController navController = Navigation.findNavController(root);
        adapter.setOnClickListener((view, board) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(COLUMN_THREAD_LIST, board);
            navController.navigate(R.id.action_nav_home_to_nav_board, bundle);
        });
    }

    @Override
    protected SampleViewModel<Category, Board> getViewModel() {
        if(boardListViewModel == null){
            boardListViewModel = ViewModelProviders.of(this).get(BoardListViewModel.class);
            if (getArguments() != null){
                boardListViewModel.setArgs(getArguments());
            }else{
                CategoryRepository repo = new CategoryRepository();
                repo.getAll(categories -> {
                    Bundle args = new Bundle();
                    args.putSerializable(COLUMN_HOST, categories.get(0));
                    boardListViewModel.setArgs(args);
                });
            }
        }
        return boardListViewModel;
    }

    @Override
    protected SampleAdapter<Board> getAdapter() {
        if(adapter == null)
            adapter = new BoardListAdapter();
        return adapter;
    }
}
