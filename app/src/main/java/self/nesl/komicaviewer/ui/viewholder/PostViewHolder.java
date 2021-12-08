package self.nesl.komicaviewer.ui.viewholder;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import self.nesl.komicaviewer.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView txtPostId;
    public TextView txtPostInd;
    public TextView txtReplyCount;
    public TextView txtPoster;
    public TextView txtTime;
    public TextView txtPost;
    public ImageView img;

    public PostViewHolder(View v) {
        super(v);
        txtTime = v.findViewById(R.id.txtTime);
        txtReplyCount = v.findViewById(R.id.txtReplyCount);
        txtPostId = v.findViewById(R.id.txtId);
        txtPoster = v.findViewById(R.id.txtPoster);
        txtPostInd = v.findViewById(R.id.txtPostInd);
        txtPost = v.findViewById(R.id.txtPost);
        img = v.findViewById(R.id.img);
//            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }
}