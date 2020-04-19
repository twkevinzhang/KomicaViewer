package self.nesl.komicaviewer.ui.sora;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.komica.SoraPost;

public class PostViewModel extends ViewModel {
    private MutableLiveData<SoraPost> post;
    private String url="https://sora.komica.org/00/index.htm";

    public PostViewModel() {
        post = new MutableLiveData<>();
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Element thread=Jsoup.parse(response).body().selectFirst("div.thread");
                Element threadpost = thread.selectFirst("div.threadpost");
                SoraPost subPost = new SoraPost(threadpost.attr("id").substring(1), threadpost);
                for (Element reply_ele : thread.select("div.reply")) {
                    subPost.addPost(reply_ele);
                }
                post.setValue(subPost);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public void setUrl(String url){
        this.url=url;
    }

    public LiveData<SoraPost> getPost() {
        return post;
    }
}