        package self.nesl.komicaviewer.ui;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import java.util.ArrayList;

        import self.nesl.komicaviewer.R;
        import self.nesl.komicaviewer.model.Post;
        import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;

        import static self.nesl.komicaviewer.util.Utils.print;

public abstract class BaseFragment extends Fragment {
    private BaseViewModel viewModel;
    private int page = 0;
    private int maxPage = 0;
    private PostlistAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg=v.findViewById(R.id.txtMsg);

        // SwipeRefreshLayout
        final SwipeRefreshLayout cateSwipeRefreshLayout = v.findViewById(R.id.refresh_layout);
        cateSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                viewModel.load(0);
                adapter.notifyDataSetChanged();
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // data and adapter
        viewModel.getList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> arr) {
                assert arr != null;
                adapter.setPostlist(arr);
                whenDataChange(adapter,arr);
                adapter.notifyDataSetChanged();
                txtMsg.setText("onChanged,getItemCount: " + adapter.getItemCount());
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // lst.addOnScrollListener
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);
        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (cateSwipeRefreshLayout.isRefreshing()) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    if(page<maxPage){
                        cateSwipeRefreshLayout.setRefreshing(true);
                        page += 1;
                        txtMsg.setText("載入中" + page);
                        viewModel.load(page);
                    }else{
                        txtMsg.setText("極限了~maxPage:" +maxPage+",now: "+page);
                    }
                } else if (!recyclerView.canScrollVertically(-1)) {
                    txtMsg.setText("到頂了(不能向上滑動)");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        return v;
    }

    public void init(BaseViewModel viewModel,int maxPage,PostlistAdapter adapter){
        this.viewModel=viewModel;
        this.maxPage=maxPage;
        this.adapter=adapter;
    }

    abstract public void whenDataChange(PostlistAdapter adapter,ArrayList<Post> arr);

}
