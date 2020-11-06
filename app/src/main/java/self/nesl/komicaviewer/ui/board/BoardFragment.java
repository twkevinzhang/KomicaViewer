package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.ui.BaseFragment;
import self.nesl.komicaviewer.ui.adapter.ThreadlistAdapter;
import self.nesl.komicaviewer.ui.post.PostFragment;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardFragment extends BaseFragment {
    public static final String COLUMN_BOARD_URL = "board";
    private BoardViewModel boardViewModel;
    private String boardUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardViewModel = ViewModelProviders.of(this).get(BoardViewModel.class);
        if (getArguments() != null) {
            boardUrl = getArguments().getString(BoardFragment.COLUMN_BOARD_URL);
            boardViewModel.setBoardUrl(boardUrl);
            if(boardViewModel.getPost().getValue()==null){
                boardViewModel.load(0);
            }
        }

        super.init(boardViewModel, 99, new ThreadlistAdapter(this,new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString(PostFragment.COLUMN_POST_URL, post.getUrl());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_board_to_nav_post, bundle);
            }
        }));

        }

    @Override
    public void whenDataChange(PostlistAdapter adapter, Post board) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(board.getTitle(0));
    }
}

