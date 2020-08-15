package self.nesl.komicaviewer.ui.post;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.ui.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;

public class ReplyDialog extends DialogFragment {
    private Post post;
    public static final String COLUMN_POST="post";

    public static ReplyDialog newInstance(Post post) {
        ReplyDialog fragment = new ReplyDialog();
        Bundle args = new Bundle();
        args.putSerializable(COLUMN_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        View v = inflater.inflate(R.layout.fragment_reply, container);
        final RecyclerView lst = v.findViewById(R.id.rcLst);

        final PostlistAdapter adapter = new PostlistAdapter(this,new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                ReplyDialog dialog=ReplyDialog.newInstance(post);
                dialog.show(getFragmentManager(), post.getPostId() + "dialog");
            }
        });

        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);
        adapter.setPostlist(post.getReplies());

        return v;
    }
}