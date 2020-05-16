package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseFragment;
import self.nesl.komicaviewer.ui.post.PostFragment;
import static self.nesl.komicaviewer.Const.IS_TEST;
import static self.nesl.komicaviewer.Const.POST_URL;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardFragment extends BaseFragment {
    public static final String COLUMN_BOARD="board";
    private BoardViewModel boardViewModel;
    private Post board;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boardViewModel=ViewModelProviders.of(this).get(BoardViewModel.class);
        super.init(boardViewModel,true);
        if (getArguments() != null) {
            board = (Post)getArguments().getSerializable(BoardFragment.COLUMN_BOARD);
            boardViewModel.setBoardUrl(board.getUrl());
            boardViewModel.load(0);
        }
    }

    @Override
    public PostlistAdapter createAdapter(){
        return new PostlistAdapter(new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString(PostFragment.COLUMN_POST_URL, (IS_TEST)?POST_URL:post.getUrl());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_board_to_nav_post,bundle);
            }
        });
    }

    @Override
    public void setTitle(Post post) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(board.getTitle(0));
    }


}

