package self.nesl.komicaviewer.ui;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import self.nesl.komicaviewer.feature.Title;
import self.nesl.komicaviewer.repository.Repository;

public abstract class SampleViewModel<DETAIL extends Title, CHILDREN> extends ViewModel implements PagingViewModel<CHILDREN> {
    abstract public LiveData<DETAIL> detail();

    abstract public void loadDetail(Bundle bundle);
}
