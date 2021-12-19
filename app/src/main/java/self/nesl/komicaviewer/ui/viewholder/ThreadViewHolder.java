package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.ui.Layout;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.ImageRender;
import self.nesl.komicaviewer.ui.viewbinder.PostViewBinder;

public class ThreadViewHolder extends ViewHolderBinder {
    public ThreadViewHolder(View v) {
        super(v);
    }

    public void bind(Layout layout){
        Post data = (Post) layout;
        PostViewBinder binder= new PostViewBinder(itemView, data, null);
        binder.txtPostId.setText("No." + data.getId());
        binder.txtPoster.setText(data.getPoster());
        binder.txtTime.setText(data.getTimeStr());
        binder.txtPostInd.setText(data.getDescription(23));

        TextView txtReplyCount = itemView.findViewById(R.id.txtReplyCount);
        txtReplyCount.setText("回應:" + data.getReplyCount());

        ImageView imgView = itemView.findViewById(R.id.img);
        String url = data.getPictureUrl();
        new ImageRender(imgView, url, true).render();
    }
}