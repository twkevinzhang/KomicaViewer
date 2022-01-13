package self.nesl.komicaviewer.ui;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface IViewModel {
    abstract public LiveData<Boolean> loading();

    abstract public LiveData<String> error();

}
