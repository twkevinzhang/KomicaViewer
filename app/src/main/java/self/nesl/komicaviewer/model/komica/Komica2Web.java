package self.nesl.komicaviewer.model.komica;

public class Komica2Web extends KWeb {
    public Komica2Web() {
        super("komica2", "http://komica2.net/");
    }

    @Override
    public String getMenuUrl(){
        return super.getDomainUrl()+ "bbsmenu2018.html";
    }

    @Override
    public String getTop50BoardUrl(){
        return super.getDomainUrl() + "mainmenu2018.html";
    }
}
