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

import java.text.MessageFormat;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.ui.adapter.ThreadAdapter;

import static self.nesl.komicaviewer.util.Utils.print;

public abstract class BaseFragment extends Fragment {
    private BaseViewModel viewModel;
    private int maxPage = 0;
    private ThreadAdapter adapter;

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public ThreadAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ThreadAdapter adapter) {
        this.adapter = adapter;
    }

    private int page = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        final RecyclerView rcLst = v.findViewById(R.id.rcLst);
        final TextView txtMsg = v.findViewById(R.id.txtMsg);

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
        viewModel.getPost().observe(getViewLifecycleOwner(), new Observer<KThread>() {
            @Override
            public void onChanged(KThread thread) {
//                ArrayList<KThread> arr=thread.getReplies(false,0);
//                if(arr==null) arr=new ArrayList<KThread>();
                adapter.setPost(thread);
                whenDataChange(adapter, thread);
                adapter.notifyDataSetChanged();
                txtMsg.setText(MessageFormat.format(
                        "onChanged," +
                                "adapter.getItemCount:{0}," +
                                "arr.length:{1}",
                        adapter.getItemCount(), thread.getReplies().size())
                );
                cateSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // rcLst.addOnScrollListener
        rcLst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rcLst.setAdapter(adapter);
        rcLst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (cateSwipeRefreshLayout.isRefreshing()) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    if (page < maxPage) {
                        cateSwipeRefreshLayout.setRefreshing(true);
                        page += 1;
                        txtMsg.setText("載入中" + page);
                        viewModel.load(page);
                    } else {
                        txtMsg.setText("極限了~maxPage:" + maxPage + ",now: " + page);
                    }
                } else if (!recyclerView.canScrollVertically(-1)) {
                    txtMsg.setText("到頂了(不能向上滑動)");
                } else {
                    txtMsg.setText("滑動中");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
        v=onCreatedView(v);
        return v;
    }

    abstract public void whenDataChange(ThreadAdapter adapter, KThread thread);

    public View onCreatedView(View v) {
        return v;
    }
}
