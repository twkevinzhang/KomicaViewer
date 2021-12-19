package self.nesl.komicaviewer.ui.thread;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.viewholder.HeadPostViewHolder;
import self.nesl.komicaviewer.ui.viewholder.ViewHolderBinder;

public class HeadPostAdapter extends SampleAdapter<Post> {
    private Activity activity;

    public HeadPostAdapter(Activity activity){
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolderBinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new HeadPostViewHolder(view, activity);
    }
}