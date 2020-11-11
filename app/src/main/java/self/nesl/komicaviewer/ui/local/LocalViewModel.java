package self.nesl.komicaviewer.ui.local;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class LocalViewModel extends BaseViewModel {
    private String table;

    public void setTable(String table) {
       this.table=table;
    }

    @Override
    public void load(int page) {
        Post oldPost=post.getValue();
        if(oldPost==null) oldPost=new Post(null,table);
        ArrayList<Post> arr=PostDB.getAllPost(table);
        Collections.reverse(arr);
        oldPost.setAllPost(arr);
        post.setValue(oldPost);
    }
}
