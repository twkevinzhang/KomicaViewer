package self.nesl.komicaviewer.ui.gallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public String getMediaUrl() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Poster other = (Poster) obj;
        if (Objects.equals(this.url, other.url)) {
            return true;
        }
        return false;
    }

    public static List<Poster> toPosterList(List<Post> list){
        List<Poster> posterList = new ArrayList<>();
        for (Post post:list) {
            for (String url:post.getImageUrls()) {
                posterList.add(new Poster(url, post));
            }
        }
        return posterList;
    }
}
