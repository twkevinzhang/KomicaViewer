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

public class KomicaTop50Host extends Host {
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
        String url=super.getUrl()+("/mainmenu2019.html");
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Document doc=Jsoup.parse(response);
                ArrayList<Post> arrayList=parseTop50Boardlist(doc);
                setBoardlist(arrayList);
                onResponse.onResponse(arrayList);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
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
