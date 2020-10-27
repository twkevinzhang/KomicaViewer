package self.nesl.komicaviewer.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.dto.KThread;

public class ThreadlistAdapter extends ThreadAdapter {
    public ThreadlistAdapter(Fragment fragment, ItemOnClickListener callBack) {
        super(fragment, callBack);
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, final int i) {
        final KThread post = postlist.get(i);
        super.setDetail(holder, post);

        holder.txtPostInd.setText(post.getDescription(23));

        String url = post.getPictureUrl();
        holder.img.setTag(R.id.imageid, post.getPostId());
        if (url != null && url.length() > 0
                && holder.img.getTag(R.id.imageid).equals(post.getPostId())
        ) {
            Glide.with(holder.img.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .error(R.drawable.ic_error_404)
                    .into(holder.img);
        } else {
            Glide.with(holder.img.getContext()).clear(holder.img);
            holder.img.setImageDrawable(fragment.getResources().getDrawable(R.drawable.ic_launcher_background));
        }
    }

}
