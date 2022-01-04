package self.nesl.komicaviewer.ui;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import self.nesl.komicaviewer.feature.Title;

public abstract class SampleViewModel<DETAIL extends Title, CHILDREN> extends AndroidViewModel implements PagingViewModel<CHILDREN> {
    public SampleViewModel(@NonNull Application application) {
        super(application);
    }

    abstract public LiveData<DETAIL> detail();

    abstract public void loadDetail(Bundle bundle);
}
