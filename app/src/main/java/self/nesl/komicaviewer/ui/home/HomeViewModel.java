package self.nesl.komicaviewer.ui.home;
import java.util.ArrayList;

import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.host.Host;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class HomeViewModel extends BaseViewModel {
    private Host host;

    public void setHost(Host host) {
        this.host=host;
    }

    @Override
    public KThread beforePost(KThread kThread) {
        return null;
    }

    @Override
    public void load(int page) {
        KThread set=BoardPreferences.getHosts();
        if(set==null)return;
        HomeViewModel.super.update(set.getThread(host.getDatasetId()));
    }
}