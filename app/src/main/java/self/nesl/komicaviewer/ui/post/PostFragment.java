package self.nesl.komicaviewer.ui.post;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.ui.adapter.ThreadAdapter;
import self.nesl.komicaviewer.ui.BaseFragment;

import static self.nesl.komicaviewer.util.Utils.print;

// nav_post
public class PostFragment extends BaseFragment {
    public static final String COLUMN_POST_URL = "post";
    public static final String COLUMN_POST_TITLE = "title";
    private String postUrl;
    private String postTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setViewModel(ViewModelProviders.of(this).get(PostViewModel.class));
        super.setMaxPage(0);
        super.setAdapter(new ThreadAdapter(this, new ThreadAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(KThread thread) {
            }
        }));

        if (getArguments() != null) {
            postUrl = getArguments().getString(COLUMN_POST_URL);
            postTitle = getArguments().getString(COLUMN_POST_TITLE);
            super.getViewModel().setPostUrl(postUrl);
            if (super.getViewModel().getPost().getValue() == null) {
                super.getViewModel().load(0);
            }
        }
    }

    @Override
    public void whenDataChange(ThreadAdapter adapter, KThread kThread) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(postTitle);
    }

    @Override
    public View onCreatedView(View v) {
        v.setPadding(-8,16,16,0);
        return v;
    }
}
