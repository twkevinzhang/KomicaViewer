package self.nesl.komicaviewer.db;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public final class BoardPreferences {
    public static final String PREF_NAME="board_urls";
    public static final String KOMICA_NAME="komica";
    public static final String KOMICA_TOP50_BOARDS_NAME="komica_top50";
    public static final String KOMICA2_NAME="komica2";
    public static final String KOMICA2_TOP50_BOARDS_NAME="komica2_top50";

    private static Context context;
    public static void initialize(Context context) {
        BoardPreferences.context = context;
    }

    public static void updateUrls(final Map<String,String> urlMap,String host) {
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString(host,new Gson().toJson(urlMap)).apply();
    }

    public static Map<String,String> getUrls(String host) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        /*
        Google Gson - deserialize list<class> object? (generic type)?
        https://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type
         */
        return new Gson().fromJson(prefs.getString(host,""), new TypeToken<Map<String, String>>(){}.getType());
    }
}