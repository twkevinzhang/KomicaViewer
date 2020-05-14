package self.nesl.komicaviewer.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.util.UrlUtil;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtil.getPostModel;
import static self.nesl.komicaviewer.util.Util.print;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Post> post = new MutableLiveData<Post>();
    private String url;

    public void update() {
        Post model = getPostModel(new UrlUtil(url).getLastPathSegment(), false);

        if (model != null) {
            print(model.getClass(),"");
            model.setUrl(url);
            model.download(null, new Post.OnResponse() {
                @Override
                public void onResponse(Post post1) {
                    PostDB.addPost(post1, PostDB.TABLE_HISTORY);
                    post.setValue(post1);
                }
            });
        }
    }

    public void setPostUrl(String url) {
        this.url = url;
    }

    public LiveData<Post> getPost() {
        return post;
    }
}