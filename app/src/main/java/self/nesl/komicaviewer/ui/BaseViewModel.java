package self.nesl.komicaviewer.ui;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import self.nesl.komicaviewer.models.po.Post;
public abstract class BaseViewModel extends ViewModel {

    public MutableLiveData<Post> post=new MutableLiveData<Post>();
    abstract public void load(int page);
    public MutableLiveData<Post> getPost() { return post; }

    public void update(Post newPost){
        Post oldPost=post.getValue();
        if(oldPost!=null){
            ArrayList<Post> arr=oldPost.getReplies(false);
            for (Post subPost:newPost.getReplies(false)) if(!arr.contains(subPost)) arr.add(subPost);
            newPost.setAllPost(arr);
        }

        post.postValue(newPost);
    }
}
