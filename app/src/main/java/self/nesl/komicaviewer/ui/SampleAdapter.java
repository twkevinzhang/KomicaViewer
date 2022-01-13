package self.nesl.komicaviewer.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import self.nesl.komicaviewer.ui.viewholder.ViewHolderBinder;
import self.nesl.komicaviewer.util.Utils;

public abstract class SampleAdapter<DATA extends Layout> extends RecyclerView.Adapter<ViewHolderBinder> {
    private List<Layout> headers = new ArrayList<>();
    private List<DATA> list = new ArrayList<>();
    private OnClickListener<DATA> callBack;

    @Override
    public int getItemViewType(int position) {
        return getAll().get(position).layout();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBinder holder, final int position) {
        int dataStart = headers.size();
        if(position >= dataStart){
            if(callBack!= null) {
                DATA data = list.get(position - dataStart);
                holder.itemView.setOnClickListener(v -> callBack.OnClick(holder.itemView, data));
            }
        }
        holder.bind(getAll().get(position));
    }

    public List<Layout> getAll(){
        return Utils.concat(headers, (List<Layout>) list);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return headers.size()+ list.size();
    }

    public void add(DATA item) {
        if(!this.list.contains(item)){
            this.list.add(item);
            notifyItemInserted(getItemCount());
        }
    }

    public void addAll(List<DATA> list) {
        if(list.isEmpty()) return;
        LinkedHashSet<DATA> set=  new LinkedHashSet<>(list);
        set.removeAll(this.list);
        this.list.addAll(set);
        notifyItemRangeInserted(getItemCount() - set.size(),  set.size());
    }

    public void addHeader(Layout layout) {
        this.headers.add(layout);
        notifyItemInserted(0);
    }

    public void clear() {
        int dataSize = list.size();
        list.clear();
        notifyItemRangeRemoved(this.headers.size(), dataSize);
    }

    public void setOnClickListener(OnClickListener<DATA> callBack) {
        this.callBack = callBack;
    }

    public List<DATA> getDataList(){
        return list;
    }

    public interface OnClickListener<T> {
        void OnClick(View view, T item);
    }
}