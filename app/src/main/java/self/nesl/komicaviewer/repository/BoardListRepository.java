package self.nesl.komicaviewer.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import self.nesl.komicaviewer.factory.Factory;
import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.factory.BoardListFactory;

public class BoardListRepository implements Repository<List<Board>> {
    private Factory<List<Board>> factory;

    public BoardListRepository(Category category){
        this.factory= new BoardListFactory(category);
    }

    @Override
    public LiveData<List<Board>> get() {
        MutableLiveData<List<Board>> liveData = new MutableLiveData<>();
        factory.createRequest(null).fetch(response -> {
            liveData.postValue(factory.parse(response));
        });
        return liveData;
    }
}
