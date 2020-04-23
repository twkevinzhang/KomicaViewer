package self.nesl.komicaviewer.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

import static self.nesl.komicaviewer.util.util.print;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> post=new MutableLiveData<Post>();
    private String url;
    private Post format;

    public void update() {
        print(this.getClass().getName()+" AndroidNetworking: "+url);
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                post.setValue(format.parseDoc(Jsoup.parse(response),url));
            }
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public void setPostUrl(String url){
        this.url=url;
    }

    public void setFormat(Post format){
        this.format=format;
    }

    public LiveData<Post> getPost() {
        return post;
    }
}