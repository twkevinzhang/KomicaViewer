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
import self.nesl.komicaviewer.model.komica.internaltwocat.InTwocatBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;

import static self.nesl.komicaviewer.util.Utils.print;

public class Komica2Host extends Host{

    @Override
    public String getName() {
        return "komica2.net";
    }

    @Override
    public Map[] getSubHosts() {
        return new Map[]{
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica2.net"); // 二次裡Ａ
                    put(Host.MAP_BOARD_MODEL_COLUMN, new SoraBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "2cat.org"); // GIF裡
                    put(Host.MAP_BOARD_MODEL_COLUMN, new InTwocatBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "p.komica.acg.club.tw"); // 觸手裡
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "cyber.boguspix.com"); // 機娘裡
                    put(Host.MAP_BOARD_MODEL_COLUMN, new SoraBoard());
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN,  "majeur.zawarudo.org"); // 詢問裡
                    put(Host.MAP_BOARD_MODEL_COLUMN, null);
                }},
        };
    }

    @Override
    public void downloadBoardlist(OnResponse onResponse) {
         String url=super.getUrl()+"/mainmenu2018.html";
        AndroidNetworking.get(url)
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                Document doc= Jsoup.parse(response);
                ArrayList<Post> arrayList=KomicaTop50Host.parseTop50Boardlist(doc,new Komica2Host());
                setBoardlist(arrayList);
                onResponse.onResponse(arrayList);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
