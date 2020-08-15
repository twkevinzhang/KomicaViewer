package self.nesl.komicaviewer.ui.post;

import self.nesl.komicaviewer.db.PostDB;
import self.nesl.komicaviewer.ui.BaseViewModel;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtils.getCurrentHost;
import static self.nesl.komicaviewer.util.Utils.print;

public class PostViewModel extends BaseViewModel {
    private Post post;

    @Override
    public void load(int page) {
        Post model = getCurrentHost().getPostModel(post.getBoardUrl(), false);
        model.download( new Post.OnResponse() {
            @Override
            public void onResponse(Post post1) {
                PostDB.addPost(post1, PostDB.TABLE_HISTORY);
                PostViewModel.super.insertPostlist(post1.getReplies());
            }
        },0,post.getBoardUrl(),post.getPostId() );
    }

    public void setPost(Post post) {
        this.post = post;
    }
}