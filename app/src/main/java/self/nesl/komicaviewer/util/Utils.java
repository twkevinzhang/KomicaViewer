package self.nesl.komicaviewer.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Utils {
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
        String[] jpnWeek=new String[]{"月","火","水","木","金","土","日"};
        for(int i=0;i<jpnWeek.length;i++){
            s=s.replace(jpnWeek[i],engWeek[i]);
        }
        return s;
    }

}
