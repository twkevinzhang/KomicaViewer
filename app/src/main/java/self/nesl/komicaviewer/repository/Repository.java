package self.nesl.komicaviewer.repository;

import java.util.List;

import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.request.OnResponse;

public interface Repository<T> {
    public void get(OnResponse<T> onResponse);
}
