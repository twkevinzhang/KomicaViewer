package self.nesl.komicaviewer.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.Komica2Host;
import self.nesl.komicaviewer.model.komica.KomicaHost;
import self.nesl.komicaviewer.model.komica.SoraBoard;
import self.nesl.komicaviewer.model.komica.SoraPost;

import static self.nesl.komicaviewer.util.Util.print;

public class ProjectUtil {
    private static final String MAP_TITLE_COLUMN="title";
    private static final String MAP_LINK_COLUMN="link";

    public static Post getPostModel(Document document, String url, boolean isBoard){
        for(Host host : new Host[]{
                new KomicaHost(),
                new Komica2Host(),
        }){
            if(new UrlUtil(url).getHost().contains(host.getHost())){
                return host.getPostModel(document,url,isBoard);
            }
        }
        return null;
    }

    public static Element installThreadTag(Element threads){
        //如果找不到thread標籤，就是2cat.komica.org，要用addThreadTag()改成標準綜合版樣式
        if (threads.selectFirst("div.thread") == null) {
            print(new Object(){}.getClass(),"thread is null,將thread加入threads中，變成標準綜合版樣式");
            //將thread加入threads中，變成標準綜合版樣式
            Element thread = threads.appendElement("div").addClass("thread");
            for (Element div : threads.children()) {
                thread.appendChild(div);
                if (div.tagName().equals("hr")) {
                    threads.appendChild(thread);
                    thread = threads.appendElement("div").addClass("thread");
                }
            }
        }
        return threads;
    }

    public static Date parseTime(String time){
        for(String s : Arrays.asList(
                "yyyy/MM/dd(EEE) HH:mm:ss.SSS",
                "yy/MM/dd(EEE) HH:mm:ss",
                "yy/MM/dd(EEE)HH:mm:ss",
                "yy/MM/dd(EEE)HH:mm"
        )){
            try {
                return new SimpleDateFormat(s, Locale.ENGLISH).parse(time);
            }catch (ParseException ignored) {}
        }
        return null;
    }

    public static ArrayList<Post> arrayMapToArrayPost(ArrayList<Map<String, String>> maps){
        if(maps==null)return null;
        ArrayList<Post> arrayList=new ArrayList<Post>();
        for(Map<String, String> map : maps){
            Post p=new Post(){
                @Override
                public String getUrl() {
                    return map.get(MAP_LINK_COLUMN);
                }

                @Override
                public String getIntroduction(int words, String[] rank) {
                    return null;
                }

                @Override
                public Post parseDoc(Document document, String url) {
                    return null;
                }
            };
            p.setTitle(map.get(MAP_TITLE_COLUMN));
            arrayList.add(p);
        }
        return arrayList;
    }

    public static ArrayList<Map<String, String>> arrayPostToArrayMap(ArrayList<Post> arrayList){
        if(arrayList==null || arrayList.size()==0)return null;
        ArrayList<Map<String, String>> maps=new ArrayList<>();
        for(Post p : arrayList){
            Map<String,String> myMap = new HashMap<String,String>();
            myMap.put(MAP_TITLE_COLUMN,p.getTitle(0));
            myMap.put(MAP_LINK_COLUMN,p.getUrl());
            maps.add(myMap);
        }
        return maps;
    }
}
