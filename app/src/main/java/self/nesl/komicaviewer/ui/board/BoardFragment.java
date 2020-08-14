package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseFragment;
import self.nesl.komicaviewer.ui.adapter.ThreadlistAdapter;
import self.nesl.komicaviewer.ui.post.PostFragment;

import static self.nesl.komicaviewer.Const.IS_TEST;
import static self.nesl.komicaviewer.Const.POST_URL;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardFragment extends BaseFragment {
    public static final String COLUMN_BOARD = "board";
    private BoardViewModel boardViewModel;
    private Post board;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardViewModel = ViewModelProviders.of(this).get(BoardViewModel.class);
        if (getArguments() != null) {
            board = (Post) getArguments().getSerializable(BoardFragment.COLUMN_BOARD);
            boardViewModel.setBoard(board);
            boardViewModel.load(0);
        }

        super.init(boardViewModel, 99, new ThreadlistAdapter(this,new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(PostFragment.COLUMN_POST, post);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_board_to_nav_post, bundle);
            }
        }));
    }

    @Override
    public void whenDataChange(PostlistAdapter adapter, ArrayList<Post> arr) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(this.board.getTitle(0));
    }
}

