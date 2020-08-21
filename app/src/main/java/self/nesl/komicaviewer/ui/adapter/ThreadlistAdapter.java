package self.nesl.komicaviewer.ui.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.jsoup.nodes.Element;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Utils.print;

public class ThreadlistAdapter extends PostlistAdapter {
    public ThreadlistAdapter(Fragment fragment, ItemOnClickListener callBack) {
        super(fragment, callBack);
    }

    @NonNull
    @Override
    public PostlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thread, parent, false);
        return new PostlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostlistViewHolder holder, final int i) {
        final Post post = postlist.get(i);
        super.setDetail(holder, post);

        holder.txtPostInd.setText(post.getIntroduction(23, null));

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
