package self.nesl.komicaviewer.repository;

import androidx.lifecycle.LiveData;

import self.nesl.komicaviewer.request.OnResponse;

public interface RepositoryAsLive<T> {
    public LiveData<T> get();
}
