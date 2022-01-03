package self.nesl.komicaviewer.ui;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import self.nesl.komicaviewer.feature.Title;

public interface PagingViewModel<T> {
    public static final String PAGE = "page";

    abstract public int getCurrentPage();

    abstract public LiveData<List<T>> children();

    abstract public LiveData<Boolean> loading();

    abstract public void refreshChildren();

    abstract public void nextChildren();

    abstract public LiveData<String> error();

}
