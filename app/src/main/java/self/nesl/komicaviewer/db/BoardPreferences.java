package self.nesl.komicaviewer.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Map;

import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.KomicaHost;

import static android.content.Context.MODE_PRIVATE;
import static self.nesl.komicaviewer.util.ProjectUtil.arrayMapToArrayPost;
import static self.nesl.komicaviewer.util.ProjectUtil.arrayPostToArrayMap;
import static self.nesl.komicaviewer.util.Util.print;

public final class BoardPreferences {
    public static final String PREF_NAME = "board_urls";
    public static final String[] COLUMNS = new String[]{"title", "url"};

    private static Context context;

    public static void initialize(Context context) {
        BoardPreferences.context = context;
    }

    public static void update(final Host host, Host.OnResponse onResponse) {
        KomicaHost komicaHost=new KomicaHost();
        komicaHost.downloadBoardlist(new Host.OnResponse() {
            @Override
            public void onResponse(ArrayList<Post> arrayList) {
                context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(host.getHost(), new Gson().toJson(arrayPostToArrayMap(arrayList))).apply();
                onResponse.onResponse(arrayList);
            }
        });
    }

    public static ArrayList<Post> getBoards(Host host) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (prefs.getString(host.getHost(), "").equals("")) {
            return null;
        }

        /*
        Google Gson - deserialize list<class> object? (generic type)?
        https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
         */
        // todo: toJson(Post)
        ArrayList<Map<String, String>> maps= new Gson().fromJson(prefs.getString(host.getHost(), ""), new TypeToken<ArrayList<Map<String, String>>>() {}.getType());
        return arrayMapToArrayPost(maps);
    }


}