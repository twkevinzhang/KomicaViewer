package self.nesl.komicaviewer.model;

import java.io.Serializable;

public abstract class Web implements Serializable {
    private String name;
    private String domainUrl;

    public Web(String name, String domainUrl){
        this.name=name;
        this.domainUrl=domainUrl;
    }

    public String getName(){
        return name;
    }

    public String getDomainUrl(){
        return domainUrl;
    }
}
