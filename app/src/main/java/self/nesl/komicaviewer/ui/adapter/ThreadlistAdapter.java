package self.nesl.komicaviewer.ui.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

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

        holder.txtPostInd.setText(post.getIntroduction(23,null));

        String url=post.getPictureUrl();
        if(url!=null){
            Glide.with(holder.img)
                    .load(post.getPictureUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .error(R.drawable.ic_menu_camera)
                    .into(holder.img);
        }
    }

}
