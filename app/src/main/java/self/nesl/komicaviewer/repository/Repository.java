package self.nesl.komicaviewer.repository;

import java.util.List;

import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.request.OnResponse;

public interface Repository<T extends Id> {
    public void getAll(OnResponse<List<T>> onResponse);
    public void get(String id, OnResponse<T> onResponse);
}
