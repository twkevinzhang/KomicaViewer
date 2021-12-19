package self.nesl.komicaviewer.repository;

import static self.nesl.komicaviewer.util.ProjectUtils.find;

import android.os.Bundle;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.request.Request;
import self.nesl.komicaviewer.request.ThreadListRequestFactory;

public class ThreadListRepository implements Repository<List<Post>> {
    private Request<List<Post>> request;

    public ThreadListRepository(Board Board, Bundle bundle){
        this.request= new ThreadListRequestFactory(Board).createRequest(bundle);
    }

    @Override
    public void get(OnResponse<List<Post>> onResponse) {
        request.fetch(onResponse);
    }
}
