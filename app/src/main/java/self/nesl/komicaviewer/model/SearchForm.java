package self.nesl.komicaviewer.model;

import java.io.Serializable;

public class SearchForm implements Serializable {
    private String mode;
    private String keyword;
    private String field;
    private String method;

    public SearchForm(String mode, String keyword, String field, String method){
        this.mode=mode;
        this.keyword=keyword;
        this.field=field;
        this.method=method;
    }

    public String getMode(){
        return this.mode;
    }

    public String getKeyword(){
        return this.keyword;
    }

    public String getField(){
        return this.field;
    }

    public String getMethod(){
        return this.method;
    }
}
