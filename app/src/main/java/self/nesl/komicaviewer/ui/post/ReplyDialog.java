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
import self.nesl.komicaviewer.adapter.PostlistAdapter;
import self.nesl.komicaviewer.model.Post;

public class ReplyDialog extends DialogFragment {
    private Post post;
    public static final String COLUMN_POST="post";

    public ReplyDialog(Post post, FragmentManager fragmentManager){
        this.post=post;
        if(post.getReplyCount()!=0){
            this.show(fragmentManager, post.getPostId()+"dialog");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post)getArguments().getSerializable(COLUMN_POST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_reply, container);
        final RecyclerView lst = v.findViewById(R.id.rcLst);

        final PostlistAdapter adapter = new PostlistAdapter(this,new PostlistAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Post post) {
                new ReplyDialog(post,getFragmentManager());
            }
        });

        // lst
        lst.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lst.setAdapter(adapter);
        adapter.setPostlist(post.getReplies());

        // width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT+20;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT+20;
        getDialog().getWindow().setAttributes(lp);

        return v;
    }
}