package self.nesl.komicaviewer.model.komica;

import self.nesl.komicaviewer.model.Host;

public class Komica2Host extends Host {

    @Override
    public String getHost() {
        return "komica2.net";
    }

    @Override
    public String[] getSubHosts() {
        return new String[]{
                "komica2.net",
                "2cat.org",
                "p.komica.acg.club.tw",
                "cyber.boguspix.com",
                "majeur.zawarudo.org",
        };
    }

    @Override
    public void downloadBoardlist(OnResponse onResponse) {

    }

    public String getTop50Menu() {
        return super.getUrl()+"/mainmenu2018.html";
    }
}
