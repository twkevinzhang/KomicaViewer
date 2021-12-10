package self.nesl.komicaviewer.ui.thread;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewholder.PostViewHolder;

public class PostListAdapter extends SampleAdapter<Post, PostViewHolder> {
    private FragmentManager fragmentManager;

    public PostListAdapter(FragmentManager fragmentManager){
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view, fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int i) {
        super.onBindViewHolder(holder, i);
        final Post post = list.get(i);
        holder.bind(post, list);
    }
}