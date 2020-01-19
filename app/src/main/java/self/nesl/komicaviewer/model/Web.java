package self.nesl.komicaviewer.model;

public class Web {
    private String name;
    private String domainUrl;
    private String menuUrl;
    private String top50BoardUrl;
    private String allBoardPrefName ="komica_board_urls";
    private String top50BoardPrefName="komica_top50_board_urls";

    public Web(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public String getDomainUrl(){
        return domainUrl;
    }
    public String getMenuUrl(){
        return menuUrl;
    }
    public String getTop50BoardUrl(){
        return top50BoardUrl;
    }
    public String getAllBoardPrefName(){
        return allBoardPrefName;
    }
    public String getTop50BoardPrefName(){
        return top50BoardPrefName;
    }

    public Web setName(String name){
        this.name=name;
        return this;
    }
    public Web setDomainUrl(String domainUrl){
        this.domainUrl=domainUrl;
        return this;
    }
    public Web setMenuUrl(String menuUrl){
        this.menuUrl=menuUrl;
        return this;
    }
    public Web setTop50BoardUrl(String top50BoardUrl){
        this.top50BoardUrl=top50BoardUrl;
        return this;
    }
    public Web setAllBoardPrefName(String allBoardPrefName){
        this.allBoardPrefName=allBoardPrefName;
        return this;
    }
    public Web setTop50BoardPrefName(String top50BoardPrefName){
        this.top50BoardPrefName=top50BoardPrefName;
        return this;
    }
}
