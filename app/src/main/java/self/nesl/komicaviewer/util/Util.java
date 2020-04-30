package self.nesl.komicaviewer.util;

import android.util.Log;

import androidx.annotation.Nullable;

import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

public class Util {
    public static void print(@Nullable Object c, String s){
        if(c!=null){
            Log.e(c.getClass().getName(),s);
        }else{
            Log.e("print",s);
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

    public static String parseChiToEngWeek(String s){
        return s.replace("一","Mon")
                .replace("二","Tue")
                .replace("三","Wed")
                .replace("四","Thu")
                .replace("五","Fri")
                .replace("六","Sat")
                .replace("日","Sun");
//        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    public static Post getPostFormat(Document document, String boardUrl){
        String[] sora=new String[]{
                "komica.org",
                "2cat.org"};
        String host=new MyURL(boardUrl).getHost();
        if(host.contains(sora[0])){
            return new SoraPost().parseDoc(document,boardUrl);
        }else if(host.contains(sora[1])){
//            return new SoraPost().parseDoc(document,boardUrl);
        }
        return null;
    }
}
