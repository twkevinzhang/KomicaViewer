package self.nesl.komicaviewer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.adapter.BoardlistAdapter;
import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.host.KomicaTop50Host;
import static self.nesl.komicaviewer.util.Utils.print;

// nav_home
public class HomeFragment extends Fragment {
    public static final String COLUMN_HOST="host";

    private HomeViewModel homeViewModel;
    private Host host;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        host = getArguments() != null ? (Host) getArguments().getSerializable(COLUMN_HOST) : new KomicaTop50Host();
        homeViewModel.setHost(host);
        homeViewModel.update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg = v.findViewById(R.id.txtMsg);

        // title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(host.getHost());

        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        BoardlistAdapter adapter = new BoardlistAdapter(this);
        lst.setAdapter(adapter);

        // data and adapter
        homeViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> boards) {
                adapter.addAllPost(boards);
                adapter.notifyDataSetChanged();
            }
        });

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                homeViewModel.update();
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }
}
