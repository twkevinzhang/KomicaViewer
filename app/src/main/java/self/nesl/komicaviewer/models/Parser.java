package self.nesl.komicaviewer.models;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.models.po.Post;

public abstract class Parser{
    public Post post;

    public Parser(Post post,Element origin) {
        this.post=post;
        this.post.setOrigin(origin);
        this.post.setShow(origin.clone());
    }
    public abstract Post parse();
}
