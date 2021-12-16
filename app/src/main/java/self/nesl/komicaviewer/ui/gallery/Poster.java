package self.nesl.komicaviewer.ui.gallery;

import self.nesl.komicaviewer.models.Post;

public class Poster {
    private String url;
    private Post post;

    public Poster(String url, Post post){
        this.url= url;
        this.post= post;
    }

    public Post getPost() {
        return post;
    }

    public String getUrl() {
        return url;
    }
}
