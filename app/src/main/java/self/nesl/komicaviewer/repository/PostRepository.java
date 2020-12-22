package self.nesl.komicaviewer.repository;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.request.Request;

public class PostRepository implements Repository<Post> {
    private Request<List<Post>> request;

    public void setRequest(Request<List<Post>> request) {
        this.request = request;
    }

    @Override
    public void getAll(OnResponse<List<Post>> onResponse) {
        request.fetch(onResponse);
    }

    @Override
    public void get(String id, OnResponse<Post> onResponse) {
        getAll(posts->{
            onResponse.onResponse(posts.stream().filter(b -> b.getId().equals(id)).findFirst().get());
        });
    }
}
