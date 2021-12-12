package self.nesl.komicaviewer.ui.viewholder;

import android.app.Activity;
import android.view.View;

import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class HeadPostViewHolder extends ViewHolderBinder {
    private Activity activity;

    public HeadPostViewHolder(View v, Activity activity) {
        super(v);
        this.activity=activity;
    }

    public void bind(Layout layout){
        PostViewBinder binder = new PostViewBinder(itemView, (Post) layout);
        binder.setActivity(activity);
        binder.render();
    }
}