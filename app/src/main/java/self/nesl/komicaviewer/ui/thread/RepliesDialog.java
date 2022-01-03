package self.nesl.komicaviewer.ui.thread;
import static self.nesl.komicaviewer.util.ProjectUtils.filterRepliesList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.gallery.Poster;

public class RepliesDialog extends DialogFragment {
    public static final String COLUMN_POST="post";
    public static final String COLUMN_ALL ="replies";
    private List<Post> all;
    private Post post;
    private RecyclerView rcLst;
    private CommentListAdapter adapter;

    public static RepliesDialog newInstance(Bundle bundle) {
        RepliesDialog dialog = new RepliesDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            all = getArguments().getParcelableArrayList(COLUMN_ALL);
            post = (Post)getArguments().getSerializable(COLUMN_POST);
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
        View v = inflater.inflate(R.layout.layout_list, container);
        rcLst = v.findViewById(R.id.rcLst);
        initAdapter();
        return v;
    }

    private void initAdapter(){
        adapter= new CommentListAdapter();
        String threadId = post.getId();
        List<Post> replies= filterRepliesList(threadId, all);
        adapter.setAllComments(all);
        adapter.addAll(replies);
        adapter.setOnImageClickListener(CommentListAdapter.onImageClickListener(getContext(), Poster.toPosterList(replies)));
        adapter.setOnReplyToClickListener(CommentListAdapter.onReplyToClickListener(getChildFragmentManager()));
        adapter.setOnAllReplyClickListener(CommentListAdapter.onAllReplyClickListener(getChildFragmentManager()));
        adapter.setOnLinkClickListener(CommentListAdapter.onLinkClickListener(getActivity()));
        rcLst.setAdapter(adapter);
    }
}