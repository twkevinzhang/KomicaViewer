package self.nesl.komicaviewer.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

public class Util {
    public static void print(@Nullable Object c, String... s){
        String s1=TextUtils.join(", ",s);
        if(c!=null){
            Log.e(c.getClass().getName(),s1);
        }else{
            Log.e("print",s1);
        }

//        System.out.println(s);
    }

    public static Map<String, String[]> getStyleMap(String styleStr) {
        Map<String, String[]> keymaps = new HashMap<>();
        // margin-top:-80px !important;color:#fcc;border-bottom:1px solid #ccc; background-color: #333; text-align:center
        String[] list = styleStr.split(":|;");
        for (int i = 0; i < list.length; i+=2) {
            keymaps.put(list[i].trim(),list[i+1].trim().split(" "));
        }
        return keymaps;
    }

    static String[] engWeek=new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    public static String parseChiToEngWeek(String s){
        String[] chiWeek=new String[]{"一","二","三","四","五","六","日"};
        for(int i=0;i<chiWeek.length;i++){
            s=s.replace(chiWeek[i],engWeek[i]);
        }
        return s;
    }

    public static String parseJpnToEngWeek(String s){
        // todo
        String[] jpnWeek=new String[]{"月","火","水","木","金","土","日"};
        for(int i=0;i<jpnWeek.length;i++){
            s=s.replace(jpnWeek[i],engWeek[i]);
        }
        return s;
    }

    public static Post getPostFormat(Document document, String boardUrl){
        String[] sora=new String[]{
                "komica.org",
                "2cat.org"
        };
        String host=new MyURL(boardUrl).getHost();
        if(host.contains(sora[0])){
            return new SoraPost().parseDoc(document,boardUrl);
        }else if(host.contains(sora[1])){
//            return new SoraPost().parseDoc(document,boardUrl);
        }
        return null;
    }

    public static Element installThreadTag(Element threads){
        //如果找不到thread標籤，就是2cat.komica.org，要用addThreadTag()改成標準綜合版樣式
        if (threads.selectFirst("div.thread") == null) {
            print(null,"thread is null,將thread加入threads中，變成標準綜合版樣式");
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
}
