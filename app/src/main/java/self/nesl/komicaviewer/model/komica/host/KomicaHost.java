package self.nesl.komicaviewer.model.komica.host;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.mymoe.MymoeBoard;
import self.nesl.komicaviewer.model.komica.mymoe.MymoePost;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.model.komica.twocat.TwocatBoard;

import static self.nesl.komicaviewer.util.Utils.print;

public class KomicaHost extends Host {

    @Override
    public String getHost() {
        return "komica.org";
    }

    @Override
    public Map[] getSubHosts() {
        return new Map[]{
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.org");
                    put(Host.MAP_BOARD_MODEL_COLUMN, new SoraBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "vi.anacel.com");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "acgspace.wsfun.com ");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.dbfoxtw.me");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "anzuchang.com");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.yucie.net");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "kagaminerin.org");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "p.komica.acg.club.tw");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "2cat.org");
                    put(Host.MAP_BOARD_MODEL_COLUMN, new TwocatBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "mymoe.moe");
                    put(Host.MAP_BOARD_MODEL_COLUMN, new MymoeBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "strange-komica.com");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "secilia.zawarudo.org");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "gzone-anime.info");
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
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

    public ArrayList<Post> parseAllBoardlist(Document doc) {
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
                    Post p=getPostModel(li_link,true,true);
                    p.setTitle(li_title);
                    p.setUrl(li_link);
                    p.addTag(ui_title);
                    boards.add(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return boards;
    }
}
