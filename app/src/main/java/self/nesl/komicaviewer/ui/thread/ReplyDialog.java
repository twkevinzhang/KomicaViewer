package self.nesl.komicaviewer.ui.thread;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.render.PostRender;
import self.nesl.komicaviewer.ui.viewholder.PostViewHolder;

public class ReplyDialog extends DialogFragment {
    public static final String COLUMN_POST="post";
    public static final String COLUMN_POST_LIST="postlist";
    private Post post;
    private List<Post> list;

    public static ReplyDialog newInstance(Bundle bundle) {
        ReplyDialog dialog = new ReplyDialog();
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
        View v = inflater.inflate(R.layout.item_post, container);
        PostViewHolder binder= new PostViewHolder(v,getChildFragmentManager());
        binder.bind(post, list);

        return v;
    }
}