package self.nesl.komicaviewer.ui.post;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.ui.BaseViewModel;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtils;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtils.getCurrentHost;
import static self.nesl.komicaviewer.util.Utils.print;

public class PostViewModel extends BaseViewModel {
    private Post post;

    @Override
    public void load(int page) {
        Post model = getCurrentHost().getPostModel(new UrlUtils(post.getUrl()).getLastPathSegment(), false);
        model.setUrl(post.getUrl());
        model.download(null, new Post.OnResponse() {
            @Override
            public void onResponse(Post post1) {
                PostDB.addPost(post1, PostDB.TABLE_HISTORY);
                PostViewModel.super.insertPostlist(post1.getReplies());
            }
        });
    }

    public void setPost(Post post) {
        this.post = post;
    }
}