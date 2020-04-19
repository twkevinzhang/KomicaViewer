package self.nesl.komicaviewer.model.komica;

public class KomicaWeb extends KWeb {
    public KomicaWeb() {
        super("komica", "https://www.komica.org/");
    }

    @Override
    public String getMenuUrl(){
        return super.getDomainUrl()+ "bbsmenu.html";
    }

    @Override
    public String getTop50BoardUrl(){
        return super.getDomainUrl() + "mainmenu2018.html";
    }
}
