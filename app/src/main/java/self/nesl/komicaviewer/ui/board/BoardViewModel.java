package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.ProjectUtils.getCurrentHost;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardViewModel extends BaseViewModel {
    public static final String COLUMN_PAGE = "page";
    private Post board;

    @Override
    public void load(int page){
        Post model=getCurrentHost().getPostModel(board.getUrl(),true);
        Bundle bundle=new Bundle();
        bundle.putInt(COLUMN_PAGE,page);
        model.download(bundle, new Post.OnResponse() {
            @Override
            public void onResponse(Post post) {
                BoardViewModel.super.insertPostlist(post.getReplies());
            }
        });
    }

    public void setBoard(Post board){
        this.board =board;
    }
}