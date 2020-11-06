package self.nesl.komicaviewer.ui.home;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.models.Host;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.ui.BaseViewModel;
import self.nesl.komicaviewer.ui.board.BoardViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class HomeViewModel extends BaseViewModel {
    private MutableLiveData<ArrayList<Post>> list= new MutableLiveData<ArrayList<Post>>();
    private Host host;

    public void setHost(Host host) {
        this.host=host;
    }

    @Override
    public void load(int page) {
        ArrayList<Post> arrayList= BoardPreferences.getBoards(host);
        Post post=new Post(null,"home");
        if(arrayList==null || arrayList.size()==0){
            BoardPreferences.update(host, new Host.OnResponse() {
                @Override
                public void onResponse(ArrayList<Post> arrayList1) {
                    post.setAllPost(arrayList1);

                }
            });
        }else{
            post.setAllPost(arrayList);
        }
        HomeViewModel.super.update(post);
    }
}