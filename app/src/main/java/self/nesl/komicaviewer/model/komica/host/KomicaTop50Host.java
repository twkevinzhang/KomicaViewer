package self.nesl.komicaviewer.model.komica.host;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Map;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.ProjectUtils.getPostModel;
import static self.nesl.komicaviewer.util.Utils.print;

public class KomicaTop50Host extends KomicaHost {
    @Override
    public String getName() {
        return "komica.org";
    }

    @Override
    public void downloadBoardlist(OnResponse onResponse) {
        String url=super.getUrl()+("/mainmenu2019.html");
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Document doc=Jsoup.parse(response);
                ArrayList<Post> arrayList=parseTop50Boardlist(doc,new KomicaTop50Host());
                setBoardlist(arrayList);
                onResponse.onResponse(arrayList);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public static ArrayList<Post> parseTop50Boardlist(Document doc,Host host) {
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
            Post p=getPostModel(url,true,true);
            p.setTitle(title);
            p.setUrl(url);
//            p.addTag(ui_title);
            boards.add(p);
        }
        return boards;
    }


}
