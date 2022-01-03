package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

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
        binder.txtPostInd.setText(data.getDesc(23));

        TextView txtReplyCount = itemView.findViewById(R.id.txtReplyCount);
        txtReplyCount.setText("回應:" + data.getReplyCount());

        List<String> urls=  data.getImageUrls();
        if(!urls.isEmpty()){
            ImageView imgView = itemView.findViewById(R.id.img);
            new ImageRender(imgView, urls.get(0), true).render();
        }
    }
}