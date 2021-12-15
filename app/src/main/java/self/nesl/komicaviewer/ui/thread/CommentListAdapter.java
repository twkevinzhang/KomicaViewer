package self.nesl.komicaviewer.ui.thread;

import static self.nesl.komicaviewer.ui.thread.QuoteDialog.COLUMN_POST;
import static self.nesl.komicaviewer.ui.thread.QuoteDialog.COLUMN_POST_LIST;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.models.Layout;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.render.CommentRender;
import self.nesl.komicaviewer.ui.viewholder.CommentViewHolder;
import self.nesl.komicaviewer.ui.viewholder.SwitcherViewHolder;
import self.nesl.komicaviewer.ui.viewholder.ViewHolderBinder;

public class CommentListAdapter extends SampleAdapter<Post>{
    private CommentRender.OnReplyToClickListener OnReplyToClickListener;
    private CommentRender.OnLinkClickListener OnLinkClickListener;
    private CompoundButton.OnCheckedChangeListener onSwitchListener;
    private CommentRender.OnAllReplyClickListener OnAllReplyClickListener;
    private List<Post> all;

    public CommentListAdapter(boolean addSwitcher){
        all = super.getDataList();
        if(addSwitcher)
            addHeader(() -> R.layout.header_comment_list);
    }

    @NonNull
    @Override
    public ViewHolderBinder onCreateViewHolder(@NonNull ViewGroup parent, int layout) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        switch (layout){
            case R.layout.item_post:
                return new CommentViewHolder(view, OnReplyToClickListener, OnLinkClickListener, OnAllReplyClickListener, all);
            case R.layout.header_comment_list:
                return new SwitcherViewHolder(view, onSwitchListener);
            default:
                throw new IllegalStateException("Comment Layout Exception: " + layout);
        }
    }

    public static List<Post> toPostList(List<Layout> list){
        List<Layout> list1= list.stream().filter(layout1-> layout1 instanceof Post).collect(Collectors.toList());
        return list1.stream().map(layout1 -> (Post) layout1).collect(Collectors.toList());
    }

    public void setAll(List<Post> list){
        this.all = list;
    }

    public void setOnReplyToClickListener(CommentRender.OnReplyToClickListener OnReplyToClickListener){
        this.OnReplyToClickListener=OnReplyToClickListener;
    }

    public void setOnLinkClickListener(CommentRender.OnLinkClickListener OnLinkClickListener){
        this.OnLinkClickListener=OnLinkClickListener;
    }

    public void setOnSwitchListener(CompoundButton.OnCheckedChangeListener onSwitchListener){
        this.onSwitchListener=onSwitchListener;
    }

    public void setOnAllReplyClickListener(CommentRender.OnAllReplyClickListener OnAllReplyClickListener){
        this.OnAllReplyClickListener=OnAllReplyClickListener;
    }

    public static CommentRender.OnReplyToClickListener onReplyToClickListener(FragmentManager fragmentManager){
        return  (replyTo, all) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(COLUMN_POST,replyTo);
            bundle.putParcelableArrayList(COLUMN_POST_LIST, new ArrayList<>(all));
            QuoteDialog.newInstance(bundle).show(fragmentManager, "ReplyDialog2");
        };
    }

    public static CommentRender.OnLinkClickListener onLinkClickListener(Activity activity){
        return  (link) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            activity.startActivity(browserIntent);
        };
    }

    public static CommentRender.OnAllReplyClickListener onAllReplyClickListener(FragmentManager fragmentManager){
        return  (thread, all) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RepliesDialog.COLUMN_POST,thread);
            bundle.putParcelableArrayList(RepliesDialog.COLUMN_ALL, new ArrayList<>(all));
            RepliesDialog.newInstance(bundle).show(fragmentManager, "ReplyDialog3");
        };
    }
}