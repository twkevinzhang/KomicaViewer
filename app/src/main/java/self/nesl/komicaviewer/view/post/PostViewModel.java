package self.nesl.komicaviewer.view.post;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.parser.DocToPostParser;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> post = new MutableLiveData<>();
    private Post mPost;

    public void scrapyKomicaPost(final Board board){
        AndroidNetworking.get(mPost.getLink())
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                post.postValue(new DocToPostParser().toPost(Jsoup.parse(response),board));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("PVM",anError.getErrorBody());
                Log.e("PVM",anError.getErrorDetail());
                Log.e("PVM", String.valueOf(anError.getErrorCode()));
            }
        });
    }

    public void loadKomicaPost(final Board board){
        scrapyKomicaPost(board);
    }

    public MutableLiveData<Post> getPost() {
        return post;
    }

    public void setPost(Post post){
        this.mPost=post;
    }
}