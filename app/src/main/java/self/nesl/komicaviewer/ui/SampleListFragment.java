package self.nesl.komicaviewer.ui;

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

public abstract class SampleListFragment<DETAIL extends Title, CHILDREN extends Layout> extends PagingListFragment<CHILDREN> {
    protected void initObserver() {
        super.initObserver();
        getViewModel().detail().observe(getViewLifecycleOwner(), detail -> {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(detail.getTitle());
        });
    }
    protected abstract SampleViewModel<DETAIL, CHILDREN> getViewModel();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDetail();
    }

    protected void loadDetail(){
        getViewModel().loadDetail(null);
    }
}
