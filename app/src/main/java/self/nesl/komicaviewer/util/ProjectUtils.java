package self.nesl.komicaviewer.util;
import org.jsoup.nodes.Element;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import self.nesl.komicaviewer.models.Host;
import self.nesl.komicaviewer.models.Request;
import self.nesl.komicaviewer.models.komica.host.KomicaTop50Host;
import self.nesl.komicaviewer.models.komica.sora.SoraBoardRequest;

public class ProjectUtils {
    private static final String MAP_TITLE_COLUMN="title";
    private static final String MAP_LINK_COLUMN="link";

    public static Element installThreadTag(Element threads){
        //如果找不到thread標籤，就是2cat.komica.org，要用addThreadTag()改成標準綜合版樣式
        if (threads.selectFirst("div.thread") == null) {

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
                "yy/MM/dd(EEE)HH:mm",
                "yy/MM/dd HH:mm:ss" // mymoe
        )){
            try {
                return new SimpleDateFormat(s, Locale.ENGLISH).parse(time);
            }catch (ParseException ignored) {}
        }
        return null;
    }

    public static Host urlParse(String url){
        return new KomicaTop50Host();
    }
}
