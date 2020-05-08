package self.nesl.komicaviewer.model.komica;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import static self.nesl.komicaviewer.util.Util.print;

public class KomicaHost extends Host {
    @Override
    public String getHost() {
        return "komica.org";
    }

    @Override
    public String[] getSubHosts() {
        return new String[]{
                "komica.org",
                "vi.anacel.com",
                "acgspace.wsfun.com",
                "komica.dbfoxtw.me",
                "idolma.ster.tw",
                "komica.yucie.net",
                "kagaminerin.org",
                "p.komica.acg.club.tw",
                "2cat.org"
        };
    }

    @Override
    public void downloadBoardlist(OnResponse onResponse) {
        String url=super.getUrl()+"/bbsmenu.html";
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Document doc=Jsoup.parse(response);
                ArrayList<Post> arrayList=parseAllBoardlist(doc);
                setBoardlist(arrayList);
                onResponse.onResponse(arrayList);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    private ArrayList<Post> parseAllBoardlist(Document doc) {
        ArrayList<Post> boards=new ArrayList<Post>();
        for (Element ul : doc.select("ul")) {
            String ui_title = ul.getElementsByClass("category").text();
            for (Element li : ul.select("li")) {
                String li_title = li.text();
                String li_link = li.select("a").attr("href");
                if (li_link.contains("/index.")) {
                    li_link = li_link.substring(0, li_link.indexOf("/index."));
                }
                try {
                    String finalLi_link = li_link;
                    Post p=new Post(){
                        @Override
                        public String getUrl() {
                            return finalLi_link;
                        }

                        @Override
                        public String getIntroduction(int words, String[] rank) {
                            return null;
                        }
                    };
                    p.setTitle(li_title);
                    boards.add(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return boards;
    }
}
