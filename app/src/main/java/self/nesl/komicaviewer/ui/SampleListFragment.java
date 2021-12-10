package self.nesl.komicaviewer.ui;

import static self.nesl.komicaviewer.ui.SampleViewModel.PAGE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.MessageFormat;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.feature.Title;

public abstract class SampleListFragment<DETAIL extends Title, CHILDREN> extends Fragment {
    protected SwipeRefreshLayout refresh;
    protected RecyclerView rvLst;
    protected TextView txtMsg;
    protected int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            getViewModel().setArgs(getArguments());
    }

    @Override
    final public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        rvLst = v.findViewById(R.id.rcLst);
        txtMsg = v.findViewById(R.id.txtMsg);
        refresh = v.findViewById(R.id.refresh_layout);
        initRefresh();
        initObserver();
        initAdapter();
        loadDetail();
        loadPage(0);
        return v;
    }

    protected void initRefresh() {
        refresh.setOnRefreshListener(() -> {
            clearPage();
            loadPage(0);
        });
    }

    protected void initObserver() {
        getViewModel().children().observe(getViewLifecycleOwner(), list -> {
            getAdapter().addAll(list);
            txtMsg.setText(MessageFormat.format(
                    "onChanged," +
                            "adapter.getItemCount:{0}," +
                            "arr.length:{1}",
                    getAdapter().getItemCount(), list.size())
            );
            refresh.setRefreshing(false);
        });

        getViewModel().detail().observe(getViewLifecycleOwner(), detail -> {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(detail.getTitle());
        });
    }

    protected void initAdapter() {
        rvLst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                super.onScrollStateChanged(rv, newState);

                if (!rv.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    if (refresh.isRefreshing()) {
                        txtMsg.setText("急三小");
                        return;
                    }
                    if (page < getMaxPage()) {
                        page += 1;
                        loadPage(page);
                    } else {
                        txtMsg.setText("極限了~maxPage:" + getMaxPage() + ",now: " + page);
                    }
                } else if (!rv.canScrollVertically(-1)) {
                    txtMsg.setText("到頂了(不能向上滑動)");
                }
            }

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
            }
        });
        rvLst.setAdapter(getAdapter());
    }

    protected void loadDetail(){
        getViewModel().loadDetail(null);
    }

    protected void loadPage(int page) {
        this.page = page;
        if (page > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(PAGE, page);
            getViewModel().loadChildren(bundle);
        } else {
            getViewModel().loadChildren(null);
        }
        refresh.setRefreshing(true);
        txtMsg.setText("載入中..." + page);
    }

    protected void clearPage() {
        page = -1;
        getAdapter().clear();
        refresh.setRefreshing(false);
    }

    protected abstract SampleViewModel<DETAIL, CHILDREN> getViewModel();

    protected abstract SampleAdapter<CHILDREN, ? extends RecyclerView.ViewHolder> getAdapter();

    protected int getMaxPage() {
        return 99;
    }
}
