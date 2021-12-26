package self.nesl.komicaviewer.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import self.nesl.komicaviewer.db.dao.PostDao;
import self.nesl.komicaviewer.models.Post;

public class HistoryRepository implements RepositoryAsLive<List<Post>>{
    private final PostDao dao;

    public HistoryRepository(final PostDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Post>> get() {
        return dao.loadAll();
    }

    public LiveData<List<Post>> getLiked() {
        return dao.loadAllLiked();
    }
}
