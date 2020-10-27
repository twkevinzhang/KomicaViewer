package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.ui.adapter.ThreadAdapter;
import self.nesl.komicaviewer.ui.BaseFragment;
import self.nesl.komicaviewer.ui.adapter.ThreadlistAdapter;
import self.nesl.komicaviewer.ui.post.PostFragment;

import static self.nesl.komicaviewer.util.Utils.print;

public class BoardFragment extends BaseFragment {
    public static final String COLUMN_BOARD_URL = "board";
    public static final String COLUMN_BOARD_TITLE = "boardTitle";
    private String boardUrl;
    private String boardTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setViewModel(ViewModelProviders.of(this).get(BoardViewModel.class));
        super.setMaxPage(99);
        super.setAdapter(new ThreadlistAdapter(this,new ThreadAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(KThread thread) {
                Bundle bundle = new Bundle();
                bundle.putString(PostFragment.COLUMN_POST_URL, thread.getUrl());
                bundle.putString(PostFragment.COLUMN_POST_TITLE, thread.getTitle());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_board_to_nav_post, bundle);
            }
        }));

        if (getArguments() != null) {
            boardTitle = getArguments().getString(BoardFragment.COLUMN_BOARD_TITLE);
            boardUrl = getArguments().getString(BoardFragment.COLUMN_BOARD_URL);
            super.getViewModel().setPostUrl(boardUrl);
            if(super.getViewModel().getPost().getValue()==null){
                super.getViewModel().load(0);
            }
        }
    }

    @Override
    public void whenDataChange(ThreadAdapter adapter, KThread board) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(boardTitle);
    }
}

