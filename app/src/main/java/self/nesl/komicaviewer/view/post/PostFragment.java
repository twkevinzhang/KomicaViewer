package self.nesl.komicaviewer.view.post;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.File;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.PostForm;

public class PostFragment extends Fragment {
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public PostFragment() {
        // Required empty public constructor
    }

    private Board board;
    private String masterPostId;

    public static PostFragment newInstance(Bundle args) {
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            board = (Board) getArguments().getSerializable("board");
            masterPostId = getArguments().getString("masterPostId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_post, container, false);
        Toolbar toolbar=v.findViewById(R.id.post_toolbar);
        final TextView txtPostTitle=v.findViewById(R.id.txtPostTitle);
        final TextView txtPostInd=v.findViewById(R.id.txtPostInd);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_post:
                        post(txtPostTitle.getText().toString(),txtPostInd.getText().toString(),null);
                        break;
                }
                return false;
            }
        });

        return v;
    }

    public void post(String title, String ind, File pic) {
        String postUrl=board.getLink()+"/pixmicat.php";
        String refererUrl;

        if(masterPostId==null){
            refererUrl=postUrl;
        }else{
            refererUrl=postUrl+"?res="+masterPostId;
        }

        // PostForm
        PostForm c=new PostForm();
        c. mode="regist";
        c. MAX_FILE_SIZE="5242880";
        c. sub="DO NOT FIX THIS";
        c. name="spammer";
        c. com="EID OG SMAPS";
        c. email="foo@foo.bar";
        if(pic==null){
            c. upfile="(binary)";
            c. noimg="on";
        }
        if(masterPostId!=null) c.resto=masterPostId;

        final String finalUrl = refererUrl;
        AndroidNetworking.post(postUrl)
                .addBodyParameter(board.getPostTitleSecret(), title)
                .addBodyParameter(board.getPostIndSecret(), ind)
                .addBodyParameter(c)
                .addHeaders("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeaders("cache-control","max-age=0")
                .addHeaders("content-length","1419")
                .addHeaders("referer",board.getLink())
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // do anything with response
                        Toast.makeText(getActivity(),"OK!",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e("PF", finalUrl);
                        Log.e("PF",error.getErrorCode()+"");
                        Log.e("PF",error.getErrorBody());
                        Log.e("PF",error.getErrorDetail());
                        error.printStackTrace();
                    }
                });
    }

}
