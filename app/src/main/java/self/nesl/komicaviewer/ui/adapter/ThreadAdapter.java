package self.nesl.komicaviewer.ui.adapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.dto.KThread;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder> {
    public KThread thread;
    public ItemOnClickListener callBack;
    public Fragment fragment;

    public ThreadAdapter(Fragment fragment, ItemOnClickListener callBack) {
        this.thread =  new KThread(null,null);
        this.callBack = callBack;
        this.fragment=fragment;
    }

    public class ThreadViewHolder extends RecyclerView.ViewHolder{
        public TextView txtPostId;
        public TextView txtPostInd;
        public TextView txtReplyCount;
        public TextView txtPoster;
        public TextView txtTime;
        public WebView webView;
        public RecyclerView threads;
        public ImageView img;

        ThreadViewHolder(View v) {
            super(v);
            txtTime = v.findViewById(R.id.txtTime);
            txtReplyCount = v.findViewById(R.id.txtReplyCount);
            txtPostId = v.findViewById(R.id.txtId);
            txtPoster = v.findViewById(R.id.txtPoster);
            txtPostInd = v.findViewById(R.id.txtPostInd);
            webView=v.findViewById(R.id.webView);
            img=v.findViewById(R.id.img);
            threads=v.findViewById(R.id.threads);
//            itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        }

    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, final int i) {
        KThread post = thread.getReplies().get(i);
        setDetail(holder,post);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // todo: 讓圖片大小可以遵守dialog大小
//        int width=holder.itemView.getWidth();
//        print(this,"width",width+"");

        Element quoteEle=post.getQuote();
        for(Element imgEle: quoteEle.select("img")){
            imgEle.attr("width", (width/3)+"");
        }
        holder.webView.setInitialScale(250);
        holder.webView.loadDataWithBaseURL("",quoteEle.html(),"text/html","UTF-8","");

        ThreadAdapter adapter=new ThreadAdapter(fragment,new ThreadAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(KThread thread) {
            }
        });
        holder.threads.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
        adapter.setPost(post);
        holder.threads.setAdapter(adapter);

    }

    void setDetail(ThreadViewHolder holder, KThread post){
        holder.txtPostId.setText("No." + post.getPostId());
        holder.txtReplyCount.setText("回應:" + post.getReplyCount());
        holder.txtPoster.setText(post.getPoster());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        holder.txtTime.setText(sdf.format(post.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.itemOnClick(post);
            }
        });
    }



    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return thread.getReplies().size();
    }

    public void setPost(KThread thread) {
        this.thread=thread;
    }

    public void clear() {
        thread.removeAll();
    }

    public interface ItemOnClickListener {
        public void itemOnClick(KThread post);
    }
}