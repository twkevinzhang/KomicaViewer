package self.nesl.komicaviewer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.model.Post;

public abstract class BaseViewModel extends ViewModel {

    public MutableLiveData<Post> post=new MutableLiveData<Post>();
    abstract public void load(int page);
    public MutableLiveData<Post> getPost() {
        return post;
    }

    public void insertPostlist(Post newPost){
        Post oldPost= getPost().getValue();
        if(oldPost!=null){
            oldPost.addAllPost(newPost.getReplies());
        }else{
            oldPost=newPost;
        }
        getPost().postValue(oldPost);
    }
}
