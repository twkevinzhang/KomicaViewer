package self.nesl.komicaviewer.repository;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.factory.Factory;
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
    public LiveData<List<Post>> get() {
        Request req= factory.createRequest(bundle);
        MutableLiveData<List<Post>> liveData = new MutableLiveData<>();
        if(req != null){
            req.fetch(response -> {
                liveData.postValue(factory.parse(response));
            });
        }else{
            Log.e("ThreadListRepo", "Host request not impl.");
            liveData.postValue(null);
        }
        return liveData;
    }
}
