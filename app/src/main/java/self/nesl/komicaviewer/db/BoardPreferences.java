package self.nesl.komicaviewer.db;
import android.content.Context;
import android.content.SharedPreferences;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import self.nesl.komicaviewer.gson.KThreadAdapter;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.dto.Post;
import static android.content.Context.MODE_PRIVATE;
import static self.nesl.komicaviewer.Const.PARSER_HOST;
import static self.nesl.komicaviewer.util.Utils.print;

public final class BoardPreferences {
    public static final String PREF_NAME = "board_urls";
    public static final String COLUMN = "hosts";
    private static Context context;
    static Gson gson = new GsonBuilder().registerTypeAdapter(KThread.class, new KThreadAdapter()).create();

    public static void initialize(Context context) {
        BoardPreferences.context = context;

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (!prefs.getString(COLUMN, "").equals("")) return;

        print(BoardPreferences.class,"AndroidNetworking GET",PARSER_HOST+"/hosts");
        AndroidNetworking.get(PARSER_HOST+"/hosts")
                .build().getAsJSONObject(new JSONObjectRequestListener(){
            @Override
            public void onResponse(JSONObject response) {
                context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(
                        COLUMN,
                        response.toString()
                ).apply();
            }
            @Override
            public void onError(ANError anError) {anError.printStackTrace();}
        });
    }

    public static KThread getHosts() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String s=prefs.getString(COLUMN, "");
        print("this",s);
        if(s.equals(""))return null;
        return gson.fromJson(s, KThread.class);
    }

    private static final String MAP_TITLE_COLUMN="title";
    private static final String MAP_LINK_COLUMN="link";
    private static ArrayList<KThread> arrayMapToArrayPost(ArrayList<Map<String, String>> maps){
        if(maps==null)return null;
        ArrayList<KThread> arrayList=new ArrayList<KThread>();
        for(Map<String, String> map : maps){
            String url=map.get(MAP_LINK_COLUMN);
            KThread p=new KThread(url,url);
            p.setTitle(map.get(MAP_TITLE_COLUMN));
            arrayList.add(p);
        }
        return arrayList;
    }

    private static ArrayList<Map<String, String>> arrayPostToArrayMap(ArrayList<? extends Post> threads){
        if(threads==null || threads.size()==0)return null;
        ArrayList<Map<String, String>> maps=new ArrayList<>();
        for(Post p : threads){
            Map<String,String> myMap = new HashMap<String,String>();
            myMap.put(MAP_TITLE_COLUMN,p.getTitle());
            myMap.put(MAP_LINK_COLUMN,p.getUrl());
            maps.add(myMap);
        }
        return maps;
    }
}