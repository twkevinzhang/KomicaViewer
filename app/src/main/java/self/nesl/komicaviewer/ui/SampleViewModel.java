package self.nesl.komicaviewer.ui;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import self.nesl.komicaviewer.feature.Title;

public abstract class SampleViewModel<DETAIL extends Title, CHILDREN> extends ViewModel {
    public static final String PAGE = "page";

    abstract public void setArgs(Bundle bundle);

    abstract public int getCurrentPage();

    abstract public LiveData<List<CHILDREN>> children();

    abstract public LiveData<DETAIL> detail();

    abstract public LiveData<Boolean> loading();

    abstract public void clearChildren();

    abstract public void loadChildren();

    abstract public void loadDetail(Bundle bundle);

}
