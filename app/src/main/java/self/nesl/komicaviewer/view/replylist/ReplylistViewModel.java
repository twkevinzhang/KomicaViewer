package self.nesl.komicaviewer.view.replylist;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.parser.DocToReplylistParser;

public class ReplylistViewModel extends ViewModel {
    private MutableLiveData<Post> post = new MutableLiveData<>();
    private Post mPost;

    public void scrapyKomicaPost(final Board board){
        AndroidNetworking.get(mPost.getLink())
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                Post p=new DocToReplylistParser(Jsoup.parse(response),board).toPost();
                Log.e("RlVM",p.getReplyAll().size()+"");
                post.postValue(new DocToReplylistParser(Jsoup.parse(response),board).toPost());
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

    public void loadReplylist(final Board board){
        scrapyKomicaPost(board);
    }

    public MutableLiveData<Post> getPost() {
        return post;
    }

    public void setPost(Post post){
        this.mPost=post;
    }
}