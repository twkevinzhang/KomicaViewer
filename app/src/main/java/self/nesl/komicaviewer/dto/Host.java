package self.nesl.komicaviewer.dto;

import java.io.Serializable;
import java.util.Map;

import static self.nesl.komicaviewer.util.Utils.print;

public class Host implements Serializable {
    private String domain;
    private Map<String,Board[]> dataSets;
    public Host(String domain,Map<String,Board[]> dataSets) {
        this.domain=domain;
        this.dataSets = dataSets;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Map<String,Board[]> getDataSets() {
        return dataSets;
    }

    public void setDataSets(Map<String,Board[]> dataSets) {
        this.dataSets = dataSets;
    }

    public Board[] getDataSet(String setId) {
        return dataSets==null?null:dataSets.get(setId);
    }
}
