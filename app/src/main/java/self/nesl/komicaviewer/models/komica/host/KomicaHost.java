package self.nesl.komicaviewer.models.komica.host;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.models.Host;
import self.nesl.komicaviewer.models.Request;
import self.nesl.komicaviewer.models.komica._2cat._2catBoardRequest;
import self.nesl.komicaviewer.models.komica._2cat._2catThreadRequest;
import self.nesl.komicaviewer.models.po.Post;
import self.nesl.komicaviewer.models.komica.sora.SoraBoardRequest;
import self.nesl.komicaviewer.models.komica.sora.SoraPostParser;
import self.nesl.komicaviewer.models.komica.sora.SoraThreadRequest;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.ProjectUtils.urlParse;
import static self.nesl.komicaviewer.util.Utils.print;

public class KomicaHost extends Host {

    public KomicaHost(){
        setIcon( R.drawable.ic_menu_slideshow);
    }

    @Override
    public String getHost() {
        return "komica.org";
    }

    @Override
    public Map[] getRequests() {
        return new Map[]{
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.org"); // 綜合、新番捏他、動畫
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "2cat.org"); // 碧藍幻想
                    put(Host.MAP_BOARD_MODEL_COLUMN, _2catBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, _2catThreadRequest.class);
                }}
                ,
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.org"); // 綜合、新番捏他、動畫
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "vi.anacel.com"); // Figure/GK
                    put(Host.MAP_BOARD_MODEL_COLUMN,null);
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "acgspace.wsfun.com"); // 艦隊收藏
                    put(Host.MAP_BOARD_MODEL_COLUMN,null);
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.dbfoxtw.me"); // 人外
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "anzuchang.com"); // Idolmaster
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "komica.yucie.net"); // 格鬥遊戲
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "kagaminerin.org"); // 3D STG
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "p.komica.acg.club.tw");
                    put(Host.MAP_BOARD_MODEL_COLUMN,null);
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "2cat.org"); // 碧藍幻想
                    put(Host.MAP_BOARD_MODEL_COLUMN,_2catBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, _2catThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "mymoe.moe"); // 綜合2
                    put(Host.MAP_BOARD_MODEL_COLUMN,null);
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "strange-komica.com"); // 魔物獵人
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "secilia.zawarudo.org");
                    put(Host.MAP_BOARD_MODEL_COLUMN,null);
                    put(Host.MAP_POST_MODEL_COLUMN, null);
                }},
                new HashMap<String, Object>(){{
                    put(Host.MAP_HOST_COLUMN, "gzone-anime.info"); // TYPE-MOON
                    put(Host.MAP_BOARD_MODEL_COLUMN, SoraBoardRequest.class);
                    put(Host.MAP_POST_MODEL_COLUMN, SoraThreadRequest.class);
                }},
                // add komica model item in there
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
                    Post p=new Post(li_link,li_link);
                    p.setTitle(li_title);
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
