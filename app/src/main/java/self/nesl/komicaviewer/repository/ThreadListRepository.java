package self.nesl.komicaviewer.repository;

import android.os.Bundle;
import android.util.Log;

import java.io.NotActiveException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.factory.Factory;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.factory.ThreadListFactory;
import self.nesl.komicaviewer.request.Request;

public class ThreadListRepository implements Repository<List<Post>> {
    private Factory<List<Post>> factory;
    private Bundle bundle;

    public ThreadListRepository(Board board, Bundle bundle){
        this.bundle=bundle;
        this.factory= new ThreadListFactory(board.getUrl());
    }

    @Override
    public void get(OnResponse<List<Post>> onResponse) {
        Request req= factory.createRequest(bundle);
        if(req != null){
            req.fetch(response -> {
                onResponse.onResponse(factory.parse(response));
            });
        }else{
            Log.e("ThreadListRepo", "Host request not impl.");
            onResponse.onResponse(Collections.emptyList());
        }
    }
}
