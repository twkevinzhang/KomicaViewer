package self.nesl.komicaviewer.model.komica;

import java.io.Serializable;

import self.nesl.komicaviewer.model.Web;

public abstract class KWeb extends Web implements Serializable {
    public KWeb(String name, String domainUrl){
        super(name, domainUrl);
    }

    abstract public  String getMenuUrl();
    abstract public  String getTop50BoardUrl();

    public String getAllBoardPrefName(){
        return super.getName()+"_board_urls";
    }
    public String getTop50BoardPrefName(){
        return super.getName()+"_top50_board_urls";
    }
}
