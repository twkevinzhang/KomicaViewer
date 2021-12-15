package self.nesl.komicaviewer.util;

import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import self.nesl.komicaviewer.feature.Id;
import self.nesl.komicaviewer.models.Post;

public class ProjectUtils {
    public static Element installThreadTag(Element threads) {
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

    public static Date parseTime(String time) {
        for (String s : Arrays.asList(
                "yyyy/MM/dd(EEE) HH:mm:ss.SSS",
                "yy/MM/dd(EEE) HH:mm:ss",
                "yy/MM/dd(EEE)HH:mm:ss",
                "yy/MM/dd(EEE)HH:mm",
                "yy/MM/dd HH:mm:ss" // mymoe
        )) {
            try {
                return new SimpleDateFormat(s, Locale.ENGLISH).parse(time);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    public static List<Post> filterRepliesList(String threadId, List<Post> list){
        return filterReplies(threadId, list).collect(Collectors.toList());
    }

    public static Stream<Post> filterReplies(String threadId, List<Post> list){
        return list.stream().filter(r->
                (threadId == null && r.getReplyTo() == null) ||
                (threadId != null && threadId.equals(r.getReplyTo()))
        );
    }

    public static <T extends Id> List<T> filterWithList(String id, List<T> list){
        return filter(id, list).collect(Collectors.toList());
    }

    public static <T extends Id> Stream<T> filter(String id, List<T> list){
        return list.stream().filter(r-> id.equals(r.getId()));
    }

    public static <T extends Id> T find(String id, List<T> list){
        return list.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .orElse(null);
    }
}
