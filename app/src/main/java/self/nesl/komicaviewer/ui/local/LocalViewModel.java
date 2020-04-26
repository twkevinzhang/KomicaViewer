package self.nesl.komicaviewer.ui.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jsoup.Jsoup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraBoard;

public class LocalViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> postlist=new MutableLiveData<ArrayList<Post>>();
    private String table;

    public void update() {
        postlist.setValue( PostDB.getAllPostLink(table));
    }

    public LiveData<ArrayList<Post>> getPostlist() {
        return postlist;
    }

    public void setTable(String table) {
       this.table=table;
    }
}
