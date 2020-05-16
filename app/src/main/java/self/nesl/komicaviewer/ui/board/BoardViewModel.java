package self.nesl.komicaviewer.ui.board;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.ProjectUtils.getPostModel;
import static self.nesl.komicaviewer.util.Utils.print;

public class BoardViewModel extends BaseViewModel {
    public static final String COLUMN_PAGE = "page";
    private String boardUrl;

    @Override
    public void load(int page){
        Post model=getPostModel(boardUrl,true);
        if(model!=null){
            Bundle bundle=new Bundle();
            bundle.putInt(COLUMN_PAGE,page);
            model.download(bundle, new Post.OnResponse() {
                @Override
                public void onResponse(Post post) {
                    getPost().postValue(post);
                }
            });

        }
    }

    public void setBoardUrl(String s){
        this.boardUrl =s;
    }
}