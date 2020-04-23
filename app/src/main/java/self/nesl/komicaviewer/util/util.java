package self.nesl.komicaviewer.util;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;

import self.nesl.komicaviewer.R;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraPost;

public class util {
    public static String getHasHttpUrl(String s, String domain) {
        if(s.substring(0, 1).equals("/")){
            if(!s.substring(0, 2).equals("//")){
                s = "https://" + domain + s;
            }else{
                s="https:"+s;
            }
        }
        return s;
    }

    public static void print(String s){
        Log.e("print",s);
//        System.out.println(s);
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

}
