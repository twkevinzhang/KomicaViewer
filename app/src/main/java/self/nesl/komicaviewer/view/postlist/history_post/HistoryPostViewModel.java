package self.nesl.komicaviewer.view.postlist.history_post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.db.StaticString;
import self.nesl.komicaviewer.model.Post;


// 取得資料 的 模組
public class HistoryPostViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Post>> postlist = new MutableLiveData<>();

    public void loadPostlist(int page){
        postlist.postValue(PostDB.getPostlist(StaticString.HISTORY_TABLE_NAME));
    }

    public LiveData<ArrayList<Post>> getPostlist() {
        return postlist;
    }
}
