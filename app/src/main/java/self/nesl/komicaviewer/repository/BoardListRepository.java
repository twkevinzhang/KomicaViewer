package self.nesl.komicaviewer.repository;

import static self.nesl.komicaviewer.util.ProjectUtils.find;

import android.util.Log;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.request.BoardListRequestFactory;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.request.Request;

public class BoardListRepository implements Repository<List<Board>> {
    private Request<List<Board>> request;

    public BoardListRepository(Category category){
        this.request= new BoardListRequestFactory(category).createRequest(null);;
    }

    @Override
    public void get(OnResponse<List<Board>> onResponse) {
        request.fetch(onResponse);
    }
}
