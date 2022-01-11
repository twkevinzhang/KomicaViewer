package self.nesl.komicaviewer.ui.thread;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.Layout;
import self.nesl.komicaviewer.ui.SampleAdapter;
import self.nesl.komicaviewer.ui.gallery.GalleryViewHolder;
import self.nesl.komicaviewer.ui.gallery.Poster;
import self.nesl.komicaviewer.ui.render.ReplyRender;
import self.nesl.komicaviewer.ui.render.ImageRender;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewholder.ReplyViewHolder;
import self.nesl.komicaviewer.ui.viewholder.SwitcherViewHolder;
import self.nesl.komicaviewer.ui.viewholder.ViewHolderBinder;
import self.nesl.komicaviewer.ui.views.PosterOverlayView;

public class ReplyListAdapter extends SampleAdapter<Post>{
    private ReplyRender.OnReplyToClickListener OnReplyToClickListener;
    private PostRender.OnLinkClickListener OnLinkClickListener;
    private CompoundButton.OnCheckedChangeListener onSwitchListener;
    private ReplyRender.OnAllReplyClickListener OnAllReplyClickListener;
    private PostRender.OnImageClickListener OnImageClickListener;
    private List<Post> allReplies;

    public void addSwitcher(){
        addHeader(() -> R.layout.header_reply_list);
    }

    @NonNull
    @Override
    public ViewHolderBinder onCreateViewHolder(@NonNull ViewGroup parent, int layout) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        switch (layout){
            case R.layout.item_post:
                return new ReplyViewHolder(view, OnReplyToClickListener, OnLinkClickListener, OnAllReplyClickListener, OnImageClickListener, allReplies);
            case R.layout.header_reply_list:
                return new SwitcherViewHolder(view, onSwitchListener);
            default:
                throw new IllegalStateException("Reply Layout Exception: " + layout);
        }
    }

    public static List<Post> toPostList(List<Layout> list){
        List<Layout> list1= list.stream().filter(layout1-> layout1 instanceof Post).collect(Collectors.toList());
        return list1.stream().map(layout1 -> (Post) layout1).collect(Collectors.toList());
    }

    public void setAllReplies(List<Post> list){
        this.allReplies = list;
    }

    public void setOnReplyToClickListener(ReplyRender.OnReplyToClickListener OnReplyToClickListener){
        this.OnReplyToClickListener=OnReplyToClickListener;
    }

    public void setOnLinkClickListener(PostRender.OnLinkClickListener OnLinkClickListener){
        this.OnLinkClickListener=OnLinkClickListener;
    }

    public void setOnSwitchListener(CompoundButton.OnCheckedChangeListener onSwitchListener){
        this.onSwitchListener=onSwitchListener;
    }

    public void setOnAllReplyClickListener(ReplyRender.OnAllReplyClickListener OnAllReplyClickListener){
        this.OnAllReplyClickListener=OnAllReplyClickListener;
    }

    public void setOnImageClickListener(PostRender.OnImageClickListener OnImageClickListener){
        this.OnImageClickListener=OnImageClickListener;
    }

    public static ReplyRender.OnReplyToClickListener onReplyToClickListener(FragmentManager fragmentManager){
        return  (replyTo, all) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(QuoteDialog.COLUMN_POST,replyTo);
            bundle.putParcelableArrayList(QuoteDialog.COLUMN_POST_LIST, new ArrayList<>(all));
            QuoteDialog.newInstance(bundle).show(fragmentManager, "ReplyDialog2");
        };
    }

    public static ReplyRender.OnLinkClickListener onLinkClickListener(Activity activity){
        return  (link) -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                activity.startActivity(browserIntent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(activity.getApplicationContext(), "連結錯誤", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public static ReplyRender.OnAllReplyClickListener onAllReplyClickListener(FragmentManager fragmentManager){
        return  (thread, all) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RepliesDialog.COLUMN_POST,thread);
            bundle.putParcelableArrayList(RepliesDialog.COLUMN_ALL, new ArrayList<>(all));
            RepliesDialog.newInstance(bundle).show(fragmentManager, "ReplyDialog3");
        };
    }

    public static PostRender.OnImageClickListener onImageClickListener(Context context, List<Poster> list){
        return (v, replyPosterList, startPosition) -> {
            List<Poster> list2 = list;
            if(list2 == null){
                list2 = Collections.emptyList();
            }
            Poster shower = replyPosterList.get(startPosition);
            int index= list2.indexOf(shower);
            if(index < 0){
                list2 = replyPosterList;
                index = startPosition;
            }

            List<Poster> list3 = list2;
            PosterOverlayView posterOverlayView =  new PosterOverlayView(context);
            update(posterOverlayView, list3, index);
            new StfalconImageViewer.Builder<>(context, list3,
                    ReplyListAdapter::loadImage,
                    GalleryViewHolder::buildViewHolder)
                    .withStartPosition(index)
                    .withTransitionFrom((ImageView) v)
                    .withHiddenStatusBar(true)
                    .withImageChangeListener(position -> {
                        update(posterOverlayView, list3, position);
                    })
                    .withOverlayView(posterOverlayView)
                    .show();
        };
    }

    private static void loadImage(ImageView imageView, Poster imageInfo){
        new ImageRender(imageView, imageInfo.getMediaUrl()).render();
    }

    private static void update(PosterOverlayView posterOverlayView, List<Poster> imageInfoList, int position){
        posterOverlayView.update(imageInfoList.get(position));
    }
}