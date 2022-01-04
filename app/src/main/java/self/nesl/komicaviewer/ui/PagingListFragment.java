package self.nesl.komicaviewer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.MessageFormat;

import self.nesl.komicaviewer.R;

public abstract class PagingListFragment<CHILDREN extends Layout> extends Fragment {
    protected View root;
    protected SwipeRefreshLayout refresh;
    protected RecyclerView rvLst;
    protected TextView txtMsg;

    @Override
    final public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_refresh_list, container, false);
        rvLst = v.findViewById(R.id.rcLst);
        txtMsg = v.findViewById(R.id.txtMsg);
        refresh = v.findViewById(R.id.refresh_layout);
        root =v;
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRefresh();
        initObserver();
        initAdapter();
        if(getViewModel().children().getValue() == null || getViewModel().children().getValue().isEmpty()){
            loadPage();
        }
    }

    protected void initRefresh() {
        refresh.setOnRefreshListener(this::refresh);
    }

    protected void refresh(){
        getAdapter().clear();
        getViewModel().refreshChildren();
    }

    protected void initObserver() {
        getViewModel().children().observe(getViewLifecycleOwner(), list -> {
            if(list != null){
                getAdapter().addAll(list);
                txtMsg.setText(MessageFormat.format(
                        "onChanged," +
                                "adapter.getItemCount:{0}," +
                                "arr.length:{1}",
                        getAdapter().getItemCount(), list.size())
                );
            }else{
                txtMsg.setText("Loading Error!");
            }
        });

        getViewModel().loading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading)
                txtMsg.setText("載入中..." + getViewModel().getCurrentPage());
            refresh.setRefreshing(isLoading);
        });

        getViewModel().error().observe(getViewLifecycleOwner(), error -> {
            if(error != null)
                txtMsg.setText(error);
            refresh.setRefreshing(false);
        });
    }

    protected void initAdapter() {
        rvLst.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                super.onScrollStateChanged(rv, newState);
                if (!rv.canScrollVertically(1)) {
                    // 如果不能向下滑動，到底了
                    loadPage();
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

    protected void loadPage() {
        getViewModel().nextChildren();
    }

    protected abstract PagingViewModel<CHILDREN> getViewModel();

    protected abstract SampleAdapter<CHILDREN> getAdapter();
}
