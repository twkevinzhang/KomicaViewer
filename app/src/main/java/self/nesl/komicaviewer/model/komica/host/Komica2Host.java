package self.nesl.komicaviewer.model.komica.host;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.cyber.CyberBoard;
import self.nesl.komicaviewer.model.komica.cyber.CyberPost;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.util.UrlUtils;

public class Komica2Host extends Host{

    @Override
    public String getHost() {
        return "komica2.net";
    }

    @Override
    public Map[] getSubHosts() {
        return new Map[]{
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica2.net");
                    put(Host.MAP_POST_MODEL_COLUMN, new SoraPost());
                    put(Host.MAP_BOARD_MODEL_COLUMN, new SoraBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "2cat.org");
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "p.komica.acg.club.tw");
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "cyber.boguspix.com");
                    put(Host.MAP_POST_MODEL_COLUMN, new CyberPost());
                    put(Host.MAP_BOARD_MODEL_COLUMN, new CyberBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "majeur.zawarudo.org");
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
        };
    }

    @Override
    public void downloadBoardlist(OnResponse onResponse) {
//        String url=super.getUrl()+"/bbsmenu2018.html";
         String url=super.getUrl()+"/mainmenu2018.html"; // top50
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Document doc= Jsoup.parse(response);
//                ArrayList<Post> arrayList=KomicaHost.parseAllBoardlist(doc);
                ArrayList<Post> arrayList=new KomicaTop50Host().parseTop50Boardlist(doc); // top50
                setBoardlist(arrayList);
                onResponse.onResponse(arrayList);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
