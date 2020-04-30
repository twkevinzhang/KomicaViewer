package self.nesl.komicaviewer.ui.post;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.util.MyURL;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Util.getPostFormat;
import static self.nesl.komicaviewer.util.Util.print;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> post=new MutableLiveData<Post>();
    private String url;

    public void update() {
        print(this,"AndroidNetworking: "+url);
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Post post1=getPostFormat(Jsoup.parse(response),new MyURL(url).getUrlToLastPath());
                PostDB.addPost(post1, PostDB.HISTORY_TABLE_NAME);
                post.setValue(post1);
            }
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public void setPostUrl(String url){
        this.url=url;
    }

    public LiveData<Post> getPost() {
        return post;
    }
}