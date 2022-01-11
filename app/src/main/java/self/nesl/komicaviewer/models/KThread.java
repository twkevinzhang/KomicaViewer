package self.nesl.komicaviewer.models;

import java.util.List;

import self.nesl.komicaviewer.models.Post;

public class KThread {
    private Post headPost;
    private List<Post> replies;
    public KThread(Post headPost, List<Post> replies){
        this.headPost=headPost;
        this.replies = replies;
    }

    public Post getHeadPost() {
        return headPost;
    }

    public List<Post> getReplies() {
        return replies;
    }
}
