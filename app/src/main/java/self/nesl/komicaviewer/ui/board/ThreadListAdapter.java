package self.nesl.komicaviewer.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.viewholder.ThreadViewHolder;
import self.nesl.komicaviewer.ui.viewholder.ViewHolderBinder;

public class ThreadListAdapter extends SampleAdapter<Post> {
    @NonNull
    @Override
    public ViewHolderBinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(view);
    }
}
