package self.nesl.komicaviewer.ui.board;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtil.getPostModel;
import static self.nesl.komicaviewer.util.Util.print;

public class BoardViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Post>> postlist=new MutableLiveData<ArrayList<Post>>();
    private String boardUrl;

    public void load(int page){
        String pageUrl= boardUrl;
        if (page != 0) {
            pageUrl += "/pixmicat.php?page_num="+page;
        }
        print(this.getClass(),"AndroidNetworking: "+pageUrl);
        AndroidNetworking.get(pageUrl)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                postlist.setValue(getPostModel(Jsoup.parse(response), boardUrl,true).getReplies());
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public void setBoardUrl(String s){
        this.boardUrl =s;
    }

    public LiveData<ArrayList<Post>> getPostlist() {
        return postlist;
    }
}