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

import static self.nesl.komicaviewer.Const.KOMICA_IS_ALL;
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
        boolean isAll=KOMICA_IS_ALL;
        String url=super.getUrl()+(isAll?"/bbsmenu.html":"/mainmenu2019.html");
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Document doc=Jsoup.parse(response);
                ArrayList<Post> arrayList=isAll?parseAllBoardlist(doc): parseTop50Boardlist(doc);
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

    private ArrayList<Post> parseTop50Boardlist(Document doc) {
        ArrayList<Post> boards = new ArrayList<Post>();
        for (Element e : doc.getElementsByClass("divTableRow").select("a")) {
            String url = e.attr("href");
            if (url.contains("/index.")) {
                url = url.substring(0, url.indexOf("/index."));
            }
            if (url.substring(url.length() - 1).equals("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String title = e.text();
            String finalUrl = url;
            Post p=new Post(){
                @Override
                public String getUrl() {
                    return finalUrl;
                }

                @Override
                public String getIntroduction(int words, String[] rank) {
                    return null;
                }
            };
            p.setTitle(title);
            boards.add(p);
        }
        return boards;
    }


}
