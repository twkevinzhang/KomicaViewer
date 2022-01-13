package self.nesl.komicaviewer.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import self.nesl.komicaviewer.ui.Layout;

public class Utils {
    static String[] engWeek = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    public static void print(@Nullable Object o, String... s) {
        String s1 = TextUtils.join(", ", s);
        String tag = o != null ? o.getClass().getName() : "print";
        Log.e(tag, s1);

    }

    public static void print(String... s) {
        print(null, s);
    }

    public static <T> List<T> concat(List<T> ...lists){
        ArrayList<T> all = new ArrayList<>();
        for (List<T> list:lists) {
            all.addAll(list);
        }
        return all;
    }

    public static Map<String, String[]> getStyleMap(String styleStr) {
        Map<String, String[]> keymaps = new HashMap<>();
        // margin-top:-80px !important;color:#fcc;border-bottom:1px
        String[] list = styleStr.split(":|;");
        for (int i = 0; i < list.length; i += 2) {
            keymaps.put(list[i].trim(), list[i + 1].trim().split(" "));
        }
        // { "margin-top":["-80px", "!important"], "color":["#fcc"], "border-bottom":["1px"] }
        return keymaps;
    }

    public static String parseChiToEngWeek(String s) {
        String[] chiWeek = new String[]{"一", "二", "三", "四", "五", "六", "日"};
        for (int i = 0; i < chiWeek.length; i++) {
            s = s.replace(chiWeek[i], engWeek[i]);
        }
        return s;
    }

    public static String parseJpnToEngWeek(String s) {
        String[] jpnWeek = new String[]{"月", "火", "水", "木", "金", "土", "日"};
        for (int i = 0; i < jpnWeek.length; i++) {
            s = s.replace(jpnWeek[i], engWeek[i]);
        }
        return s;
    }

    public static Document netWorking(String url) {
        try {
            HttpURLConnection connection1 = (HttpURLConnection) new URL(url).openConnection();
            connection1.setReadTimeout(10000);
            StringBuilder whole = new StringBuilder();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new BufferedInputStream(connection1.getInputStream())));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                whole.append(inputLine);
            in.close();
            return Jsoup.parse(whole.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
