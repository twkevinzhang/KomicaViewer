package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtil.getPostModel;
import static self.nesl.komicaviewer.util.Util.print;

public class BoardViewModel extends ViewModel {
    public static final String COLUMN_PAGE = "page";

    private MutableLiveData<ArrayList<Post>> postlist=new MutableLiveData<ArrayList<Post>>();
    private String boardUrl;

    public void load(int page){
        Post model=getPostModel(boardUrl,true);
        if(model!=null){
            Bundle bundle=new Bundle();
            bundle.putInt(COLUMN_PAGE,page);
            model.download(bundle, new Post.OnResponse() {
                @Override
                public void onResponse(Post post) {
                    postlist.setValue(post.getReplies());
                }
            });

        }
    }

    public void setBoardUrl(String s){
        this.boardUrl =s;
    }

    public LiveData<ArrayList<Post>> getPostlist() {
        return postlist;
    }
}