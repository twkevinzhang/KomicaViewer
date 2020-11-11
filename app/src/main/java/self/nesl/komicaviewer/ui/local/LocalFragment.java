package self.nesl.komicaviewer.ui.local;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.navigation.Navigation;
import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.ui.BaseFragment;
import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.ui.adapter.ThreadlistAdapter;
import self.nesl.komicaviewer.ui.post.PostFragment;
public class LocalFragment extends BaseFragment {

    private LocalViewModel mViewModel;
    public static final String COLUMN_TABLE_NAME = "tableName"; // binding nav_history,nav_favorite, mobile_navigation.xml

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LocalViewModel.class);
        if (getArguments() != null) {
            mViewModel.setTable(getArguments().getString(COLUMN_TABLE_NAME));
            mViewModel.load(0);
        }

        super.init(mViewModel, 99, new ThreadlistAdapter(this,new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putString(PostFragment.COLUMN_POST_URL, post.getUrl());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_local_to_nav_post, bundle);
            }
        }));
    }

    @Override
    public void whenDataChange(PostlistAdapter adapter, Post post) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("localhost");
    }

}
