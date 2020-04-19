package self.nesl.komicaviewer.util;

import android.util.Log;

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

}
