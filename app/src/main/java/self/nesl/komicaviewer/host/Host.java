package self.nesl.komicaviewer.host;

import java.io.Serializable;
import java.util.ArrayList;

import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.util.UrlUtils;
public abstract class Host implements Serializable {
    private int icon=0;
    private String datasetId;
    public Host(String datasetId) {
        this.datasetId=datasetId;
    }

    public String getUrl() {
        return new UrlUtils(getDatasetId()).getUrl();
    }

    public String getName(){
        return getDatasetId();
    }

    public String getDatasetId(){
        return datasetId;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public interface OnResponse {
        void onResponse(ArrayList<KThread> arrayList);
    }
}
