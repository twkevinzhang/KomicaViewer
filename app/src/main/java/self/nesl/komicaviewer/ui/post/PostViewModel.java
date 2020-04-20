package self.nesl.komicaviewer.ui.post;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

import static self.nesl.komicaviewer.util.util.getParseNameByUrl;
import static self.nesl.komicaviewer.util.util.print;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> post=new MutableLiveData<Post>();
    private String url;
    private String format;

    public void update() {
        post = new MutableLiveData<>();
        print(this.getClass().getName()+" AndroidNetworking: "+url);
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                if(format.equals(SoraPost.class.getName())){
                    Element thread=Jsoup.parse(response).body().selectFirst("div.thread");
                    Element threadpost = thread.selectFirst("div.threadpost");
                    SoraPost subPost = new SoraPost(threadpost.attr("id").substring(1), threadpost);
                    for (Element reply_ele : thread.select("div.reply")) {
                        subPost.addPost(reply_ele);
                    }
                    post.setValue(subPost);
                }
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public void setPostUrl(String url){
        this.url=url;
    }

    public void setFormat(String format){
        this.format=format;
    }

    public LiveData<Post> getPost() {
        return post;
    }
}