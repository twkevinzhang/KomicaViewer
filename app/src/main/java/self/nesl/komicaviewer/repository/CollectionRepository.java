package self.nesl.komicaviewer.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.models.Post;

public class CollectionRepository implements Repository<List<Post>> {
    private final PostDao dao;

    public CollectionRepository(final PostDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Post>> get() {
        return dao.loadAll();
    }
}
