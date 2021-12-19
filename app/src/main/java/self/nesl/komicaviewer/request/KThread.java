package self.nesl.komicaviewer.request;

import java.util.List;

import self.nesl.komicaviewer.models.Post;

public class KThread {
    private Post headPost;
    private List<Post> comments;
    public KThread(Post headPost, List<Post> comments){
        this.headPost=headPost;
        this.comments=comments;
    }

    public Post getHeadPost() {
        return headPost;
    }

    public List<Post> getComments() {
        return comments;
    }
}
