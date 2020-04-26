package self.nesl.komicaviewer.ui.sora;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraBoard;

import static self.nesl.komicaviewer.util.Util.print;

public class SoraViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Post>> postlist=new MutableLiveData<ArrayList<Post>>();
    private String boardUrl;

    public void load(int page){
        String url=boardUrl;
        if (page != 0) {
            url += "/pixmicat.php?page_num="+page;
        }
        print(this.getClass().getName()+" AndroidNetworking: "+url);
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                postlist.setValue(new SoraBoard(Jsoup.parse(response), boardUrl).getReplies());
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public void setBoardUrl(String s){
        this.boardUrl=s;
    }

    public LiveData<ArrayList<Post>> getPostlist() {
        return postlist;
    }
}