package self.nesl.komicaviewer.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.nesl.komicaviewer.dto.Board;
import self.nesl.komicaviewer.dto.Host;
import self.nesl.komicaviewer.gson.HostAdapter;
import self.nesl.komicaviewer.gson.KThreadAdapter;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.dto.Post;

import static android.content.Context.MODE_PRIVATE;
import static self.nesl.komicaviewer.Const.PARSER_HOST;
import static self.nesl.komicaviewer.util.Utils.print;

public final class BoardPreferences {
    public static final String PREF_NAME = "board_urls";
    private static Context context;
    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        BoardPreferences.context = context;
        BoardPreferences.prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        print(BoardPreferences.class, "AndroidNetworking GET", PARSER_HOST + "/hosts");
        AndroidNetworking.get(PARSER_HOST + "/hosts")
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Host.class, new HostAdapter()).create();
                Host[] hosts = gson.fromJson(response.toString(), Host[].class);
                SharedPreferences.Editor e = prefs.edit();
                for (Host host : hosts) {
                    e.putString(host.getDomain(), new Gson().toJson(host));
                }
                e.apply();
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }

    public static Host[] getHosts() {
        Map<String, ?> host_map = prefs.getAll();
        ArrayList<Host> hosts = new ArrayList<>();
        for (Map.Entry<String, ?> entry : host_map.entrySet()) {
            hosts.add(new Gson().fromJson(entry.getValue().toString(), Host.class));
        }
        return hosts.toArray(new Host[0]);
    }

    public static Host getHost(String domain) {
        return new Gson().fromJson(prefs.getString(domain, ""), Host.class);
    }
}