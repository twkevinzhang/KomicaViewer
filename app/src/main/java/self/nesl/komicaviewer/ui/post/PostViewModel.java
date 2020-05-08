package self.nesl.komicaviewer.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.util.UrlUtil;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtil.getPostModel;
import static self.nesl.komicaviewer.util.Util.print;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> post=new MutableLiveData<Post>();
    private String url;

    public void update() {
        print(new Object(){}.getClass(),"AndroidNetworking: "+url);
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Post post1= getPostModel(Jsoup.parse(response),new UrlUtil(url).getLastPathSegment(),false);
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