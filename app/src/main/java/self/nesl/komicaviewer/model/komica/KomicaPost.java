package self.nesl.komicaviewer.model.komica;

import org.jsoup.nodes.Element;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;

public interface KomicaPost {
    void addPost(Element reply_ele);
    void installPreview(Post parent, String target_id);
    void installTitle(String title);
}
