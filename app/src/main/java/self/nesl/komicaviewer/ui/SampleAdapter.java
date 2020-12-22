package self.nesl.komicaviewer.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SampleAdapter<DATA, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public List<DATA> list = new ArrayList<>();
    private OnClickListener<DATA> callBack;

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int i) {
        final DATA data = list.get(i);
        holder.itemView.setOnClickListener(v -> callBack.OnClick(holder.itemView, data));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<DATA> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener<DATA> callBack) {
        this.callBack = callBack;
    }

    public interface OnClickListener<T> {
        void OnClick(View view, T item);
    }
}