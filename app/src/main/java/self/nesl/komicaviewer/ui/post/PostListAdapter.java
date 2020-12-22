package self.nesl.komicaviewer.ui.post;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.viewholder.PostViewHolder;

public class PostListAdapter extends SampleAdapter<Post, PostViewHolder> {
    public DisplayMetrics displayMetrics;

    public PostListAdapter(DisplayMetrics displayMetrics) {
        this.displayMetrics = displayMetrics;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int i) {
        super.onBindViewHolder(holder, i);
        final Post post = list.get(i);

        setDetail(holder, post);


////        DisplayMetrics displayMetrics = new DisplayMetrics();
////        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//
//        // todo: 讓圖片大小可以遵守dialog大小
////        int width=holder.itemView.getWidth();
////        print(this,"width",width+"");
//
//        Element quoteEle = post.getShow();
//        for (Element imgEle : quoteEle.select("img")) {
//            imgEle.attr("width", (width / 3) + "");
//        }
//        holder.webView.setInitialScale(250);
//        holder.webView.loadDataWithBaseURL("", quoteEle.html(), "text/html", "UTF-8", "");
    }

    private void setDetail(PostViewHolder holder, Post data) {
        holder.txtPostId.setText("No." + data.getId());
        holder.txtReplyCount.setText("回應:" + data.getReplyCount());
        holder.txtPoster.setText(data.getPoster());
        holder.txtTime.setText(data.getTimeStr());
    }

    public void addThreadpost(Post post) {
        if (!list.contains(post)) list.add(0, post);
        notifyDataSetChanged();
    }
}