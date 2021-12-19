package self.nesl.komicaviewer.repository;

import android.os.Bundle;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.factory.Factory;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.factory.ThreadListFactory;

public class ThreadListRepository implements Repository<List<Post>> {
    private Factory<List<Post>> factory;
    private Bundle bundle;

    public ThreadListRepository(Board Board, Bundle bundle){
        this.bundle=bundle;
        this.factory= new ThreadListFactory(Board);
    }

    @Override
    public void get(OnResponse<List<Post>> onResponse) {
        factory.createRequest(bundle).fetch(response -> {
            onResponse.onResponse(factory.parse(response));
        });
    }
}
