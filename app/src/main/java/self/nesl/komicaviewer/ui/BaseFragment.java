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

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Utils.print;

public abstract class BaseFragment extends Fragment {
    private BaseViewModel viewModel;
    private int page = 0;
    private boolean canLoad;
    private PostlistAdapter.ItemOnClickListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView lst = v.findViewById(R.id.rcLst);
        final TextView txtMsg=v.findViewById(R.id.txtMsg);
        PostlistAdapter adapter=new PostlistAdapter(this,listener);

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
        viewModel.getPost().observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                assert post != null;
                whenDataChange(adapter,post);
                adapter.notifyDataSetChanged();
                txtMsg.setText("onChanged,getItemCount: " + adapter.getItemCount());
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);
        lst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!canLoad)return;
                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (cateSwipeRefreshLayout.isRefreshing()) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    cateSwipeRefreshLayout.setRefreshing(true);
                    page += 1;
                    txtMsg.setText("載入中" + page);
                    viewModel.load(page);

                } else if (!recyclerView.canScrollVertically(-1)) {
                    txtMsg.setText("到頂了(不能向上滑動)");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        return doSomething(v);
    }

    public void init(BaseViewModel viewModel,boolean sort,boolean canLoad,PostlistAdapter.ItemOnClickListener listener){
        this.viewModel=viewModel;
        this.canLoad =canLoad;
        this.listener=listener;
    }

    abstract public void whenDataChange(PostlistAdapter adapter,Post post);
    abstract public View doSomething(View v);
}
