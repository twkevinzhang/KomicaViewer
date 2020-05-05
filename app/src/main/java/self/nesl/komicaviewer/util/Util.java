package self.nesl.komicaviewer.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.SoraBoard;
import self.nesl.komicaviewer.model.komica.SoraPost;

public class Util {
    public static void print(@Nullable Class c, String... s){
        String s1=TextUtils.join(", ",s);
        if(c!=null){
            Log.e(c.getName(),s1);
        }else{
            Log.e("print",s1);
        }

//        System.out.println(s);
    }

    public static void print(String... s){
        print(null,s);
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

}
