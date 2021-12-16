package self.nesl.komicaviewer.ui.render;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.thread.CommentListAdapter;
import self.nesl.komicaviewer.ui.viewbinder.LinkPreviewBinder;

public class ImageRender implements Render {
    private View root;
    private String url;
    private boolean centerCrop = false;

    public ImageRender(Context context, String url){
        this.root =new ImageView(context);
        this.url=url;
    }

    public ImageRender(ImageView imageView, String url){
        this(imageView, url, false);
    }

    public ImageRender(ImageView imageView, String url, boolean centerCrop){
        this.root = imageView;
        this.url=url;
        this.centerCrop=centerCrop;
    }

    public View render(){
        if (url != null) {
            if(centerCrop){
                Glide.with(root.getContext())
                        .load(url)
                        .placeholder(R.drawable.bg_background)
                        .centerCrop()
                        .error(R.drawable.ic_error_404)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) root);
            }else{
                Glide.with(root.getContext())
                        .load(url)
                        .placeholder(R.drawable.bg_background)
                        .error(R.drawable.ic_error_404)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) root);
            }

            root.setVisibility(View.VISIBLE);
        } else {
            Glide.with(root.getContext()).clear(root);
            root.setVisibility(View.GONE);
        }
        return root;
    }
}

