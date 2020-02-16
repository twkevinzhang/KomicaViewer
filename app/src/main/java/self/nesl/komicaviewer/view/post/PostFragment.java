package self.nesl.komicaviewer.view.post;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;

public class PostFragment extends Fragment {
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public PostFragment() {
        // Required empty public constructor
    }

    private Board board;
    private String masterPostId;

    public static PostFragment newInstance(Bundle args) {
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            board = (Board) getArguments().getSerializable("board");
            masterPostId = getArguments().getString("masterPostId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_post, container, false);
        Toolbar toolbar=v.findViewById(R.id.post_toolbar);
        final TextView txtPostTitle=v.findViewById(R.id.txtPostTitle);
        final TextView txtPostInd=v.findViewById(R.id.txtPostInd);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_post:
                        new Post().setTitle(txtPostTitle.getText().toString())
                            .setQuote(txtPostInd.getText().toString())
                            .setParentBoard(board)
                            .pushToKomica();
                        getActivity().finish();
                        break;
                }
                return false;
            }
        });

        return v;
    }



}
