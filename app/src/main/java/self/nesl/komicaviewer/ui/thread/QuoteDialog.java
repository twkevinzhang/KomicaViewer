package self.nesl.komicaviewer.ui.thread;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.viewholder.CommentViewHolder;

public class QuoteDialog extends DialogFragment {
    public static final String COLUMN_POST="post";
    public static final String COLUMN_POST_LIST="postlist";
    private Post post;
    private List<Post> list;

    public static QuoteDialog newInstance(Bundle bundle) {
        QuoteDialog dialog = new QuoteDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post)getArguments().getSerializable(COLUMN_POST);
            list = getArguments().getParcelableArrayList(COLUMN_POST_LIST);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        // safety check // https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height#answer-20364233
        if (getDialog() == null) return;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.item_comment, container);
        CommentViewHolder binder= new CommentViewHolder(
                v,
                CommentListAdapter.onReplyToClickListener(getChildFragmentManager()),
                CommentListAdapter.onLinkClickListener(getActivity()),
                CommentListAdapter.onAllReplyClickListener(getChildFragmentManager()),
                CommentListAdapter.onImageClickListener(v.getContext()),
                list
        );
        binder.bind(post);

        return v;
    }
}