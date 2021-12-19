package self.nesl.komicaviewer.repository;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.factory.BoardListFactory;
import self.nesl.komicaviewer.request.OnResponse;

public class BoardListRepository implements Repository<List<Board>> {
    private BoardListFactory factory;

    public BoardListRepository(Category category){
        this.factory= new BoardListFactory(category);
    }

    @Override
    public void get(OnResponse<List<Board>> onResponse) {
        factory.createRequest(null).fetch(response -> {
            onResponse.onResponse(factory.parse(response));
        });
    }
}
